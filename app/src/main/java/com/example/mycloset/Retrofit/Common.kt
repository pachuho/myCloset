package com.example.mycloset.Retrofit

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object Common {
    var retrofit = Retrofit.Builder()
        .baseUrl("http://52.79.235.161/myCloset/")
        .addConverterFactory(GsonConverterFactory.create())
        .build()
}