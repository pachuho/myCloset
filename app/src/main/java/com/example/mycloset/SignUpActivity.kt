package com.hochupa.mycloset

import android.app.DatePickerDialog
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
import com.hochupa.mycloset.databinding.ActivitySignUpBinding
import com.hochupa.mycloset.retrofit.*
import com.hochupa.mycloset.utils.App
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.text.DateFormat
import java.text.SimpleDateFormat
import java.util.*


class SignUpActivity : AppCompatActivity() {
    private var mBinding: ActivitySignUpBinding? = null
    private val binding get() = mBinding!!

    // 정규식
    val symbolNickname: String = "[0-9|a-z|A-Z|ㄱ-ㅎ|ㅏ-ㅣ|가-힝| ]*"

    // 입력 여부 확인
    var kakaoCheck = false
    var googleCheck = false
    var emailCheck = false
    var pwdCheck = false
    var pwdConfirmCheck = false
    var nickNameCheck = false

    // 기타 변수 선언
    var gender: String = "man"
    var birthday: String = "0000-00-00"
    var manager: String = "N"

    // 카카오, 구글 로그인
    var kakaoId = "Null"
    var googleId = "Null"

    // 바인딩 받아오기
    lateinit var rg_gender : RadioGroup
    lateinit var cb_all : CheckBox
    lateinit var cb_privacy : CheckBox
    lateinit var cb_pushAlarm : CheckBox
    lateinit var cb_use : CheckBox

    lateinit var btn_birth : Button
    lateinit var btn_signUp : Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mBinding = ActivitySignUpBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // 툴바 뒤로가기
        setSupportActionBar(binding.signUpToolbar)
        val ab = supportActionBar!!
        ab.setDisplayShowTitleEnabled(false)
        ab.setDisplayHomeAsUpEnabled(true)

        // 바인딩 받아오기
        val et_email = binding.etEmail
        val et_pwd = binding.etPwd
        val et_pwdConfirm = binding.etPwdConfirm
        val et_nickName = binding.etNickName

        val ll_email = binding.llEmail
        val ll_nickName = binding.llNickName
        val ll_nickNameLength = binding.llNickNameLength
        val ll_pwd = binding.llPwd
        val ll_pwdConfirm = binding.llPwdConfirm
        val ll_pwdConfirmLength = binding.llPwdConfirmLength
        val ll_pwdLength = binding.llPwdLength

        rg_gender = binding.rgGender
        cb_all = binding.cbAll
        cb_privacy = binding.cbPrivacy
        cb_pushAlarm = binding.cbPushAlarm
        cb_use = binding.cbUse

        btn_birth = binding.btnBirth
        btn_signUp = binding.btnSignUp

        // 아이디 값이 있다면 받아오기
        kakaoCheck = intent.hasExtra("kakaoId")
        googleCheck = intent.hasExtra("googleId")

