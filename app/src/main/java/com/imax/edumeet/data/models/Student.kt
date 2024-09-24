package com.imax.edumeet.data.models

data class Student(
    val _id: String,
    val group: String,
    val name: String,
    val originalPassword: String,
    val password: String,
    val phone: String,
    val profileImage: String
)