package com.seiberl.protrackreader.ui.jumpimport.models.jumpfile

import com.seiberl.protrackreader.persistance.entities.Jump

class JumpFile {

    lateinit var jump: Jump
    lateinit var fileContent: List<String>

    fun parseFileContent(fileContent: List<String>) {
        this.fileContent = fileContent
        structure.forEach {
            it.readValue(fileContent)
        }

        jump = Jump(
            protrackId = SerialNumber.value,
            number = JumpNumber.value,
            timestamp = DateTime.value,
            exitAltitude = ExitAltitude.value,
            deploymentAltitude = DeploymentAltitude.value,
            freefallTime = FreefallTime.value,
            averageSpeed = AverageSpeed.value,
            maxSpeed = MaxSpeed.value,
            firstHalfSpeed = FirstHalfSpeed.value,
            secondHalfSpeed = SecondHalfSpeed.value,
            groundLevelPressure = GroundLevelPressure.value,
            freefallRecorded = FreefallRecorded.value,
            canopyRecorded = CanopyRecorded.value,
            sampleSize = SampleSize.value,
            samples = Samples.value
        )
    }

    object SerialNumber : StringSection(4)
    object JumpNumber : IntSection(5)
    object DateTime : DateTimeSection(6)
    object ExitAltitude : IntSection(8)
    object DeploymentAltitude : IntSection(9)
    object FreefallTime : IntSection(10)
    object AverageSpeed : IntSection(11)
    object MaxSpeed : IntSection(12)
    object FirstHalfSpeed : IntSection(14)
    object SecondHalfSpeed : IntSection(15)
    object GroundLevelPressure : IntSection(35)
    object FreefallRecorded : BooleanSection(36)
    object CanopyRecorded : BooleanSection(37)
    object SampleSize : IntSection(38)
    object Samples : IntArraySection(39)

    companion object {
        val structure = arrayOf<Section<*>>(
            SerialNumber,
            JumpNumber,
            DateTime,
            ExitAltitude,
            DeploymentAltitude,
            FreefallTime,
            AverageSpeed,
            MaxSpeed,
            FirstHalfSpeed,
            SecondHalfSpeed,
            GroundLevelPressure,
            FreefallRecorded,
            CanopyRecorded,
            SampleSize,
            Samples
        )
    }
}