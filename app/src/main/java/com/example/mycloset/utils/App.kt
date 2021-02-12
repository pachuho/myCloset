package com.hochupa.mycloset.utils

import android.app.Application
import android.content.pm.PackageInfo
import android.content.pm.PackageManager
import android.util.Base64
import android.util.Log
import com.hochupa.mycloset.R
import com.kakao.sdk.common.KakaoSdk
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.security.MessageDigest
import java.security.NoSuchAlgorithmException


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

        getHashKey()
        // 카카오 앱 키
        KakaoSdk.init(this, getString(R.string.kakao_app_key))
        super.onCreate()
    }

    // HashKey 생성(Debug)
    private fun getHashKey() {
        var packageInfo: PackageInfo? = null
        try {
            packageInfo = packageManager.getPackageInfo(packageName, PackageManager.GET_SIGNATURES)
        } catch (e: PackageManager.NameNotFoundException) {
            e.printStackTrace()
        }
        if (packageInfo == null) Log.e("KeyHash", "KeyHash:null")
        for (signature in packageInfo!!.signatures) {
            try {
                val md: MessageDigest = MessageDigest.getInstance("SHA")
                md.update(signature.toByteArray())
                Log.d("KeyHash", Base64.encodeToString(md.digest(), Base64.DEFAULT))
            } catch (e: NoSuchAlgorithmException) {
                Log.e("KeyHash", "Unable to get MessageDigest. signature=$signature", e)
            }
        }
    }
}