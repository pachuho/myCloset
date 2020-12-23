package com.example.mycloset.retrofit

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
                      @Field("kakaoId") kakaoId:String,
                      @Field("googleId") googleId:String,
                      @Field("pwd") pwd:String,
                      @Field("nickName") nickName:String,
                      @Field("birthday") birthday:String,
                      @Field("gender") gender:String,
                      @Field("checkAlarm") checkAlarm:String,
                      @Field("signUpDate") signUpDate:String,
                      @Field("manager") manager:String) : Call<Success>

    // 이메일 중복확인
    @FormUrlEncoded
    @POST("checkEmail.php")
    fun requestCheckEmail(@Field("email") email:String) : Call<Success>

    // 닉네임 중복확인
    @FormUrlEncoded
    @POST("checkNickName.php")
    fun requestCheckNickName(@Field("nickName") nickName: String) : Call<Success>

    // 카카오 계정 조회
    @FormUrlEncoded
    @POST("checkKakaoId.php")
    fun requestCheckKakaoId(@Field("kakaoId") kakaoId: String) : Call<Check>

    // 구글 계정 조회
    @FormUrlEncoded
    @POST("checkGoogleId.php")
    fun requestCheckGoogleId(@Field("googleId") googleId: String) : Call<Check>
}