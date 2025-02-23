package com.seiberl.protrackreader.ui.profile.dropzone

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.seiberl.protrackreader.persistance.entities.Dropzone
import com.seiberl.protrackreader.persistance.repository.DropzoneRepository
import com.seiberl.protrackreader.util.IoDispatcher
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

data class UIState(
    val dropzone: List<Dropzone>,
    val showAddDropzoneDialog: Boolean = false
)

@HiltViewModel
class DropzoneViewModel @Inject constructor(
    private val dropzoneRepository: DropzoneRepository,
    @IoDispatcher private val ioDispatcher: CoroutineDispatcher
): ViewModel() {

    private val _uiState = MutableStateFlow(UIState(emptyList())).apply {
        viewModelScope.launch {
            dropzoneRepository.observe().collect {
                value = value.copy(dropzone = it)
            }
        }
    }
    val uiState: StateFlow<UIState> = _uiState

    fun onAddDropzoneClick() {
        _uiState.value = _uiState.value.copy(showAddDropzoneDialog = true)
    }

    fun addDropzone(dropzone: Dropzone) {
        _uiState.value = _uiState.value.copy(showAddDropzoneDialog = false)
        viewModelScope.launch(ioDispatcher) {
            dropzoneRepository.add(dropzone)
        }
    }

    fun starDropzone(dropzone: Dropzone) {
        viewModelScope.launch(ioDispatcher) {
            dropzoneRepository.star(dropzone)
        }
    }

    fun onDialogDismiss() {
        _uiState.value = uiState.value.copy(showAddDropzoneDialog = false)
    }

    fun removeDropzone(dropzone: Dropzone) {
        viewModelScope.launch(ioDispatcher) {
            dropzoneRepository.remove(dropzone)
        }
    }
}