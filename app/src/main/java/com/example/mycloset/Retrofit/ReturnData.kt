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
    val checkAlarm: String,
    val manager: String
)

data class Success(
    val success : Boolean
)

data class Check(
    val success : Boolean,
    val email: String
)