package com.example.mycloset

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.TypedValue
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.widget.*
import android.widget.CompoundButton
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import kotlinx.android.synthetic.main.activity_sign_up_email.*
import java.util.*


class SignUpEmailActivity : AppCompatActivity() {
    // 정규식
    val symbolNickname: String = "[0-9|a-z|A-Z|ㄱ-ㅎ|ㅏ-ㅣ|가-힝| ]*"
    var gender: String = "man"
    var emailCheck = false
    var pwdCheck = false
    var pwdConfirmCheck = false
    var nickNameCheck = false
    var birthCheck = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sign_up_email)


        // textwatcher 지정
        et_email.addTextChangedListener(loginTextWatcher)
        et_pwd.addTextChangedListener(loginTextWatcher)
        et_pwdConfirm.addTextChangedListener(loginTextWatcher)
        et_nickName.addTextChangedListener(loginTextWatcher)
        btn_birth.addTextChangedListener(loginTextWatcher)

        // 툴바 뒤로가기
        setSupportActionBar(toolbar)
        val ab = supportActionBar!!
        ab.setDisplayShowTitleEnabled(false)
        ab.setDisplayHomeAsUpEnabled(true)

        // 이메일 입력 시
        et_email.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                emailCheck = addHelper(et_email.length() > 0 && !android.util.Patterns.EMAIL_ADDRESS.matcher(et_email.text.toString()).matches(),
                        ll_email, "이메일 주소를 다시 확인 해주세요", et_email)
            }

            override fun afterTextChanged(p0: Editable?) {

            }
        })

        // 비밀번호 입력 시
        et_pwd.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                addLength(ll_pwdLength, et_pwd, "16")
                pwdCheck = addHelper(et_pwd.length() in 1..7, ll_pwd, "비밀번호 8~16자를 입력해주세요", et_pwd)

                // 비밀번호 확인 입력 후 비밀번호 입력창 입력 시
                pwdCheck = addHelper(et_pwdConfirm.length() > 0 && et_pwd.text.toString() != et_pwdConfirm.text.toString(),
                        ll_pwdConfirm, "비밀번호가 일치하지 않아요", et_pwd)
            }

            override fun afterTextChanged(p0: Editable?) {

            }
        })

        // 비밀번호 확인 입력 시
        et_pwdConfirm.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                addLength(ll_pwdConfirmLength, et_pwdConfirm, "16")
                pwdConfirmCheck = addHelper(et_pwdConfirm.length() > 0 && et_pwd.text.toString() != et_pwdConfirm.text.toString(),
                        ll_pwdConfirm, "비밀번호가 일치하지 않아요", et_pwdConfirm)
            }

            override fun afterTextChanged(p0: Editable?) {

            }
        })

        // 닉네임 입력 시
        et_nickName.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                addLength(ll_nickNameLength, et_nickName, "8")
                nickNameCheck = addHelper(et_nickName.length() == 1 || !et_nickName.text.matches(symbolNickname.toRegex()),
                        ll_nickName, "특수문자 제외 2~8자를 입력해주세요", et_nickName)
            }

            override fun afterTextChanged(p0: Editable?) {

            }
        })

        // 생년월일 입력 시
        btn_birth.setOnClickListener {
            val calendar: Calendar = Calendar.getInstance()
            val cYear = calendar.get(Calendar.YEAR)
            val cMonth = calendar.get(Calendar.MONTH)
            val cDay = calendar.get(Calendar.DAY_OF_MONTH)

            val dialog = AlertDialog.Builder(this@SignUpEmailActivity).create()
            val edialog: LayoutInflater = LayoutInflater.from(this@SignUpEmailActivity)
            val mView: View = edialog.inflate(R.layout.dialog_datepicker, null)

            var year: NumberPicker = mView.findViewById(R.id.np_year)
            var month: NumberPicker = mView.findViewById(R.id.np_month)
            var day: NumberPicker = mView.findViewById(R.id.np_day)
            val cancel: TextView = mView.findViewById(R.id.tv_cancel)
            val save: TextView = mView.findViewById(R.id.tv_ok)

            //  순환 안되게 막기
            year.wrapSelectorWheel = false
            month.wrapSelectorWheel = false
            day.wrapSelectorWheel = false

            //  최소값 설정
            year.minValue = cYear - 100
            month.minValue = 1
            day.minValue = 1

            //  최대값 설정
            year.maxValue = cYear
            month.maxValue = cMonth + 1
            day.maxValue = cDay

            // 연도 조건
            fun yearCondition() : Unit{
                if (year.value == year.maxValue) {
                    month.maxValue = cMonth + 1
                    month.wrapSelectorWheel = false
                    day.maxValue = cDay
                    day.wrapSelectorWheel = false
                } else {
                    month.maxValue = 12
                    when (month.value) {
                        1, 3, 5, 7, 8, 10, 12 -> day.maxValue = 31
                        2 -> day.maxValue = 28
                        4, 6, 9, 11 -> day.maxValue = 30
                        else -> print("error")
                    }
                }
            }

            // 월 조건
            fun monthCondition() : Unit {
                if (year.value == year.maxValue && month.value == cMonth + 1) {
                    month.maxValue = cMonth + 1
                    month.wrapSelectorWheel = false
                    day.maxValue = cDay
                    day.wrapSelectorWheel = false
                } else {
                    month.wrapSelectorWheel = true
                    month.maxValue = 12
                    when (month.value) {
                        1, 3, 5, 7, 8, 10, 12 -> day.maxValue = 31
                        2 -> day.maxValue = 28
                        4, 6, 9, 11 -> day.maxValue = 30
                        else -> print("error")
                    }
                }
            }

            // 일 조건
            fun dayCondition() : Unit {
                if (year.value == cYear && month.value == month.maxValue && day.value == cDay) {
                    day.maxValue = cDay
                    day.wrapSelectorWheel = false
                } else {
                    day.wrapSelectorWheel = true
                    when (month.value) {
                        1, 3, 5, 7, 8, 10, 12 -> day.maxValue = 31
                        2 -> day.maxValue = 28
                        4, 6, 9, 11 -> day.maxValue = 30
                        else -> print("error")
                    }
                }
            }

            // 보여질 값 설정
            if (birthCheck)
            {
                val valueTemp = btn_birth.text.split("년 ", "월 ", "일")
                year.value = valueTemp[0].toInt()
                month.value = valueTemp[1].toInt()
                day.value = valueTemp[2].toInt()

                yearCondition()
                monthCondition()
                dayCondition()

            } else {
                year.value = cYear
                month.value = cMonth + 1
                day.value = cDay
            }

            // 년도 변화 감지
            year.setOnValueChangedListener { picker, oldVal, newVal ->
                yearCondition()
            }

            // 월 변화 감지
            month.setOnValueChangedListener { picker, oldVal, newVal ->
                if (oldVal == 1 && newVal == month.maxValue) year.value -= 1
                if (oldVal == month.maxValue && newVal == 1) year.value += 1
                monthCondition()
            }

            // 일 변화 감지
            day.setOnValueChangedListener { picker, oldVal, newVal ->
                if (oldVal == 1 && newVal == day.maxValue) month.value -= 1
                if (oldVal == day.maxValue && newVal == 1) month.value += 1
                dayCondition()
            }

            //  취소 버튼 클릭 시
            cancel.setOnClickListener {
                dialog.dismiss()
                dialog.cancel()
            }

            //  완료 버튼 클릭 시
            save.setOnClickListener {
                btn_birth.text = "${year.value}년 ${month.value}월 ${day.value}일"
                birthCheck = true
                dialog.dismiss()
                dialog.cancel()
            }

            dialog.setView(mView)
            dialog.create()
            dialog.show()
        }

        // 성별 선택 시
        rg_gender.setOnCheckedChangeListener { radioGroup, i ->
            when (i) {
                R.id.rb_man -> gender = "man"
                R.id.rb_woman -> gender = "woman"
                R.id.rb_etc -> gender = "etc"
            }
        }

        // 약관동의 시
        cb_all.setOnClickListener {
            onCheckChanged(cb_all)
            btn_signUp.isEnabled = cb_all.isChecked && emailCheck && pwdCheck && pwdConfirmCheck && nickNameCheck && birthCheck
//            Toast.makeText(this, "email:$emailCheck \npwd:$pwdCheck \n" +
//                    "pwdConfirm:$pwdConfirmCheck \nnickName:$nickNameCheck \nbirth:$birthCheck \nprivacy:${cb_privacy.isChecked}\nuse:${cb_use.isChecked}", Toast.LENGTH_SHORT).show()
        }
        cb_privacy.setOnClickListener {
            onCheckChanged(cb_privacy)
            btn_signUp.isEnabled = cb_privacy.isChecked && cb_use.isChecked && emailCheck && pwdCheck && pwdConfirmCheck && nickNameCheck && birthCheck
        }
        cb_use.setOnClickListener {
            onCheckChanged(cb_use)
            btn_signUp.isEnabled = cb_privacy.isChecked && cb_use.isChecked && emailCheck && pwdCheck && pwdConfirmCheck && nickNameCheck && birthCheck
        }
        cb_pushAlarm.setOnClickListener { onCheckChanged(cb_pushAlarm) }

        // 회원가입 버튼 클릭 시
        btn_signUp.setOnClickListener {

        }

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
    fun addHelper(condition: Boolean, layout: LinearLayout, inputText: String? = "텍스트를 입력해주세요", target : EditText): Boolean {
        if (condition) {
            layout.removeAllViews()
            val helper = TextView(this@SignUpEmailActivity)
            helper.text = " $inputText"
            helper.setTextColor(ContextCompat.getColor(this@SignUpEmailActivity, R.color.red))
            helper.setTextSize(TypedValue.COMPLEX_UNIT_SP, 11F)
            layout.addView(helper)
            return false
        } else if(target.text.isEmpty()) {
            layout.removeAllViews()
            return false
        } else {
            layout.removeAllViews()
            return true
        }
    }

    // EditText 글자 수 체크
    fun addLength(layout: LinearLayout, editName: EditText, maxLength: String? = "8") {
        layout.removeAllViews()
        val checkLength = TextView(this@SignUpEmailActivity)
        checkLength.text = "${editName.text.length}/${maxLength}"
        checkLength.setTextSize(TypedValue.COMPLEX_UNIT_SP, 10F)
        layout.addView(checkLength)
    }

    // 이용약관 체크박스
    private fun onCheckChanged(compoundButton: CompoundButton) {
        when (compoundButton.id) {
            R.id.cb_all -> {
                if (cb_all.isChecked) {
                    cb_privacy.isChecked = true
                    cb_use.isChecked = true
                    cb_pushAlarm.isChecked = true
                } else {
                    cb_privacy.isChecked = false
                    cb_use.isChecked = false
                    cb_pushAlarm.isChecked = false
                }
            }
            else -> cb_all.isChecked = (cb_privacy.isChecked && cb_use.isChecked && cb_pushAlarm.isChecked)
        }
    }

    // 회원가입 버튼 활성화
    private val loginTextWatcher: TextWatcher = object : TextWatcher {
        override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {}
        override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {}
        override fun afterTextChanged(s: Editable) {
            btn_signUp.isEnabled = emailCheck && pwdCheck && pwdConfirmCheck && nickNameCheck && birthCheck && cb_privacy.isChecked && cb_use.isChecked
        }
    }
}