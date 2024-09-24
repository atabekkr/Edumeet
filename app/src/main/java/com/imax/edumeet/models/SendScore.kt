package com.imax.edumeet.models

data class SendScore(
    val studentId: String,
    val lesson: String,
    val topic: String,
    val score: Int
)
