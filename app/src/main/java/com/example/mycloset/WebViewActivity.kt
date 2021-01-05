package com.example.mycloset

import android.app.Dialog
import android.content.Intent
import android.graphics.Bitmap
import android.net.http.SslError
import android.os.Bundle
import android.os.Message
import android.view.ViewGroup
import android.webkit.SslErrorHandler
import android.webkit.WebChromeClient
import android.webkit.WebView
import android.webkit.WebViewClient
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_web_view.*


class WebViewActivity : AppCompatActivity() {
    var lastBackPressedTime: Long = 0


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_web_view)
        val loadingDialog : Dialog = LoadingDialog(this)



        webView_product.webViewClient = object : WebViewClient() {
            override fun onReceivedSslError(view: WebView, handler: SslErrorHandler, error: SslError) {
                handler.proceed()
            }

            override fun onPageStarted(view: WebView?, url: String?, favicon: Bitmap?) {
                super.onPageStarted(view, url, favicon)
                loadingDialog.show()
            }

            override fun onPageFinished(view: WebView?, url: String?) {
                super.onPageFinished(view, url)
                loadingDialog.dismiss()
            }
        }


        if (intent.hasExtra("link")) {
            intent.getStringExtra("link")?.let { webView_product.loadUrl(it) }
        } else {
            val intent = Intent(this@WebViewActivity, ErrorActivity::class.java)
            startActivity(intent)
        }

    }



    // 뒤로가기
    override fun onBackPressed() {

        if (webView_product.canGoBack()) {
            webView_product.goBack()
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
}