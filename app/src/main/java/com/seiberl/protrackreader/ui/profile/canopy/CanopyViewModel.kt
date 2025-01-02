package com.seiberl.protrackreader.ui.profile.canopy

import androidx.lifecycle.ViewModel
import com.seiberl.protrackreader.persistance.repository.CanopyRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class CanopyViewModel @Inject constructor(
    private val canopyRepository: CanopyRepository
): ViewModel() {

}