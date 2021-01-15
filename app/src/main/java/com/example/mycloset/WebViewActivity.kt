package com.example.mycloset

import android.annotation.SuppressLint
import android.content.Intent
import android.graphics.Bitmap
import android.net.http.SslError
import android.os.Bundle
import android.os.Message
import android.view.View
import android.view.ViewGroup
import android.webkit.*
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.mycloset.databinding.ActivityWebViewBinding


class WebViewActivity : AppCompatActivity() {
    private var mBinding: ActivityWebViewBinding? = null
    private val binding get() = mBinding!!

    var lastBackPressedTime: Long = 0
    lateinit var loadingDialog: LoadingDialog

    lateinit var webViewProduct : WebView

    @SuppressLint("SetJavaScriptEnabled")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mBinding = ActivityWebViewBinding.inflate(layoutInflater)
        setContentView(binding.root)

        loadingDialog = LoadingDialog(this)

        webViewProduct = binding.webViewProduct
        webViewProduct.apply {
            webViewClient = WebViewClientClass() // 클릭 시 새창 안뜨도록
            webChromeClient = object : WebChromeClient() {
                override fun onCreateWindow(view: WebView?, isDialog: Boolean, isUserGesture: Boolean, resultMsg: Message?): Boolean {
                    val newWebView = WebView(this@WebViewActivity).apply {
                        webViewClient = WebViewClient()
                        settings.javaScriptEnabled = true
                    }

                    loadingDialog.apply {
                        setContentView(newWebView)
                        window!!.attributes.width = ViewGroup.LayoutParams.MATCH_PARENT
                        window!!.attributes.height = ViewGroup.LayoutParams.MATCH_PARENT
                        show()
                    }

                    newWebView.webChromeClient = object : WebChromeClient() {
                        override fun onCloseWindow(window: WebView?) {
                            loadingDialog.dismiss()
                        }
                    }

                    (resultMsg?.obj as WebView.WebViewTransport).webView = newWebView
                    resultMsg.sendToTarget()
                    return true
                }
            }

            settings.javaScriptEnabled = true
            settings.setSupportMultipleWindows(true) // 새창 띄우기 허용여부
            settings.javaScriptCanOpenWindowsAutomatically = true // 자바스크립트 새창 띄우기(멀티뷰) 허용여부
           settings.loadWithOverviewMode = true // 메타태그 허용여부
            settings.useWideViewPort = true // 화면 사이즈 맞추기 허용여부
            settings.setSupportZoom(false) // 화면 줌 허용여부

            // Enable and setUp webView cache
            settings.cacheMode = WebSettings.LOAD_NO_CACHE // 브라우저 캐시 허용여부
            settings.domStorageEnabled = true // 로컬저장소 허용여부
            settings.displayZoomControls = true

            if (intent.hasExtra("link")) {
                intent.getStringExtra("link")?.let { webViewProduct.loadUrl(it) }
            } else {
                val intent = Intent(this@WebViewActivity, ErrorActivity::class.java)
                startActivity(intent)
            }

        }
    }

    inner class WebViewClientClass : WebViewClient(){
        override fun shouldOverrideUrlLoading(view: WebView?, url: String?): Boolean {
            if (view != null && url != null) {
                view.loadUrl(url)
            }
            return true
        }

        override fun onPageStarted(view: WebView?, url: String?, favicon: Bitmap?) {
            super.onPageStarted(view, url, favicon)
            loadingDialog.show()
            webViewProduct.visibility = View.INVISIBLE
        }

        override fun onPageCommitVisible(view: WebView?, url: String?) {
            super.onPageCommitVisible(view, url)
            loadingDialog.dismiss()
            webViewProduct.visibility = View.VISIBLE
        }

        override fun onReceivedSslError(view: WebView?, handler: SslErrorHandler?, error: SslError?) {
            super.onReceivedSslError(view, handler, error)
            val builder: android.app.AlertDialog.Builder = android.app.AlertDialog.Builder(this@WebViewActivity)
            var message = "SSL Certificate error"
            when (error?.primaryError) {
                SslError.SSL_UNTRUSTED -> message = "신뢰할 수 없는 사이트입니다."
                SslError.SSL_EXPIRED -> message = "만료된 사이트입니다."
                SslError.SSL_IDMISMATCH -> message = "도메인이 없습니다."
                SslError.SSL_NOTYETVALID -> message = "검증되지 않은 사이트입니다."
            }
            message += "페이지로 이동 하시겠습니까?"
            builder.setTitle("SSL Certificate Error")
            builder.setMessage(message)
            builder.setPositiveButton(R.string.yes) { _, _ -> handler?.proceed() }
            builder.setNegativeButton(R.string.no
            ) { _, _ -> handler?.cancel() }
            val dialog: android.app.AlertDialog = builder.create()
            dialog.show()
        }
    }



    // 뒤로가기
    override fun onBackPressed() {
        if (webViewProduct.canGoBack()) {
            webViewProduct.goBack()
        } else {
            val currentTime = System.currentTimeMillis()
            val differenceTime = currentTime - lastBackPressedTime

            if (differenceTime in 0..2000) {
                finish()
            } else {
                lastBackPressedTime = currentTime
                Toast.makeText(this, getString(R.string.one_more_touch_to_app), Toast.LENGTH_SHORT).show()
            }
        }
    }

    override fun onDestroy() {
        mBinding = null
        super.onDestroy()
    }
}