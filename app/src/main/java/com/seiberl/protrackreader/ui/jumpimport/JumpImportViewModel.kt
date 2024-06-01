package com.seiberl.protrackreader.ui.jumpimport

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.seiberl.protrackreader.persistance.dao.JumpDao
import com.seiberl.protrackreader.persistance.repository.JumpRepository
import com.seiberl.protrackreader.util.IoDispatcher
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.launch
import javax.inject.Inject

typealias Navigation = () -> Unit

data class ImportUiState(
    val hasDuplicates: Boolean = false
)

@HiltViewModel
class JumpImportViewModel @Inject constructor(
    private val repository: JumpRepository,
    @IoDispatcher private val ioDispatcher: CoroutineDispatcher
) : ViewModel() {

    private val recordedJumps = mutableListOf<Int>()
    private val _uiState = MutableStateFlow(ImportUiState())

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

    }

    fun onCancelClick() {
        onShowNextActivity()
    }

}