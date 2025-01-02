package com.seiberl.protrackreader.ui.profile.dropzone

import androidx.lifecycle.ViewModel
import com.seiberl.protrackreader.persistance.repository.DropzoneRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class DropzoneViewModel @Inject constructor(
    private val dropzoneRepository: DropzoneRepository
): ViewModel() {

}