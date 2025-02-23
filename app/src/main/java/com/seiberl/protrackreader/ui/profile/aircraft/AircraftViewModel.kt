package com.seiberl.protrackreader.ui.profile.aircraft

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.seiberl.protrackreader.persistance.entities.Aircraft
import com.seiberl.protrackreader.persistance.repository.AircraftRepository
import com.seiberl.protrackreader.util.IoDispatcher
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

data class UIState(
    val aircraft: List<Aircraft>,
    val showAddAircraftDialog: Boolean = false
)

@HiltViewModel
class AircraftViewModel @Inject constructor(
    private val aircraftRepository: AircraftRepository,
    @IoDispatcher private val ioDispatcher: CoroutineDispatcher
): ViewModel() {

    private val _uiState = MutableStateFlow(UIState(emptyList())).apply {
        viewModelScope.launch {
            aircraftRepository.observe().collect {
                value = value.copy(aircraft = it)
            }
        }
    }
    val uiState: StateFlow<UIState> = _uiState

    fun onAddAircraftClick() {
        _uiState.value = _uiState.value.copy(showAddAircraftDialog = true)
    }

    fun addAircraft(aircraft: Aircraft) {
        _uiState.value = _uiState.value.copy(showAddAircraftDialog = false)
        viewModelScope.launch(ioDispatcher) {
            aircraftRepository.add(aircraft)
        }
    }

    fun starAircraft(aircraft: Aircraft) {
        viewModelScope.launch(ioDispatcher) {
            aircraftRepository.star(aircraft)
        }
    }

    fun onDialogDismiss() {
        _uiState.value = uiState.value.copy(showAddAircraftDialog = false)
    }

    fun removeAircraft(aircraft: Aircraft) {
        viewModelScope.launch(ioDispatcher) {
            aircraftRepository.remove(aircraft)
        }
    }
}