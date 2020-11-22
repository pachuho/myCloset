package com.example.mycloset

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.TypedValue
import android.view.MenuItem
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import kotlinx.android.synthetic.main.activity_sign_up_email.*

class SignUpEmailActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sign_up_email)

        // 툴바 뒤로가기
        setSupportActionBar(toolbar)
        val ab = supportActionBar!!
        ab.setDisplayShowTitleEnabled(false)
        ab.setDisplayHomeAsUpEnabled(true)

        // 이메일 입력 시
        et_email.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}

            override fun afterTextChanged(p0: Editable?) {
                if (et_email.length() > 0 && !android.util.Patterns.EMAIL_ADDRESS.matcher(et_email.text).matches())
                {
                    ll_email.removeAllViews()
                    val emailHelper = TextView(this@SignUpEmailActivity)
                    emailHelper.text = " 이메일 주소를 다시 확인 해주세요"
                    emailHelper.setTextColor(ContextCompat.getColor(this@SignUpEmailActivity, R.color.red))
                    emailHelper.setTextSize(TypedValue.COMPLEX_UNIT_SP, 12F)
                    ll_email.addView(emailHelper)
                } else {
                    ll_email.removeAllViews()
                }
            }
        })

        // 비밀번호 입력 시
        et_pwd.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                ll_pwdLength.removeAllViews()
                val pwdLength = TextView(this@SignUpEmailActivity)
                pwdLength.text = "${et_pwd.text.length}/16"
                pwdLength.setTextSize(TypedValue.COMPLEX_UNIT_SP, 10F)
                ll_pwdLength.addView(pwdLength)
            }

            override fun afterTextChanged(p0: Editable?) {
                if (et_pwd.length() in 1..7) {
                    ll_pwd.removeAllViews()
                    val pwdHelper = TextView(this@SignUpEmailActivity)
                    pwdHelper.text = " 비밀번호 8~16자를 입력해주세요"
                    pwdHelper.setTextColor(ContextCompat.getColor(this@SignUpEmailActivity, R.color.red))
                    pwdHelper.setTextSize(TypedValue.COMPLEX_UNIT_SP, 12F)
                    ll_pwd.addView(pwdHelper)
                } else {
                    ll_pwd.removeAllViews()
                }
            }
        })

        et_pwdConfirm.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                ll_pwdConfirmLength.removeAllViews()
                val pwdConfirmLength = TextView(this@SignUpEmailActivity)
                pwdConfirmLength.text = "${et_pwdConfirm.text.length}/16"
                pwdConfirmLength.setTextSize(TypedValue.COMPLEX_UNIT_SP, 10F)
                ll_pwdConfirmLength.addView(pwdConfirmLength)
            }

            override fun afterTextChanged(p0: Editable?) {
                if(et_pwdConfirm.length() > 0 && et_pwd.text.toString() != et_pwdConfirm.text.toString()) {
                    ll_pwdConfirm.removeAllViews()
                    val pwdConfirm = TextView(this@SignUpEmailActivity)
                    pwdConfirm.text = " 비밀번호가 일치하지 않아요"
                    pwdConfirm.setTextColor(ContextCompat.getColor(this@SignUpEmailActivity, R.color.red))
                    pwdConfirm.setTextSize(TypedValue.COMPLEX_UNIT_SP, 12F)
                    ll_pwdConfirm.addView(pwdConfirm)
                } else {
                    ll_pwdConfirm.removeAllViews()
                }
            }
        })












    }

    // 툴바 뒤로 가기 클릭 시
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        val id = item.itemId
        when (id) {
            android.R.id.home -> {
                finish()
                overridePendingTransition(R.anim.slide_from_left, R.anim.slide_to_right)
                return true
            }
        }
        return super.onOptionsItemSelected(item)
    }

    // 뒤로 가기 클릭 시
    override fun onBackPressed() {
        finish()
        overridePendingTransition(R.anim.slide_from_left, R.anim.slide_to_right)
    }

}
