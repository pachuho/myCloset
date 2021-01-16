package com.example.mycloset

import android.app.Application
import com.kakao.sdk.common.KakaoSdk

class App : Application() {
    companion object {
        lateinit var prefs: ProfileSharedPreferences
    }

    override fun onCreate() {
        prefs = ProfileSharedPreferences(applicationContext)
        KakaoSdk.init(this, getString(R.string.kakao_app_key))
        super.onCreate()
    }
}