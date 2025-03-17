package com.seiberl.protrackreader.ui.home

import android.content.Context
import android.net.Uri
import android.os.Environment
import androidx.core.content.FileProvider
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.seiberl.protrackreader.persistance.entities.Aircraft
import com.seiberl.protrackreader.persistance.entities.Canopy
import com.seiberl.protrackreader.persistance.entities.Dropzone
import com.seiberl.protrackreader.persistance.repository.JumpRepository
import com.seiberl.protrackreader.persistance.views.JumpMetaData
import com.seiberl.protrackreader.ui.home.models.JumpLogPdfCreator
import com.seiberl.protrackreader.util.IoDispatcher
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject
import kotlin.math.max
import kotlin.math.min

data class JumpListUiState(
    val jumps: List<JumpMetaData> = emptyList(),
    val selectedJumps: List<Int> = emptyList(),
    val aircraft: List<Aircraft> = emptyList(),
    val canopies: List<Canopy> = emptyList(),
    val dropzones: List<Dropzone> = emptyList(),
    val showPermissionDialog: Boolean = false,
    val showPrintJumpsDialog: Boolean = false,
    val showEditMetaDialog: Boolean = false,
    val dialogErrorToJumpField: Boolean = false,
    val dialogErrorFromJumpField: Boolean = false
)

private const val AUTHORITY = "com.seiberl.protrackreader.fileprovider"

@HiltViewModel
class JumpListViewModel @Inject constructor(
    private val repository: JumpRepository,
    private val jumpLogPdfCreator: JumpLogPdfCreator,
    @IoDispatcher private val ioDispatcher: CoroutineDispatcher,
    @ApplicationContext private val context: Context
) : ViewModel() {


    private val _uiState = MutableStateFlow(JumpListUiState())
    val uiState: StateFlow<JumpListUiState> = _uiState

    lateinit var fabClickEvent: () -> Unit
    lateinit var onRequestPermission: () -> Unit
    lateinit var onShareFile: (fileUri: Uri) -> Unit

    private var requestedPermission: Boolean = false
    private val storagePermissionGranted: Boolean
        get() = Environment.isExternalStorageManager()

    val shouldRestartApp: Boolean
        get() = requestedPermission && storagePermissionGranted

    init {
        viewModelScope.launch(ioDispatcher) {
            // Get and observe all jumps from database
            async {
                repository.observeJumpMetaData().collect { updatedJumps ->
                    _uiState.update { oldState ->
                        oldState.copy(jumps = updatedJumps.sortedByDescending { it.number })
                    }
                }
            }.start()
            async {
                repository.observeAircraft().collect { aircraft ->
                    _uiState.update { oldState ->
                        oldState.copy(aircraft = aircraft)
                    }
                }
            }.start()
            async {
                repository.observeCanopies().collect { canopies ->
                    _uiState.update { oldState ->
                        oldState.copy(canopies = canopies)
                    }
                }
            }.start()
            async {
                repository.observeDropzone().collect { dropzones ->
                    _uiState.update { oldState ->
                        oldState.copy(dropzones = dropzones)
                    }
                }
            }.start()
        }

        // Check if we have permission to read external storage (= Protrack II)
        if (!storagePermissionGranted) {
            _uiState.update { it.copy(showPermissionDialog = true) }
        }
    }

    fun onJumpSelected(jumpNumber: Int) {
        if (_uiState.value.selectedJumps.contains(jumpNumber)) {
            _uiState.update { oldState ->
                oldState.copy(selectedJumps = oldState.selectedJumps.filter { it != jumpNumber })
            }
        } else {
            _uiState.update { oldState ->
                oldState.copy(selectedJumps = oldState.selectedJumps + jumpNumber)
            }
        }
    }

    fun onJumpImportClick() = fabClickEvent()

    fun onPermissionDialogDismiss() = _uiState.update { it.copy(showPermissionDialog = false) }

    fun onPermissionDialogConfirmed() {
        _uiState.update { it.copy(showPermissionDialog = false) }
        requestedPermission = true
        onRequestPermission()
    }

    fun deleteSelectedJumps() {
        viewModelScope.launch(ioDispatcher) {
            repository.removeJumps(uiState.value.selectedJumps)
            _uiState.update { it.copy(selectedJumps = emptyList()) }
        }
    }

    fun onCreatePdfClicked() {
        if (_uiState.value.jumps.isNotEmpty()) {
            _uiState.update { it.copy(showPrintJumpsDialog = true) }
        }
    }

    fun onEditMetaDataClicked() {
        if (_uiState.value.jumps.isNotEmpty()) {
            _uiState.update { it.copy(showEditMetaDialog = true) }
        }
    }

    fun onPrintJumpsDialogDismiss() = _uiState.update { it.copy(showPrintJumpsDialog = false) }

    fun onEditMetaDialogDismiss() = _uiState.update { it.copy(showEditMetaDialog = false) }

    fun printJumps(startJumpNr: String, endJumpNr: String) {
        val fromIsValid = isJumpNrValid(startJumpNr)
        val toIsValid = isJumpNrValid(endJumpNr)

        when {
            !fromIsValid && !toIsValid -> _uiState.update {
                it.copy(dialogErrorFromJumpField = true, dialogErrorToJumpField = true)
            }
            !fromIsValid ->  _uiState.update {
                it.copy(dialogErrorFromJumpField = true, dialogErrorToJumpField = false)
            }
            !toIsValid -> _uiState.update {
                it.copy(dialogErrorFromJumpField = false, dialogErrorToJumpField = true)
            }
            else -> {
                onPrintJumpsDialogDismiss()
                val lowerJumpNr = min(startJumpNr.toInt(), endJumpNr.toInt())
                val upperJumpNr = max(startJumpNr.toInt(), endJumpNr.toInt())
                val jumpsToPrint = _uiState.value.jumps.filter {
                    it.number in lowerJumpNr..upperJumpNr
                }

                val pdfFile = jumpLogPdfCreator.createJumpLogPdf(jumpsToPrint)
                val fileUri = FileProvider.getUriForFile(context, AUTHORITY, pdfFile)
                onShareFile(fileUri)
            }
        }
    }

    fun onEditMetaDialogConfirmed(
        jumpMetadata: List<JumpMetaData>,
        aircraft: Aircraft?,
        canopy: Canopy?,
        dropzone: Dropzone?
    ) {
        onEditMetaDialogDismiss()
        viewModelScope.launch(ioDispatcher) {
            repository.updateJumpsMetaData(jumpMetadata, aircraft, canopy, dropzone)
        }
    }

    private fun isJumpNrValid(jumpNrToVerify: String): Boolean {
        // Minimum is last jump number and maximum is first jump number because sorted descending
        val minJumpNr = _uiState.value.jumps.lastOrNull()?.number ?: 0
        val maxJumpNr = _uiState.value.jumps.firstOrNull()?.number ?: 0
        val jumpNr = jumpNrToVerify.toIntOrNull()
        return jumpNr != null && jumpNr >= minJumpNr && jumpNr <= maxJumpNr
    }

}