package com.example.mycloset

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import kotlinx.android.synthetic.main.activity_login.*

class LoginActivity : AppCompatActivity() {
    var lastBackPressedTime: Long = 0
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        // 로그인 버튼
        btn_login.setOnClickListener {
            Toast.makeText(this, "업데이트 예정", Toast.LENGTH_SHORT).show()
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
        }
        else {
            lastBackPressedTime = currentTime
            Toast.makeText(applicationContext, "한번 더 누르시면 종료됩니다.",Toast.LENGTH_SHORT).show()
        }
    }
}