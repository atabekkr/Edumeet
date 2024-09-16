package com.imax.edumeet.models

data class TestMatching(
    val matching_tests: List<MatchingTest>
)

data class MatchingTest(
    val topic_id: Int,
    val test_id: Int,
    val title: String,
    val questions: List<MatchingQuestion>,
    val answers: List<MatchingAnswers>
)

data class MatchingQuestion(
    val id: Int,
    val question: String,
    val correct_answer_id: Int
)

data class MatchingAnswers(
    val id: Int,
    val answer: String
)