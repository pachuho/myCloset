package com.hochupa.mycloset.utils

import android.app.Application
import com.hochupa.mycloset.R
import com.kakao.sdk.common.KakaoSdk
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

// 가장 먼저 초기화될 부분
class App : Application() {

    // 레트로핏 공통 url
    object Common {
        var retrofit: Retrofit = Retrofit.Builder()
                .baseUrl("http://52.79.235.161/myCloset/")
                .addConverterFactory(GsonConverterFactory.create())
                .build()
    }

    // 쉐어드프리퍼런스
    companion object {
        lateinit var prefs: ProfileSharedPreferences
    }

    override fun onCreate() {
        prefs = ProfileSharedPreferences(applicationContext)

        // 카카오 앱 키
        KakaoSdk.init(this, getString(R.string.kakao_app_key))
        super.onCreate()
    }
}