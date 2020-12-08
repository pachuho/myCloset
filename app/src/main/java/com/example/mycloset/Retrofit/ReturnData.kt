package com.example.mycloset.Retrofit

data class SignIn(
    val success : Boolean,
    val email: String,
    val kakaoId: String,
    val googleId: String,
    val pwd: String,
    val nickName: String,
    val birthday: String,
    val gender: String,
    val checkAlarm: String
)

data class Success(
    val success : Boolean
)