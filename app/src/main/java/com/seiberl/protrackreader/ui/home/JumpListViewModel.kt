package com.seiberl.protrackreader.ui.home

import android.content.Intent
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.seiberl.protrackreader.persistance.entities.Jump
import com.seiberl.protrackreader.persistance.repository.JumpRepository
import com.seiberl.protrackreader.persistance.views.JumpMetaData
import com.seiberl.protrackreader.ui.jumpimport.JumpImportActivity
import com.seiberl.protrackreader.util.IoDispatcher
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject
import javax.inject.Singleton

data class JumpListUiState(
    val jumps: List<JumpMetaData> = emptyList(),
)

@HiltViewModel
class JumpListViewModel @Inject constructor(
    private val repository: JumpRepository,
    @IoDispatcher private val ioDispatcher: CoroutineDispatcher
) : ViewModel() {


    private val _uiState = MutableStateFlow(JumpListUiState())
    val uiState: StateFlow<JumpListUiState> = _uiState

    lateinit var clickEvent: () -> Unit

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

    fun onJumpImportClick() {
        clickEvent()
    }
}