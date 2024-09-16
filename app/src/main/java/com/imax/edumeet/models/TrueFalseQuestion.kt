package com.imax.edumeet.models

data class TrueFalseQuestion(
    val correct_answer: Boolean,
    val id: Int,
    val question: String
)