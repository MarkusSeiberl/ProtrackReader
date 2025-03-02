package com.seiberl.protrackreader.ui.jumpdetail

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.patrykandpatrick.vico.core.cartesian.data.CartesianChartModelProducer
import com.patrykandpatrick.vico.core.cartesian.data.lineSeries
import com.seiberl.protrackreader.persistance.models.JumpDetail
import com.seiberl.protrackreader.persistance.repository.JumpDetailRepository
import com.seiberl.protrackreader.util.IoDispatcher
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject
import kotlin.math.pow

data class JumpDetailUiState(
    val jumpDetail: JumpDetail?,
    val chartModel: CartesianChartModelProducer
)

@HiltViewModel
class JumpDetailViewModel @Inject constructor(
    private val jumpDetailRepository: JumpDetailRepository,
    @IoDispatcher private val ioDispatcher: CoroutineDispatcher
) : ViewModel() {

    private val modelProducer = CartesianChartModelProducer()
    private val _uiState = MutableStateFlow(JumpDetailUiState(null, modelProducer))
    val uiState = _uiState.asStateFlow()

    fun loadJump(jumpNr: Int) {
        viewModelScope.launch(ioDispatcher) {
            val jumpDetail = jumpDetailRepository.loadJump(jumpNr)
            _uiState.update { it.copy(jumpDetail = jumpDetail) }
            calculateChartValues(jumpDetail)
        }
    }

    private suspend fun calculateChartValues(jumpDetail: JumpDetail?) {
        if (jumpDetail == null) {
            return
        }
        val jump = jumpDetail.jump

        val profile = jump.samples.map { it.toAltitude(jump.groundLevelPressure) }
        val time = List(profile.size) { index -> index * 0.25 }
        val speed = profile.map {  }
        Log.d("TAAG", "max time = ${time.maxOrNull()}")



        modelProducer.runTransaction {
            lineSeries {
                series(x = time, y = profile)
            }
        }
    }
}

fun Int.toAltitude(groundLevelPressure: Int): Double {
    return 44330.77 * (1 - (this / groundLevelPressure.toDouble()).pow(0.1902632))
}