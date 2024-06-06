package com.seiberl.protrackreader.ui.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.seiberl.protrackreader.persistance.entities.Jump
import com.seiberl.protrackreader.persistance.repository.JumpRepository
import com.seiberl.protrackreader.persistance.views.JumpMetaData
import com.seiberl.protrackreader.util.IoDispatcher
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

data class JumpListUiState(
    val jumps: List<JumpMetaData> = emptyList(),
)

@HiltViewModel
class JumpListViewModel @Inject constructor(
    private val repository: JumpRepository,
    @IoDispatcher private val ioDispatcher: CoroutineDispatcher
) : ViewModel() {

    private val _uiState = MutableStateFlow(JumpListUiState())
    val uiState: StateFlow<JumpListUiState> = _uiState.asStateFlow()

    private val _jumps = mutableListOf<JumpMetaData>()
    val jumps: List<JumpMetaData>
        get() = _jumps.toList()

    init {
        viewModelScope.launch(ioDispatcher) {
            repository.observeJumpMetaData().collect { updatedJumps ->
                _uiState.update { it.copy(jumps = updatedJumps.sortedByDescending { it.number }) }
            }
        }
    }
}