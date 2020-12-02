package com.example.mycloset.Retrofit

import retrofit2.Call
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.POST

interface LoginService {
    @FormUrlEncoded
    @POST("signin.php")
    // 쿼리 변수
    fun requestLogin(@Field("email") email:String,
                     @Field("pwd") pwd:String ) : Call<Login>

}