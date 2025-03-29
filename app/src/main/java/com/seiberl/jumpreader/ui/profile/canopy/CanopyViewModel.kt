package com.seiberl.jumpreader.ui.profile.canopy

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.seiberl.jumpreader.persistance.entities.Canopy
import com.seiberl.jumpreader.persistance.repository.CanopyRepository
import com.seiberl.jumpreader.util.IoDispatcher
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

data class UIState(
    val canopy: List<Canopy>,
    val showAddCanopyDialog: Boolean = false
)

@HiltViewModel
class CanopyViewModel @Inject constructor(
    private val canopyRepository: CanopyRepository,
    @IoDispatcher private val ioDispatcher: CoroutineDispatcher
) : ViewModel() {
    private val _uiState = MutableStateFlow(UIState(emptyList())).apply {
        viewModelScope.launch {
            canopyRepository.observe().collect {
                value = value.copy(canopy = it)
            }
        }
    }
    val uiState: StateFlow<UIState> = _uiState

    fun onAddCanopyClick() {
        _uiState.value = _uiState.value.copy(showAddCanopyDialog = true)
    }

    fun addCanopy(canopy: Canopy) {
        _uiState.value = _uiState.value.copy(showAddCanopyDialog = false)
        viewModelScope.launch(ioDispatcher) {
            canopyRepository.add(canopy)
        }
    }

    fun starCanopy(canopy: Canopy) {
        viewModelScope.launch(ioDispatcher) {
            canopyRepository.star(canopy)
        }
    }

    fun onDialogDismiss() {
        _uiState.value = uiState.value.copy(showAddCanopyDialog = false)
    }

    fun removeCanopy(canopy: Canopy) {
        viewModelScope.launch(ioDispatcher) {
            canopyRepository.remove(canopy)
        }
    }
}