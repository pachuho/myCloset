package com.example.mycloset.Retrofit

import retrofit2.Call
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.POST

interface RetrofitService {
    // 로그인
    @FormUrlEncoded
    @POST("signIn.php")
    fun requestLogin(@Field("email") email:String,
                     @Field("pwd") pwd:String ) : Call<Login>

    // 회원가입
    @FormUrlEncoded
    @POST("signUp.php")
    fun requestRegister(@Field("email") email:String,
                        @Field("pwd") pwd:String ) : Call<Login>
}