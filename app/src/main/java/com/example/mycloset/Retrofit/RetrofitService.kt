package com.example.mycloset.Retrofit

import retrofit2.Call
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.POST

interface RetrofitService {
    // 로그인
    @FormUrlEncoded
    @POST("signIn.php")
    fun requestSignIn(@Field("email") email:String,
                      @Field("pwd") pwd:String ) : Call<SignIn>

    // 회원가입
    @FormUrlEncoded
    @POST("signUp.php")
    fun requestSignUp(@Field("email") email:String,
                      @Field("pwd") pwd:String,
                      @Field("nickName") nickName:String,
                      @Field("birthday") birthday:String,
                      @Field("gender") gender:String,
                      @Field("checkAlarm") checkAlarm:String,
                      @Field("signUpDate") signUpDate:String) : Call<SignUp>
}