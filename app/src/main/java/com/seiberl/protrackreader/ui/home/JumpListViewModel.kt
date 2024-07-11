package com.seiberl.protrackreader.ui.home

import android.os.Environment
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.seiberl.protrackreader.persistance.repository.JumpRepository
import com.seiberl.protrackreader.persistance.views.JumpMetaData
import com.seiberl.protrackreader.util.IoDispatcher
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

data class JumpListUiState(
    val jumps: List<JumpMetaData> = emptyList(),
    val showPermissionDialog: Boolean = false
)

@HiltViewModel
class JumpListViewModel @Inject constructor(
    private val repository: JumpRepository,
    @IoDispatcher private val ioDispatcher: CoroutineDispatcher
) : ViewModel() {


    private val _uiState = MutableStateFlow(JumpListUiState())
    val uiState: StateFlow<JumpListUiState> = _uiState

    lateinit var fabClickEvent: () -> Unit
    lateinit var onRequestPermission: () -> Unit

    init {
        viewModelScope.launch(ioDispatcher) {
            // Get and observe all jumps from database
            repository.observeJumpMetaData().collect { updatedJumps ->
                _uiState.update { oldState ->
                    oldState.copy(jumps = updatedJumps.sortedByDescending { it.number })
                }
            }
        }

        // Check if we have permission to read external storage (= Protrack II)
        if (!Environment.isExternalStorageManager()) {
            _uiState.update { it.copy(showPermissionDialog = true) }
        }
    }

    fun onJumpImportClick() {
        fabClickEvent()
    }

    fun onPermissionDialogDismiss() {
        _uiState.update { it.copy(showPermissionDialog = false) }
    }

    fun onPermissionDialogConfirmed() {
        _uiState.update { it.copy(showPermissionDialog = false) }
        onRequestPermission()
    }
}