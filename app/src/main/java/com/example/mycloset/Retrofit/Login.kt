package com.example.mycloset.Retrofit

data class Login(
    val success : Boolean,
    val email: String,
    val pwd: String,
    val nickName: String,
    val birthday: String,
    val gender: String,
    val checkAlarm: String
)

