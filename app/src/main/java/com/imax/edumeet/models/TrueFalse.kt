package com.imax.edumeet.models

data class TrueFalse(
    val questions: List<TrueFalseQuestion>,
    val test_id: Int,
    val title: String,
    val topic_id: Int
)