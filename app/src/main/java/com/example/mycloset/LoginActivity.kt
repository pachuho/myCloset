package com.example.mycloset

import android.content.Context
import android.content.Intent
import android.graphics.Rect
import android.os.Build
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.KeyEvent
import android.view.KeyEvent.KEYCODE_ENTER
import android.view.MotionEvent
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import com.example.mycloset.Retrofit.*
import com.kakao.sdk.auth.LoginClient
import com.kakao.sdk.auth.model.OAuthToken
import com.kakao.sdk.user.UserApiClient
import kotlinx.android.synthetic.main.activity_sign_in.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class LoginActivity : AppCompatActivity() {
    var lastBackPressedTime: Long = 0
    @RequiresApi(Build.VERSION_CODES.P)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sign_in)

        // textWatcher 지정
        login_et_email.addTextChangedListener(TextWatcher)
        login_et_pwd.addTextChangedListener(TextWatcher)

        // editText에서 완료 클릭 시
        login_et_pwd.setOnKeyListener { v, keyCode, event ->
            if (event.action == KeyEvent.ACTION_DOWN && keyCode == KEYCODE_ENTER) {
                btn_login.performClick()
            }
            true
        }

        // 로그인 버튼
        btn_login.setOnClickListener {
            val signInService: RetrofitService = Common.retrofit.create(RetrofitService::class.java)
            val inputEmail = login_et_email.text.toString()
            val inputPwd = login_et_pwd.text.toString()

            signInService.requestSignIn(inputEmail,inputPwd).enqueue(object: Callback<SignIn> {
                // 통신 성공
                override fun onResponse(call: Call<SignIn>, response: Response<SignIn>) {
                    val signIn = response.body()
                    // 로그인 성공(회원정보 있으면)
                    if (signIn?.success == true) {
                        Toast.makeText(this@LoginActivity, "로그인 성공", Toast.LENGTH_SHORT).show()
                        val intent = Intent(this@LoginActivity, MainActivity::class.java)
                        intent.putExtra("email", inputEmail)
                        startActivity(intent)
                        finish()

                    }
                    // 로그인 실패(회원정보 없으면)
                    else Toast.makeText(this@LoginActivity, "회원 정보를 확인해주세요", Toast.LENGTH_SHORT).show()
                }

                // 통신 실패
                override fun onFailure(call: Call<SignIn>, t: Throwable) {
                    Log.e("signIn", t.message.toString())
                    Toast.makeText(this@LoginActivity, "로그인 실패", Toast.LENGTH_SHORT).show()
                }
            })
        }

        // 회원가입-카카오 버튼
        btn_signUpKakao.setOnClickListener {
            val TAG = "카카오"

             // 로그인 공통 callback 구성
            val callback: (OAuthToken?, Throwable?) -> Unit = { token, error ->
                if (error != null) {
                    Log.e(TAG, "로그인 실패", error)
                }
                else if (token != null) {
                    Log.i(TAG, "로그인 성공 ${token.accessToken}")
                }
            }

            // 카카오톡이 설치되어 있으면 카카오톡으로 로그인, 아니면 카카오계정으로 로그인
            if (LoginClient.instance.isKakaoTalkLoginAvailable(this)) {
                LoginClient.instance.loginWithKakaoTalk(this, callback = callback)
            } else {
                LoginClient.instance.loginWithKakaoAccount(this, callback = callback)
            }

            // 토큰 정보 보기
            UserApiClient.instance.accessTokenInfo { tokenInfo, error ->
                if (error != null) {
                    Log.e(TAG, "토큰 정보 보기 실패", error)
                }
                else if (tokenInfo != null) {
                    Log.i(TAG, "토큰 정보 보기 성공" + "\n회원번호: ${tokenInfo.id}" + "\n만료시간: ${tokenInfo.expiresIn} 초")

                    // 회원 정보 조회
                    val Service: RetrofitService = Common.retrofit.create(RetrofitService::class.java)

                    Service.requestCheckKakaoId(tokenInfo.id.toString()).enqueue(object :
                        Callback<Check> {
                        override fun onResponse(call: Call<Check>, response: Response<Check>) {
                            if (response.body()?.success == true) { // 회원정보가 있다면
                                val intent = Intent(this@LoginActivity, MainActivity::class.java)
                                intent.putExtra("email", response.body()?.email)
                                startActivity(intent)
                            }
                            else { // 회원 정보가 없다면
                                val intent = Intent(this@LoginActivity, SignUpActivity::class.java)
                                intent.putExtra("kakaoId", tokenInfo.id.toString())
                                startActivity(intent)
                            }
                        }

                        override fun onFailure(call: Call<Check>, t: Throwable) {
                            Log.e("checkEmail", t.message.toString())
                            Toast.makeText(this@LoginActivity, "중복확인 실패", Toast.LENGTH_SHORT).show()
                        }

                    })
                }
            }
        }

        // 회원가입-구글 버튼
        btn_signUpGoogle.setOnClickListener {
            val intent = Intent(this, SignUpActivity::class.java)
            intent.putExtra("googleId", "test")
            startActivity(intent)
        }

        // 회원가입-이메일 버튼
        btn_signUpEmail.setOnClickListener {
            val intent = Intent(this, SignUpEmailActivity::class.java)
            startActivity(intent)
            overridePendingTransition(R.anim.slide_from_right, R.anim.slide_to_left);
        }

    }

    // 회원가입 버튼 활성화
    private val TextWatcher: TextWatcher = object : TextWatcher {
        override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {}
        override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {}
        override fun afterTextChanged(s: Editable) {
            btn_login.isEnabled = !login_et_email.text.isNullOrEmpty() && !login_et_pwd.text.isNullOrEmpty()
        }
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

    // 뒤로가기
    override fun onBackPressed() {
        val currentTime = System.currentTimeMillis()
        val differenceTime = currentTime - lastBackPressedTime
        if (differenceTime in 0..2000) {
            finish()
        } else {
            lastBackPressedTime = currentTime
            Toast.makeText(this, "한번 더 누르시면 종료됩니다.", Toast.LENGTH_SHORT).show()
        }
    }




}

