package com.seiberl.protrackreader.ui.jumpdetail

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.seiberl.protrackreader.persistance.entities.Jump
import com.seiberl.protrackreader.persistance.repository.JumpDetailRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

data class JumpDetailUiState(
    val jump: Jump?
)

@HiltViewModel
class JumpDetailViewModel @Inject constructor(
    private val jumpDetailRepository: JumpDetailRepository
) : ViewModel() {

    private var jumpDetail: Jump? = null

    private val _uiState = MutableStateFlow(JumpDetailUiState(null))
    val uiState = _uiState.asStateFlow()

    fun loadJump(jumpNr: Int) {
        viewModelScope.launch {
            jumpDetail = jumpDetailRepository.loadJump(jumpNr)
        }
    }
}