        if (kakaoCheck or googleCheck) {
            if (kakaoCheck) kakaoId = intent.getStringExtra("kakaoId").toString()
            else if (googleCheck) googleId = intent.getStringExtra("googleId").toString()

            // 비밀번호 레이아웃 제거
            binding.llTotalPwd.removeAllViews()
            // textWatcher 지정
            binding.etEmail.addTextChangedListener(TextWatcher)
            binding.etNickName.addTextChangedListener(TextWatcher)
            binding.btnBirth.addTextChangedListener(TextWatcher)
        } else {
            // textWatcher 지정
            et_email.addTextChangedListener(TextWatcher)
            et_pwd.addTextChangedListener(TextWatcher)
            et_pwdConfirm.addTextChangedListener(TextWatcher)
            et_nickName.addTextChangedListener(TextWatcher)
            btn_birth.addTextChangedListener(TextWatcher)

            // 비밀번호 입력 시
            et_pwd.addTextChangedListener(object : TextWatcher {
                override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}
                override fun afterTextChanged(p0: Editable?) {}
                override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                    addLength(ll_pwdLength, et_pwd, "16")
                    pwdCheck = addHelper(et_pwd.length() in 1..7, ll_pwd, et_pwd, "비밀번호 8~16자를 입력해주세요")

                    // 비밀번호 확인 입력 후 비밀번호 입력창 입력 시
                    pwdCheck = addHelper(
                        et_pwdConfirm.length() > 0 && et_pwd.text.toString() != et_pwdConfirm.text.toString(),
                        ll_pwdConfirm, et_pwd, "비밀번호가 일치하지 않아요"
                    )
                }
            })

            // 비밀번호 확인 입력 시
            et_pwdConfirm.addTextChangedListener(object : TextWatcher {
                override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}
                override fun afterTextChanged(p0: Editable?) {}
                override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                    addLength(ll_pwdConfirmLength, et_pwdConfirm, "16")
                    pwdConfirmCheck = addHelper(
                        et_pwdConfirm.length() > 0 && et_pwd.text.toString() != et_pwdConfirm.text.toString(),
                        ll_pwdConfirm, et_pwdConfirm, "비밀번호가 일치하지 않아요"
                    )
                }
            })
        }

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
            if (!(kakaoCheck or googleCheck)) btn_signUp.isEnabled = cb_all.isChecked && emailCheck && pwdCheck && pwdConfirmCheck && nickNameCheck
            else btn_signUp.isEnabled = cb_all.isChecked && emailCheck && nickNameCheck
        }
        cb_privacy.setOnClickListener {
            onCheckChanged(cb_privacy)
            if (!(kakaoCheck or googleCheck)) btn_signUp.isEnabled = cb_privacy.isChecked && cb_use.isChecked && emailCheck && pwdCheck && pwdConfirmCheck && nickNameCheck
            else btn_signUp.isEnabled = cb_privacy.isChecked && cb_use.isChecked && emailCheck && nickNameCheck
        }
        cb_use.setOnClickListener {
            onCheckChanged(cb_use)
            if (!(kakaoCheck or googleCheck)) btn_signUp.isEnabled = cb_privacy.isChecked && cb_use.isChecked && emailCheck && pwdCheck && pwdConfirmCheck && nickNameCheck
            else btn_signUp.isEnabled = cb_privacy.isChecked && cb_use.isChecked && emailCheck && nickNameCheck
        }
        cb_pushAlarm.setOnClickListener { onCheckChanged(cb_pushAlarm) }

        // 회원가입 버튼 클릭 시
        btn_signUp.setOnClickListener {
            val signUpService: RetrofitService = App.Common.retrofit.create(RetrofitService::class.java)

            val email = et_email.text.toString()
            var pwd = "Null"
            val nickName = et_nickName.text.toString()
            val checkAlarm = cb_pushAlarm.isChecked.toString()
            val signUpDate = signUpTime()
            if (!(kakaoCheck or googleCheck)) pwd = et_pwd.text.toString()

            signUpService.requestSignUp(
                    email, kakaoId, googleId, pwd, nickName, birthday, gender,
                    checkAlarm, signUpDate, manager
            ).enqueue(object : Callback<Success> {
                override fun onResponse(call: Call<Success>, response: Response<Success>) {
                    val signUp = response.body()
                    // 회원가입 성공
                    if (signUp?.success == true) {
                        Toast.makeText(this@SignUpActivity, "회원가입 성공", Toast.LENGTH_SHORT).show()
                        if (!(kakaoCheck or googleCheck)) finish()
                        else {
                            val intent = Intent(this@SignUpActivity, MainActivity::class.java)
                            intent.putExtra("email", email)
                            startActivity(intent)
                            finish()
                        }
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
    private val TextWatcher: TextWatcher = object : TextWatcher {
        override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {}
        override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {}
        override fun afterTextChanged(s: Editable) {
            if (!(kakaoCheck or googleCheck)) btn_signUp.isEnabled = emailCheck && pwdCheck && pwdConfirmCheck && nickNameCheck && cb_privacy.isChecked && cb_use.isChecked
            else btn_signUp.isEnabled = emailCheck && nickNameCheck && cb_privacy.isChecked && cb_use.isChecked
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
        val checkEmailService: RetrofitService = App.Common.retrofit.create(RetrofitService::class.java)

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
        val checkNicknameService: RetrofitService = App.Common.retrofit.create(RetrofitService::class.java)

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

    override fun onDestroy() {
        mBinding = null
        super.onDestroy()
    }
}

