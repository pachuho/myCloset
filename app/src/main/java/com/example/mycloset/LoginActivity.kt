package com.example.mycloset

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.mycloset.R.string.kakao_app_key
import com.example.mycloset.Retrofit.Common
import com.example.mycloset.Retrofit.SignIn
import com.example.mycloset.Retrofit.RetrofitService
import com.kakao.sdk.auth.LoginClient
import com.kakao.sdk.auth.model.OAuthToken
import com.kakao.sdk.common.KakaoSdk
import com.kakao.sdk.common.util.Utility
import com.kakao.sdk.user.UserApiClient
import kotlinx.android.synthetic.main.activity_login.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class LoginActivity : AppCompatActivity() {
    var lastBackPressedTime: Long = 0
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        // 로그인 버튼
        btn_login.setOnClickListener {
            val signInService: RetrofitService = Common.retrofit.create(RetrofitService::class.java)
            val inputEmail = login_et_email.text.toString()
            val inputPwd = login_et_pwd.text.toString()

            signInService.requestSignIn(inputEmail,inputPwd).enqueue(object: Callback<SignIn> {
                // 통신 성공
                override fun onResponse(call: Call<SignIn>, response: Response<SignIn>) {
                    val signIn = response.body()
                    // 로그인 성공(회원정보 있으면)
                    if (signIn?.success == true) {
                        Toast.makeText(this@LoginActivity, "로그인 성공", Toast.LENGTH_SHORT).show()


                    }
                    // 로그인 실패(회원정보 없으면)
                    else Toast.makeText(this@LoginActivity, "회원 정보를 확인해주세요", Toast.LENGTH_SHORT).show()
                }

                // 통신 실패
                override fun onFailure(call: Call<SignIn>, t: Throwable) {
                    Log.e("signIn", t.message.toString())
                    Toast.makeText(this@LoginActivity, "로그인 실패", Toast.LENGTH_SHORT).show()
                }
            })
        }

        // 회원가입-카카오톡 버튼
        btn_signUpKaKao.setOnClickListener {
            val TAG = "카카오"

             // 로그인 공통 callback 구성
            val callback: (OAuthToken?, Throwable?) -> Unit = { token, error ->
                if (error != null) {
                    Log.e(TAG, "로그인 실패", error)
                }
                else if (token != null) {
                    Log.i(TAG, "로그인 성공 ${token.accessToken}")
                }
            }

            // 카카오톡이 설치되어 있으면 카카오톡으로 로그인, 아니면 카카오계정으로 로그인
            if (LoginClient.instance.isKakaoTalkLoginAvailable(this)) {
                LoginClient.instance.loginWithKakaoTalk(this, callback = callback)
            } else {
                LoginClient.instance.loginWithKakaoAccount(this, callback = callback)
            }







        }

        // 회원가입-페이스북 버튼
        btn_signUpFacebook.setOnClickListener {
            Toast.makeText(this, "업데이트 예정", Toast.LENGTH_SHORT).show()
        }

        // 회원가입-이메일 버튼
        btn_signUpEmail.setOnClickListener {
            val intent = Intent(this, SignUpEmailActivity::class.java)
            startActivity(intent)
            overridePendingTransition(R.anim.slide_from_right, R.anim.slide_to_left);
        }

    }

    // 뒤로가기
    override fun onBackPressed() {
        val currentTime = System.currentTimeMillis()
        val differenceTime = currentTime - lastBackPressedTime
        if (differenceTime in 0..2000) {
            finish()
        } else {
            lastBackPressedTime = currentTime
            Toast.makeText(applicationContext, "한번 더 누르시면 종료됩니다.", Toast.LENGTH_SHORT).show()
        }
    }

}

