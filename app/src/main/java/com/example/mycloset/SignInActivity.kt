package com.example.mycloset

import android.app.Dialog
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
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import com.example.mycloset.databinding.ActivitySignInBinding
import com.example.mycloset.retrofit.*
import com.example.mycloset.utils.App
import com.example.mycloset.utils.LoadingDialog
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.kakao.sdk.auth.LoginClient
import com.kakao.sdk.auth.model.OAuthToken
import com.kakao.sdk.user.UserApiClient
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class SignInActivity : AppCompatActivity() {
    private var mBinding: ActivitySignInBinding? = null
    private val binding get() = mBinding!!

    var lastBackPressedTime: Long = 0

    private lateinit var loadingDialog: Dialog
    private lateinit var auth: FirebaseAuth
    private lateinit var googleSignInClient: GoogleSignInClient
    private val RC_SIGN_IN = 9001

    @RequiresApi(Build.VERSION_CODES.P)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        mBinding = ActivitySignInBinding.inflate(layoutInflater)
        setContentView(binding.root)

        loadingDialog = LoadingDialog(this)

        // textWatcher 지정
        binding.loginEtEmail.addTextChangedListener(textWatcher)
        binding.loginEtPwd.addTextChangedListener(textWatcher)

        // editText에서 완료 클릭 시
        binding.loginEtPwd.setOnEditorActionListener { v, actionId, event ->
            var handled = false
            if (actionId == EditorInfo.IME_ACTION_DONE) {
                binding.btnLogin.performClick()
                handled = true
            }
            handled
        }

        // 로그인 버튼
        binding.btnLogin.setOnClickListener {
            loadingDialog.show()
            val signInService: RetrofitService = Common.retrofit.create(RetrofitService::class.java)
            val inputEmail = binding.loginEtEmail.text.toString()
            val inputPwd = binding.loginEtPwd.text.toString()

            signInService.requestSignIn(inputEmail, inputPwd).enqueue(object : Callback<SignIn> {
                // 통신 성공
                override fun onResponse(call: Call<SignIn>, response: Response<SignIn>) {
                    val signIn = response.body()
                    // 로그인 성공(회원정보 있으면)
                    if (signIn?.success == true) {
                        App.prefs.userEmail = signIn.email
//                        Toast.makeText(this@SignInActivity, getString(R.string.login_success), Toast.LENGTH_SHORT).show()
                        val intent = Intent(this@SignInActivity, MainActivity::class.java)
                        startActivity(intent)
                        finish()
                    } else {
                        // 로그인 실패(회원정보 없으면)
                        Toast.makeText(this@SignInActivity, getString(R.string.confirm_your_info), Toast.LENGTH_SHORT).show()
                        loadingDialog.dismiss()
                    }
                }

                // 통신 실패
                override fun onFailure(call: Call<SignIn>, t: Throwable) {
                    Log.e("signIn", t.message.toString())
                    Toast.makeText(this@SignInActivity, getString(R.string.confirm_network), Toast.LENGTH_SHORT).show()
                    loadingDialog.dismiss()
                }
            })
        }

        // 카카오 계정으로 시작하기
        binding.btnSignUpKakao.setOnClickListener {
            loadingDialog.show()
            val TAG = "카카오"

             // 로그인 공통 callback 구성
            val callback: (OAuthToken?, Throwable?) -> Unit = { token, error ->
                if (error != null) {
                    loadingDialog.dismiss()
                    Log.e(TAG, getString(R.string.login_failure), error)
                }
                else if (token != null) {
                    Log.i(TAG, getString(R.string.login_success) + token.accessToken)

                    // 토큰 정보 보기
                    UserApiClient.instance.accessTokenInfo { tokenInfo, error ->
                        if (error != null) {
                            loadingDialog.dismiss()
                            Log.e(TAG, getString(R.string.token_failure), error)

                        }
                        else if (tokenInfo != null) {
                            Log.i(TAG, getString(R.string.token_success) + "\n" + getString(R.string.member_number) + tokenInfo.id)

                            // 회원 정보 조회
                            val Service: RetrofitService = Common.retrofit.create(RetrofitService::class.java)

                            Service.requestCheckKakaoId(tokenInfo.id.toString()).enqueue(object :
                                Callback<Check> {
                                override fun onResponse(call: Call<Check>, response: Response<Check>) {
                                    if (response.body()?.success == true) { // 회원정보가 있다면
                                        App.prefs.userEmail = response.body()?.email
                                        val intent = Intent(this@SignInActivity, MainActivity::class.java)
                                        startActivity(intent)
                                        finish()
                                    } else { // 회원 정보가 없다면
                                        val intent = Intent(this@SignInActivity, SignUpActivity::class.java)
                                        intent.putExtra("kakaoId", tokenInfo.id.toString())
                                        startActivity(intent)
                                    }
                                }

                                override fun onFailure(call: Call<Check>, t: Throwable) {
                                    Log.e("checkEmail", t.message.toString())
                                    Toast.makeText(this@SignInActivity, getString(R.string.confirm_overlap_failure), Toast.LENGTH_SHORT).show()
                                    loadingDialog.dismiss()
                                }

                            })
                        }
                    }
                }
            }
            // 카카오톡이 설치되어 있으면 카카오톡으로 로그인, 아니면 카카오계정으로 로그인
            if (LoginClient.instance.isKakaoTalkLoginAvailable(this)) {
                LoginClient.instance.loginWithKakaoTalk(this, callback = callback)
            } else {
                LoginClient.instance.loginWithKakaoAccount(this, callback = callback)
            }
        }

        // 구글 계정으로 시작하기
        binding.btnSignUpGoogle.setOnClickListener {
            loadingDialog.show()

            val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
//                    .requestIdToken(getString(R.string.default_web_client_id))
                    .requestIdToken("59867261037-1502i8vbf2phqmscn6vi4qs3tr1h9kfl.apps.googleusercontent.com")
                    .requestEmail()
                    .build()

            googleSignInClient = GoogleSignIn.getClient(this, gso)
            auth = Firebase.auth

            // Configure Google Sign In
            val signInIntent = googleSignInClient.signInIntent
            startActivityForResult(signInIntent, RC_SIGN_IN)
        }

        // 회원가입-이메일 버튼
        binding.btnSignUpEmail.setOnClickListener {
            val intent = Intent(this, SignUpActivity::class.java)
            startActivity(intent)
            overridePendingTransition(R.anim.slide_from_right, R.anim.slide_to_left)
        }

        // 회원정보 분실
        binding.tvForgot.setOnClickListener {
            Toast.makeText(this, getString(R.string.will_do_update), Toast.LENGTH_SHORT).show()

//            // 연결 끊기
//            UserApiClient.instance.unlink { error ->
//                if (error != null) {
//                    Log.e("test", "연결 끊기 실패", error)
//                }
//                else {
//                    Log.i("test", "연결 끊기 성공. SDK에서 토큰 삭제 됨")
//                }
//            }
        }

    }

    // 회원가입 버튼 활성화
    private val textWatcher: TextWatcher = object : TextWatcher {
        override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {}
        override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {}
        override fun afterTextChanged(s: Editable) {
            binding.btnLogin.isEnabled = !binding.loginEtEmail.text.isNullOrEmpty() && !binding.loginEtPwd.text.isNullOrEmpty()
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

    override fun onStop() {
        super.onStop()
        loadingDialog.dismiss()
    }

    // 뒤로가기
    override fun onBackPressed() {
        val currentTime = System.currentTimeMillis()
        val differenceTime = currentTime - lastBackPressedTime
        if (differenceTime in 0..2000) {
            finish()
        } else {
            lastBackPressedTime = currentTime
            Toast.makeText(this, getString(R.string.one_more_touch_end), Toast.LENGTH_SHORT).show()
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        val TAG = getString(R.string.get_id)

        // Result returned from launching the Intent from GoogleSignInApi.getSignInIntent(...);
        if (requestCode == RC_SIGN_IN) {
            val task = GoogleSignIn.getSignedInAccountFromIntent(data)
            try {
                // Google Sign In was successful, authenticate with Firebase
                val account = task.getResult(ApiException::class.java)!!
                Log.d(TAG, getString(R.string.auth_google) + account.id)
                firebaseAuthWithGoogle(account.idToken!!, account.id!!)
            } catch (e: ApiException) {
                // Google Sign In failed, update UI appropriately
                Log.w(TAG, getString(R.string.google_sign_failure), e)
                loadingDialog.dismiss()
            }
        }
    }


    private fun firebaseAuthWithGoogle(idToken: String, googleId: String) {
        val TAG = getString(R.string.auth_google)
        val credential = GoogleAuthProvider.getCredential(idToken, null)
        auth.signInWithCredential(credential)
                .addOnCompleteListener(this) { task ->
                    if (task.isSuccessful) {
                        // Sign in success, update UI with the signed-in user's information
                        Log.d(TAG, getString(R.string.login_success) + googleId)

                        // 회원 정보 조회
                        val Service: RetrofitService = Common.retrofit.create(RetrofitService::class.java)

                        Service.requestCheckGoogleId(googleId).enqueue(object :
                                Callback<Check> {
                            override fun onResponse(call: Call<Check>, response: Response<Check>) {
                                if (response.body()?.success == true) { // 회원정보가 있다면
                                    App.prefs.userEmail = response.body()?.email
                                    val intent = Intent(this@SignInActivity, MainActivity::class.java)
                                    startActivity(intent)
                                    finish()

                                } else { // 회원 정보가 없다면
                                    val intent = Intent(this@SignInActivity, SignUpActivity::class.java)
                                    intent.putExtra("googleId", googleId)
                                    startActivity(intent)
                                }
                            }

                            override fun onFailure(call: Call<Check>, t: Throwable) {
                                Log.e("checkEmail", t.message.toString())
                                Toast.makeText(this@SignInActivity, getString(R.string.confirm_overlap_failure), Toast.LENGTH_SHORT).show()
                                loadingDialog.dismiss()
                            }

                        })
                    } else {
                        // If sign in fails, display a message to the user.
                        Log.w(TAG, "signInWithCredential:failure", task.exception)
                        Toast.makeText(this@SignInActivity, getString(R.string.confirm_network), Toast.LENGTH_SHORT).show()
                        loadingDialog.dismiss()
                    }
                }
    }

    override fun onDestroy() {
        mBinding = null
        super.onDestroy()
    }
}

