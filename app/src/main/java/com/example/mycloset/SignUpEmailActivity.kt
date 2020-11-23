package com.example.mycloset

import android.content.Context
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.TypedValue
import android.view.MenuItem
import android.view.OrientationEventListener
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import kotlinx.android.synthetic.main.activity_sign_up_email.*

class SignUpEmailActivity : AppCompatActivity(), View.OnClickListener {
    val symbolNickname : String = "[0-9|a-z|A-Z|ㄱ-ㅎ|ㅏ-ㅣ|가-힝| ]*"
    val symbolBirthday : String = "[0-9]{2}(0[1-9]|1[0-2])(0[1-9]|[1,2][0-9]|3[0,1])"

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
                addHelper(et_email.length() > 0 && !android.util.Patterns.EMAIL_ADDRESS.matcher(et_email.text.toString()).matches(), ll_email, "이메일 주소를 다시 확인 해주세요")
            }
        })

        // 비밀번호 입력 시
        et_pwd.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                addLength(ll_pwdLength, et_pwd, "16")
            }

            override fun afterTextChanged(p0: Editable?) {
                addHelper(et_pwd.length() in 1..7, ll_pwd, "비밀번호 8~16자를 입력해주세요")

                // 비밀번호 확인 입력 후 비밀번호 입력창 입력 시
                addHelper(et_pwdConfirm.length() > 0 && et_pwd.text.toString() != et_pwdConfirm.text.toString(), ll_pwdConfirm, "비밀번호가 일치하지 않아요")
            }
        })

        // 비밀번호 확인 입력 시
        et_pwdConfirm.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                addLength(ll_pwdConfirmLength, et_pwdConfirm, "16")
            }

            override fun afterTextChanged(p0: Editable?) {
                addHelper(et_pwdConfirm.length() > 0 && et_pwd.text.toString() != et_pwdConfirm.text.toString(), ll_pwdConfirm, "비밀번호가 일치하지 않아요")
            }
        })

        // 닉네임 입력 시
        et_nickName.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                addLength(ll_nickNameLength, et_nickName, "8")
            }

            override fun afterTextChanged(p0: Editable?) {
                addHelper(et_nickName.length() == 1 || !et_nickName.text.matches(symbolNickname.toRegex()), ll_nickName, "특수문자 제외 2~8자를 입력해주세요")
            }
        })

        // 생년월일 입력 시

        button.setOnClickListener(this)

//        et_birthday.addTextChangedListener(object : TextWatcher {
//            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}
//
//            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}
//
//            override fun afterTextChanged(p0: Editable?) {
//                addHelper(et_birthday.length() > 1 && !et_birthday.text.matches(symbolBirthday.toRegex()), ll_birthday, "생년월일을 다시 확인 해주세요")
//            }
//        })

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

    // EditText 하단 도움말
    fun addHelper(condition : Boolean, layout: LinearLayout, inputText: String? = "텍스트를 입력해주세요") {
        if (condition) {
            layout.removeAllViews()
            val helper = TextView(this@SignUpEmailActivity)
            helper.text = " $inputText"
            helper.setTextColor(ContextCompat.getColor(this@SignUpEmailActivity, R.color.red))
            helper.setTextSize(TypedValue.COMPLEX_UNIT_SP, 11F)
            layout.addView(helper)
        } else layout.removeAllViews()
    }

    // EditText 글자 수 체크
    fun addLength(layout : LinearLayout, editName : EditText, maxLength : String? = "8") {
        layout.removeAllViews()
        val checkLength = TextView(this@SignUpEmailActivity)
        checkLength.text = "${editName.text.length}/${maxLength}"
        checkLength.setTextSize(TypedValue.COMPLEX_UNIT_SP, 10F)
        layout.addView(checkLength)
    }

    // 다이얼로그 생성
    override fun onClick(v: View?) {
        when(v?.id) {
            button.id -> {
                val dialog = BirthdayDialog(this)
                dialog.setOnOKClickedListener{ content -> button.text = content
                }
                dialog.start()
            }
        }
    }
}
