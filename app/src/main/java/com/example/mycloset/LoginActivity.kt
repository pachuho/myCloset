package com.example.mycloset

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import kotlinx.android.synthetic.main.activity_login.*

class LoginActivity : AppCompatActivity() {
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
            val intent = Intent(this, SignUpEmailActivity::class.java)
            startActivity(intent)
        }
    }
}