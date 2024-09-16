package com.imax.edumeet.data.models

data class RegisterResponse(
    val message: String,
    val student: Student,
    val token: String
)