package com.seiberl.protrackreader.ui.jumpimport

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.seiberl.protrackreader.persistance.entities.Jump
import com.seiberl.protrackreader.persistance.repository.JumpRepository
import com.seiberl.protrackreader.ui.jumpimport.models.ImportState
import com.seiberl.protrackreader.ui.jumpimport.models.ImportState.BEFORE_IMPORT
import com.seiberl.protrackreader.ui.jumpimport.models.JumpFileReader
import com.seiberl.protrackreader.util.IoDispatcher
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

typealias Navigation = () -> Unit

data class ImportUiState(
    val importState: ImportState = ImportState.BEFORE_IMPORT,
    val currentJumpNumber: Int = 0
)

private const val TAG = "JumpImportViewModel"

@HiltViewModel
class JumpImportViewModel @Inject constructor(
    private val repository: JumpRepository,
    private val jumpFileReader: JumpFileReader,
    @IoDispatcher private val ioDispatcher: CoroutineDispatcher
) : ViewModel() {

    private val recordedJumps = mutableListOf<Int>()
    private val _uiState = MutableStateFlow(ImportUiState())

    private val isDuplicatePredicate: ((Jump) -> Boolean) = { jump ->
        recordedJumps.contains(jump.number)
    }
    private val isNewPredicate: ((Jump) -> Boolean) = { jump ->
        isDuplicatePredicate.invoke(jump).not()
    }

    val uiState: StateFlow<ImportUiState> = _uiState.asStateFlow()

    lateinit var onShowNextActivity: Navigation

    init {
        viewModelScope.launch(ioDispatcher) {
            repository.observeJumpNumbers().distinctUntilChanged().collect { updatedJumps ->
                recordedJumps.clear()
                recordedJumps.addAll(updatedJumps)
            }
        }
    }

    fun onImportClick() {
        Log.d(TAG, "Button Click: Import")
        val success = jumpFileReader.findProtrackVolume()
        if (!success) {
//            onShowNextActivity()
            return
        }

        val jumpNumbers = jumpFileReader.readStoredJumpNumbers()
        Log.d(TAG, "Found ${jumpNumbers.size} already stored jumps")

        viewModelScope.launch(ioDispatcher) {
            Log.d(TAG, "Starting import.")
            importJumps()
        }.invokeOnCompletion {
            onShowNextActivity() // todo show finished screen - either retry or finish
        }
    }


    private fun importJumps(overrideDuplicates: Boolean = false) {
        val recordedJumpData = jumpFileReader.readStoredJumps()

        val (newJumps, duplicateJumps) = if (overrideDuplicates) {
            // Split new jumps up in jumps to simply import and jumps that should be updated.
            val newJumps = recordedJumpData.filter(isNewPredicate)
            val duplicateJumps = recordedJumpData.filter(isDuplicatePredicate)
            newJumps to duplicateJumps
        } else {
            val newJumps = recordedJumpData.filter(isNewPredicate)
            newJumps to emptyList()
        }

        repository.insertJumpNumbers(newJumps, duplicateJumps)
    }

    fun onCancelClick() {
        onShowNextActivity()
    }
}