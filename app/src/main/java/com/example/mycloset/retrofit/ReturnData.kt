package com.example.mycloset.retrofit

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

data class Dress(
    val code : Int,
    val part : String,
    val brand : String,
    val name : String,
    val price : Int,
    val image : String,
    val link : String
)