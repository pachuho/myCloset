package com.example.mycloset

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.mycloset.Retrofit.Common
import com.example.mycloset.Retrofit.Login
import com.example.mycloset.Retrofit.RetrofitService
import kotlinx.android.synthetic.main.activity_login.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class LoginActivity : AppCompatActivity() {
    var lastBackPressedTime: Long = 0
    var login:Login? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        val loginService: RetrofitService = Common.retrofit.create(RetrofitService::class.java)

        // 로그인 버튼
        btn_login.setOnClickListener {
            val inputEmail = login_et_email.text.toString()
            val inputPwd = login_et_pwd.text.toString()

            loginService.requestLogin(inputEmail,inputPwd).enqueue(object: Callback<Login> {
                // 통신 성공
                override fun onResponse(call: Call<Login>, response: Response<Login>) {
                    login = response.body()
                    // 로그인 성공(회원정보 있으면)
                    if (login?.success == true) {
                        Toast.makeText(this@LoginActivity, "로그인 성공", Toast.LENGTH_SHORT).show()


                    }
                    // 로그인 실패(회원정보 없으면)
                    else Toast.makeText(this@LoginActivity, "회원 정보를 확인해주세요", Toast.LENGTH_SHORT).show()
                }

                // 통신 실패
                override fun onFailure(call: Call<Login>, t: Throwable) {
                    Log.e("LOGIN", t.message.toString())
                    Toast.makeText(this@LoginActivity, "로그인 실패", Toast.LENGTH_SHORT).show()
                }
            })
        }

        // 회원가입-카카오톡 버튼
        btn_signUpKaKao.setOnClickListener {
            Toast.makeText(this, "업데이트 예정", Toast.LENGTH_SHORT).show()
        }

        // 회원가입-페이스북 버튼
        btn_signUpFacebook.setOnClickListener {
            Toast.makeText(this, "업데이트 예정", Toast.LENGTH_SHORT).show()
        }

        // 회원가입-이메일 버튼
        btn_signUpEmail.setOnClickListener {
            // Check if we're running on Android 5.0 or higher
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

