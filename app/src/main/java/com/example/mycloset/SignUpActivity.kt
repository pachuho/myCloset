package com.example.mycloset

import android.content.Context
import android.content.Intent
import android.graphics.Rect
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.util.TypedValue
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.MotionEvent
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.*
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.example.mycloset.Retrofit.*
import kotlinx.android.synthetic.main.activity_sign_up.*
import kotlinx.android.synthetic.main.dialog_datepicker.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.text.DateFormat
import java.text.SimpleDateFormat
import java.util.*


class SignUpActivity : AppCompatActivity() {
    // 정규식
    val symbolNickname: String = "[0-9|a-z|A-Z|ㄱ-ㅎ|ㅏ-ㅣ|가-힝| ]*"

    // 입력 여부 확인
    var emailCheck = false
    var nickNameCheck = false

    // 기타 변수 선언
    var gender: String = "man"
    var birthday: String = "0000-00-00"
    var manager: String = "N"

    // 카카오, 구글 로그인
    var kakaoId = "Null"
    var googleId = "Null"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sign_up)

        if(intent.hasExtra("kakaoId")) {
            kakaoId = intent.getStringExtra("kakaoId").toString()
            Toast.makeText(this, kakaoId, Toast.LENGTH_SHORT).show()
        } else if (intent.hasExtra("googleId")){
            googleId = intent.getStringExtra("googleId").toString()
        }

        // 비밀번호 레이아웃 제거
        ll_TotalPwd.removeAllViews()

        // textWatcher 지정
        et_email.addTextChangedListener(loginTextWatcher)
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
            override fun afterTextChanged(p0: Editable?) {}
            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                emailCheck = addHelper(
                        et_email.length() > 0 && !android.util.Patterns.EMAIL_ADDRESS.matcher(
                                et_email.text.toString()
                        ).matches(),
                        ll_email, et_email, "이메일 주소를 다시 확인 해주세요"
                )
            }
        })

        // 이메일 중복 확인
        et_email.onFocusChangeListener = View.OnFocusChangeListener { v, hasFocus ->
            if (emailCheck && !hasFocus) checkEmail(
                    true, ll_email, et_email,
                    "이미 사용중인 이메일입니다.", "사용 가능한 이메일입니다."
            )}

        // 닉네임 입력 시
        et_nickName.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}
            override fun afterTextChanged(p0: Editable?) {}
            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                addLength(ll_nickNameLength, et_nickName, "8")
                nickNameCheck = addHelper(
                        et_nickName.length() == 1 || !et_nickName.text.matches(
                                symbolNickname.toRegex()
                        ),
                        ll_nickName, et_nickName, "특수문자 제외 2~8자를 입력해주세요"
                )
            }
        })

        // 닉네임 중복 확인
        et_nickName.onFocusChangeListener = View.OnFocusChangeListener { v, hasFocus ->
            if (nickNameCheck && !hasFocus) checkNickName(
                    true, ll_nickName, et_nickName,
                    "이미 사용중인 닉네임입니다.", "사용 가능한 닉네임입니다."
            )}

        // 생년월일 입력 시
        btn_birth.setOnClickListener {
            val calendar: Calendar = Calendar.getInstance()
            val cYear = calendar.get(Calendar.YEAR)
            val cMonth = calendar.get(Calendar.MONTH)
            val cDay = calendar.get(Calendar.DAY_OF_MONTH)

            val dialog = AlertDialog.Builder(this@SignUpActivity).create()
            val edialog: LayoutInflater = LayoutInflater.from(this@SignUpActivity)
            val mView: View = edialog.inflate(R.layout.dialog_datepicker, null)

            var year: NumberPicker = mView.findViewById(R.id.np_year)
            var month: NumberPicker = mView.findViewById(R.id.np_month)
            var day: NumberPicker = mView.findViewById(R.id.np_day)

            val cancel: TextView = mView.findViewById(R.id.tv_cancel)
            val save: TextView = mView.findViewById(R.id.tv_ok)

            //  순환 안되게 막기
            year.wrapSelectorWheel = false
//            month.wrapSelectorWheel = false
//            day.wrapSelectorWheel = false

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
                    month.wrapSelectorWheel = false
                    day.wrapSelectorWheel = false
                    month.maxValue = cMonth + 1
                    day.maxValue = cDay
                } else {
                    month.wrapSelectorWheel = true
                    day.wrapSelectorWheel = true
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
                    month.wrapSelectorWheel = false
                    day.wrapSelectorWheel = false
                    month.maxValue = cMonth + 1
                    day.maxValue = cDay
                } else {
                    month.wrapSelectorWheel = true
                    day.wrapSelectorWheel = true
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
            if (btn_birth.text.toString() != "생년월일 입력")
            {
                val valueTemp = btn_birth.text.split("년 ", "월 ", "일")

//                Log.d("dayprint", "전체 ${btn_birth.text}\n")
//                Log.d("dayprint", "년: ${year.value}\n")
//                Log.d("dayprint", "월: ${month.value}\n")
//                Log.d("dayprint", "일: ${day.value}\n")

                yearCondition()
                monthCondition()
                dayCondition()

                year.value = valueTemp[0].toInt()
                month.value = valueTemp[1].toInt()
                day.value = valueTemp[2].toInt()

            } else {
                year.value = cYear
                month.value = 1
                day.value = 1

                yearCondition()
                monthCondition()
                dayCondition()
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
                if (year.hasFocus()) year.clearFocus()
                if (month.hasFocus()) month.clearFocus()
                if (day.hasFocus()) day.clearFocus()

                birthday = "${year.value}-${month.value}-${day.value}"

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
            when (i) {
                R.id.rb_man -> gender = "man"
                R.id.rb_woman -> gender = "woman"
                R.id.rb_etc -> gender = "etc"
            }
        }

        // 약관동의 시
        cb_all.setOnClickListener {
            onCheckChanged(cb_all)
            btn_signUp.isEnabled = cb_all.isChecked && emailCheck && nickNameCheck
//            Toast.makeText(this, "email:$emailCheck \npwd:$pwdCheck \n" +
//                    "pwdConfirm:$pwdConfirmCheck \nnickName:$nickNameCheck \nbirth:$birthCheck \nprivacy:${cb_privacy.isChecked}\nuse:${cb_use.isChecked}", Toast.LENGTH_SHORT).show()
        }
        cb_privacy.setOnClickListener {
            onCheckChanged(cb_privacy)
            btn_signUp.isEnabled = cb_privacy.isChecked && cb_use.isChecked && emailCheck && nickNameCheck
        }
        cb_use.setOnClickListener {
            onCheckChanged(cb_use)
            btn_signUp.isEnabled = cb_privacy.isChecked && cb_use.isChecked && emailCheck && nickNameCheck
        }
        cb_pushAlarm.setOnClickListener { onCheckChanged(cb_pushAlarm) }

        // 회원가입 버튼 클릭 시
        btn_signUp.setOnClickListener {
            val signUpService: RetrofitService = Common.retrofit.create(RetrofitService::class.java)

            val email = et_email.text.toString()
            val pwd = "Null"
            val nickName = et_nickName.text.toString()
            val checkAlarm = cb_pushAlarm.isChecked.toString()
            val signUpDate = signUpTime()

            signUpService.requestSignUp(
                    email, kakaoId, googleId, pwd, nickName, birthday, gender,
                    checkAlarm, signUpDate, manager
            ).enqueue(object : Callback<Success> {
                override fun onResponse(call: Call<Success>, response: Response<Success>) {
                    val signUp = response.body()
                    // 회원가입 성공
                    if (signUp?.success == true) {
                        Toast.makeText(this@SignUpActivity, "회원가입 성공", Toast.LENGTH_SHORT).show()
                        finish()
                        // 메인화면으로 전환
                        val intent = Intent(this@SignUpActivity, MainActivity::class.java)
                        intent.putExtra("email", email)
                        startActivity(intent)

                    } else Toast.makeText(this@SignUpActivity, "회원가입 실패", Toast.LENGTH_SHORT).show()
                }

                override fun onFailure(call: Call<Success>, t: Throwable) {
                    Log.e("signUp", t.message.toString())
                    Toast.makeText(this@SignUpActivity, "회원가입 실패", Toast.LENGTH_SHORT).show()
                }

            })
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

     // EditText에서 외부 클릭 시 포커스 해제
    override fun dispatchTouchEvent(event: MotionEvent): Boolean {
        if (event.action == MotionEvent.ACTION_DOWN) {
            val v = currentFocus
            if (v is EditText) {
                val outRect = Rect()
                v.getGlobalVisibleRect(outRect)
                if (!outRect.contains(event.rawX.toInt(), event.rawY.toInt())) {
                    v.clearFocus()
                    val imm: InputMethodManager =
                        getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
                    imm.hideSoftInputFromWindow(v.getWindowToken(), 0)
                }
            }
        }
        return super.dispatchTouchEvent(event)
    }

    // EditText 하단 도움말
    fun addHelper(
            condition: Boolean,
            layout: LinearLayout,
            target: EditText,
            inputText: String?,
            color: Int = R.color.red): Boolean {
        if (condition) {
            layout.removeAllViews()
            val helper = TextView(this@SignUpActivity)
            helper.text = " $inputText"
            helper.setTextColor(ContextCompat.getColor(this@SignUpActivity, color))
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
        val checkLength = TextView(this@SignUpActivity)
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
            btn_signUp.isEnabled = emailCheck && nickNameCheck && cb_privacy.isChecked && cb_use.isChecked
        }
    }

    // 이메일 중복 체크
    private fun checkEmail(
            condition: Boolean,
            layout: LinearLayout,
            target: EditText,
            inputText1: String?,
            inputText2: String?
    ) {
        val checkEmailService: RetrofitService = Common.retrofit.create(RetrofitService::class.java)

        checkEmailService.requestCheckEmail(target.text.toString()).enqueue(object :
                Callback<Success> {
            override fun onResponse(call: Call<Success>, response: Response<Success>) {
                if (response.body()?.success == true)
                    emailCheck = addHelper(
                            response.body()?.success == condition,
                            layout,
                            target,
                            inputText1
                    )
                else addHelper(
                        response.body()?.success == !condition,
                        layout,
                        target,
                        inputText2,
                        R.color.green
                )
            }

            override fun onFailure(call: Call<Success>, t: Throwable) {
                Log.e("checkEmail", t.message.toString())
                Toast.makeText(this@SignUpActivity, "중복확인 실패", Toast.LENGTH_SHORT).show()
            }

        })
    }

    // 닉네임 중복 체크
    private fun checkNickName(
            condition: Boolean,
            layout: LinearLayout,
            target: EditText,
            inputText1: String?,
            inputText2: String?
    ) {
        val checkNicknameService: RetrofitService = Common.retrofit.create(RetrofitService::class.java)

        checkNicknameService.requestCheckNickName(target.text.toString()).enqueue(object :
                Callback<Success> {
            override fun onResponse(call: Call<Success>, response: Response<Success>) {
                if (response.body()?.success == true)
                    nickNameCheck = addHelper(
                            response.body()?.success == condition,
                            layout,
                            target,
                            inputText1
                    )
                else addHelper(
                        response.body()?.success == !condition,
                        layout,
                        target,
                        inputText2,
                        R.color.green
                )
            }

            override fun onFailure(call: Call<Success>, t: Throwable) {
                Log.e("checkNickName", t.message.toString())
                Toast.makeText(this@SignUpActivity, "중복확인 실패", Toast.LENGTH_SHORT).show()
            }

        })
    }

    // 가입일자
    private fun signUpTime() : String{
        val tz : TimeZone
        var dateFormat : DateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.KOREAN)
        tz = TimeZone.getTimeZone("Asia/Seoul")
        dateFormat.timeZone = tz

        val date = Date()
        return dateFormat.format(date)
    }
}

