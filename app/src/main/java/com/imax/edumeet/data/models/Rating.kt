package com.imax.edumeet.data.models

data class Rating(
    val data: List<RatingOfStudent>
)

data class RatingOfStudent(
    val student: Student,
    val totalTopicsCompleted: Int,
    val percentage: Int
)