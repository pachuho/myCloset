package com.example.mycloset

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.example.mycloset.Retrofit.Login
import com.example.mycloset.Retrofit.LoginService
import kotlinx.android.synthetic.main.activity_login.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory


class LoginActivity : AppCompatActivity() {
    var lastBackPressedTime: Long = 0
    var login:Login? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        var retrofit = Retrofit.Builder()
            .baseUrl("http://52.79.235.161/myCloset/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
        var loginService: LoginService = retrofit.create(LoginService::class.java)



        // 로그인 버튼
        btn_login.setOnClickListener {
            var inputEmail = login_et_email.text.toString()
            var inputPwd = login_et_pwd.text.toString()

            loginService.requestLogin(inputEmail,inputPwd).enqueue(object: Callback<Login> {
                override fun onFailure(call: Call<Login>, t: Throwable) {
                    Log.e("LOGIN", t.message.toString())
                    var dialog = AlertDialog.Builder(this@LoginActivity)
                    dialog.setTitle("에러")
                    dialog.setMessage("호출실패했습니다.")
                    dialog.show()
                }

                override fun onResponse(call: Call<Login>, response: Response<Login>) {
                    login = response.body()
                    Log.d("LOGIN","success : "+login?.success)
                    Log.d("LOGIN","email : "+login?.email)
                    Log.d("LOGIN","nickName : "+login?.nickName)
                    Log.d("LOGIN","birthday : "+login?.birthday)
                    Log.d("LOGIN","gender : "+login?.gender)
                    Log.d("LOGIN","checkAlarm : "+login?.checkAlarm)
                    Toast.makeText(this@LoginActivity, "${login?.success}", Toast.LENGTH_SHORT).show()

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
        val DifferenceTime = currentTime - lastBackPressedTime
        if (DifferenceTime in 0..2000) {
            finish()
        } else {
            lastBackPressedTime = currentTime
            Toast.makeText(applicationContext, "한번 더 누르시면 종료됩니다.", Toast.LENGTH_SHORT).show()
        }
    }

}

