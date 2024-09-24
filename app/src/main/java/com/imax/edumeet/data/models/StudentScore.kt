package com.imax.edumeet.data.models

data class StudentScore(
    val lessons: List<Lesson>
)

data class Lesson(
    val lesson: String,
    val percentage: Int,
    val topics: List<Any>
)
