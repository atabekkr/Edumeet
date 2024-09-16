package com.imax.edumeet.models

data class TestClassic(
    val tests_classic: List<TestClassicByTopic>
)

data class TestClassicByTopic(
    val topic_id: Int,
    val description: String,
    val questions: List<ClassicQuestion>
)

data class ClassicQuestion(
    val id: Int,
    val question: String,
    val options: List<String>,
    val correct_answer: String
)
