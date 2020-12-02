package com.example.mycloset.Retrofit

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object MainRetrofit {
    // 위에서 만든 RetrofitService를 연결해줍니다.
    fun getService(): LoginService = retrofit.create(LoginService::class.java)

    private val retrofit = Retrofit.Builder()
            .baseUrl("http://52.79.235.161/") // 도메인 주소
            .addConverterFactory(GsonConverterFactory.create()) // GSON을 사용하기 위해 ConverterFactory에 GSON 지정
            .build()
}