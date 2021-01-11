package com.example.mycloset

import com.example.mycloset.viewpager.PageItem
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object Common {
    var retrofit = Retrofit.Builder()
        .baseUrl("http://52.79.235.161/myCloset/")
        .addConverterFactory(GsonConverterFactory.create())
        .build()
}

object Profile {
    lateinit var UserName: String
    lateinit var UserPwd: String
    lateinit var UserBirthday: String
    lateinit var UserGender: String
    var UserAlarm: Boolean = false
    var UserGoogle: Boolean = false
    var UserKakao: Boolean = false
    var favoriteImage = ArrayList<PageItem>()
}