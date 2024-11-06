package com.imax.edumeet.data.ai

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class TranscriptionResult(
    @SerialName("text") val summary: String,
    @SerialName("language") val language: String,
    @SerialName("duration") val duration: Double,
    @SerialName("segments") val segments: List<Segment>,
)

@Serializable
data class Segment(
    @SerialName("id") val id: Int,
    @SerialName("seek") val seek: Int,
    @SerialName("start") val start: Double,
    @SerialName("end") val end: Double,
    @SerialName("text") val text: String,
)