package com.example.mycloset

import android.app.DatePickerDialog
import android.content.Context
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.TypedValue
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.OrientationEventListener
import android.view.View
import android.widget.*
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import kotlinx.android.synthetic.main.activity_sign_up_email.*
import kotlinx.android.synthetic.main.dialog_datepicker.*
import java.time.Year
import java.util.*

class SignUpEmailActivity : AppCompatActivity(){
    // 정규식
    val symbolNickname : String = "[0-9|a-z|A-Z|ㄱ-ㅎ|ㅏ-ㅣ|가-힝| ]*"
    var gender : String = "man"
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
        btn_birth.setOnClickListener {
            val calendar : Calendar = Calendar.getInstance()
            val cYear = calendar.get(Calendar.YEAR)
            val cMonth = calendar.get(Calendar.MONTH)
            val cDay = calendar.get(Calendar.DAY_OF_MONTH)

            val dialog = AlertDialog.Builder(this@SignUpEmailActivity).create()
            val edialog : LayoutInflater = LayoutInflater.from(this@SignUpEmailActivity)
            val mView : View = edialog.inflate(R.layout.dialog_datepicker,null)

            var year : NumberPicker = mView.findViewById(R.id.np_year)
            var month : NumberPicker = mView.findViewById(R.id.np_month)
            var day : NumberPicker = mView.findViewById(R.id.np_day)
            val cancel : TextView = mView.findViewById(R.id.tv_cancel)
            val save : TextView = mView.findViewById(R.id.tv_ok)

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

            // 보여질 값 설정
            year.value = cYear
            month.value = cMonth + 1
            day.value = cDay

            // 년도 변화 감지
            year.setOnValueChangedListener{picker, oldVal, newVal ->
                if(year.value == year.maxValue) {
                    month.maxValue = cMonth + 1
                    month.wrapSelectorWheel = false
                    day.maxValue = cDay
                    day.wrapSelectorWheel = false
                }
                else {
                    month.maxValue = 12
                    when(month.value) {
                        1, 3, 5, 7, 8, 10, 12 -> day.maxValue = 31
                        2 -> day.maxValue = 28
                        4, 6, 9, 11 -> day.maxValue = 30
                        else -> print("error")
                    }

                }
            }

            // 월 변화 감지
            month.setOnValueChangedListener{picker, oldVal, newVal ->
                if (oldVal == 1 && newVal == month.maxValue) year.value -= 1
                if (oldVal == month.maxValue && newVal == 1) year.value += 1
                if (year.value == year.maxValue && month.value == cMonth + 1) {
                    month.maxValue = cMonth + 1
                    month.wrapSelectorWheel = false
                    day.maxValue = cDay
                    day.wrapSelectorWheel = false
                } else {
                    month.wrapSelectorWheel = true
                    month.maxValue = 12
                    when(newVal) {
                        1, 3, 5, 7, 8, 10, 12 -> day.maxValue = 31
                        2 -> day.maxValue = 28
                        4, 6, 9, 11 -> day.maxValue = 30
                        else -> print("error")
                    }
                }
            }

            // 일 변화 감지
            day.setOnValueChangedListener { picker, oldVal, newVal ->
                if (oldVal == 1 && newVal == day.maxValue) month.value -= 1
                if (oldVal == day.maxValue && newVal == 1) month.value += 1
                if (year.value == cYear && month.value == month.maxValue && day.value == cDay) {
                    day.maxValue = cDay
                    day.wrapSelectorWheel = false
                } else {
                    day.wrapSelectorWheel = true
                    when(month.value) {
                        1, 3, 5, 7, 8, 10, 12 -> day.maxValue = 31
                        2 -> day.maxValue = 28
                        4, 6, 9, 11 -> day.maxValue = 30
                        else -> print("error")
                    }
                }
            }

            //  취소 버튼 클릭 시
            cancel.setOnClickListener {
                dialog.dismiss()
                dialog.cancel()
            }

            //  완료 버튼 클릭 시
            save.setOnClickListener {
                btn_birth.text = "${year.value}년 ${month.value}월 ${day.value}일"
                dialog.dismiss()
                dialog.cancel()
            }

            dialog.setView(mView)
            dialog.create()
            dialog.show()
        }

        // 성별 선택 시
        rg_gender.setOnCheckedChangeListener { radioGroup, i ->
            when(i){
                R.id.rb_man -> gender = "man"
                R.id.rb_woman -> gender = "woman"
                R.id.rb_etc -> gender = "etc"
            }
        }

        // 약관동의 시
        cb_all.setOnClickListener { onCheckChanged(cb_all) }
        cb_privacy.setOnClickListener { onCheckChanged(cb_privacy) }
        cb_pushAlarm.setOnClickListener { onCheckChanged(cb_pushAlarm) }
        cb_use.setOnClickListener { onCheckChanged(cb_use) }

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

    // 체크박스
    private fun onCheckChanged(compoundButton: CompoundButton) {
        when(compoundButton.id) {
            R.id.cb_all -> {
                if (cb_all.isChecked) {
                    cb_privacy.isChecked = true
                    cb_pushAlarm.isChecked = true
                    cb_use.isChecked = true
                }else {
                    cb_privacy.isChecked = false
                    cb_pushAlarm.isChecked = false
                    cb_use.isChecked = false
                }
            }
            else -> cb_all.isChecked = (cb_privacy.isChecked && cb_pushAlarm.isChecked && cb_use.isChecked)
        }
    }
}
