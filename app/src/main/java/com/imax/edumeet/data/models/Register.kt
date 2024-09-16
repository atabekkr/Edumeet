package com.imax.edumeet.data.models

data class Register(
    val name: String,
    val password: String,
    val phone: String,
    val group: String,
    val kurs: String = "4-kurs",
    val profileImage: String = "url"
)
