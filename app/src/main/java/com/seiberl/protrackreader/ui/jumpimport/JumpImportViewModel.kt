package com.seiberl.protrackreader.ui.jumpimport

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.seiberl.protrackreader.persistance.repository.JumpRepository
import com.seiberl.protrackreader.ui.jumpimport.models.ImportState
import com.seiberl.protrackreader.ui.jumpimport.models.ImportState.IMPORT_FAILED
import com.seiberl.protrackreader.ui.jumpimport.models.ImportState.IMPORT_ONGOING
import com.seiberl.protrackreader.ui.jumpimport.models.ImportState.IMPORT_START_FAILED
import com.seiberl.protrackreader.ui.jumpimport.models.ImportState.IMPORT_SUCCESSFUL
import com.seiberl.protrackreader.ui.jumpimport.models.JumpFileReader
import com.seiberl.protrackreader.ui.jumpimport.models.JumpImporter
import com.seiberl.protrackreader.util.IoDispatcher
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.async
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
    val currentJumpNumber: Int = 0,
    val importedJumps: Int = 0
)

private const val TAG = "JumpImportViewModel"

@HiltViewModel
class JumpImportViewModel @Inject constructor(
    private val repository: JumpRepository,
    private val jumpFileReader: JumpFileReader,
    private val jumpImporter: JumpImporter,
    @IoDispatcher private val ioDispatcher: CoroutineDispatcher
) : ViewModel() {

    private val recordedJumps = mutableListOf<Int>()
    private val _uiState = MutableStateFlow(ImportUiState())

    private val isDuplicatePredicate: ((Int) -> Boolean) = { jumpNr ->
        recordedJumps.contains(jumpNr)
    }
    private val isNewPredicate: ((Int) -> Boolean) = { jumpNr ->
        isDuplicatePredicate.invoke(jumpNr).not()
    }

    private val exceptionHandler = CoroutineExceptionHandler { _, exception ->
        Log.e(TAG, "Exception: $exception")
        _uiState.update { it.copy(importState = IMPORT_FAILED) }
    }

    val uiState: StateFlow<ImportUiState> = _uiState.asStateFlow()

    lateinit var onShowNextActivity: Navigation

    init {
        viewModelScope.launch(ioDispatcher) {
            async {
                repository.observeJumpNumbers().distinctUntilChanged().collect { updatedJumps ->
                    recordedJumps.clear()
                    recordedJumps.addAll(updatedJumps)
                }
            }.start()
            async {
                jumpImporter.currentJumpImport.collect { jumpNr ->
                    _uiState.update {
                        it.copy(
                            currentJumpNumber = jumpNr,
                            importedJumps = it.importedJumps.inc()
                        )
                    }
                }
            }.start()
        }
    }

    fun onImportClick() {
        Log.d(TAG, "Button Click: Import")
        val success = jumpFileReader.findProtrackVolume()
        if (!success) {
            _uiState.update { it.copy(importState = IMPORT_START_FAILED) }
            return
        }

        val jumpNumbers = jumpFileReader.readStoredJumpNumbers()
        Log.d(TAG, "Found ${jumpNumbers.size} already stored jumps")

        viewModelScope.launch(ioDispatcher) {
            Log.d(TAG, "Starting import.")
            _uiState.update { it.copy(importState = IMPORT_ONGOING) }
            launch(exceptionHandler) {
                importJumps(jumpNumbers)
                _uiState.update { it.copy(importState = IMPORT_SUCCESSFUL) }
            }
        }
    }


    private fun importJumps(jumpNumbers: List<Int>, overrideDuplicates: Boolean = false) {
        val (newJumps, duplicateJumps) = if (overrideDuplicates) {
            // Split new jumps up in jumps to simply import and jumps that should be updated.
            val newJumps = jumpNumbers.filter(isNewPredicate)
            val duplicateJumps = jumpNumbers.filter(isDuplicatePredicate)
            newJumps to duplicateJumps
        } else {
            // Ignore all duplicates - just import new jumps
            val newJumps = jumpNumbers.filter(isNewPredicate)
            newJumps to emptyList()
        }

        val sortedNewJumps = newJumps.sorted()
        val sortedDuplicatedJumps = duplicateJumps.sorted()

        jumpImporter.importJumps(sortedNewJumps, sortedDuplicatedJumps)
    }

    fun onCancelClick() {
        onShowNextActivity()
    }

    fun onContinueClick() {
        onShowNextActivity()
    }
}