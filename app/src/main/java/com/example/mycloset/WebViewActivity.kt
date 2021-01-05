package com.example.mycloset

import android.content.Intent
import android.os.Bundle
import android.view.KeyEvent
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_web_view.*

class WebViewActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_web_view)

        if (intent.hasExtra("link")) {
            intent.getStringExtra("link")?.let { webView_main.loadUrl(it) }
        } else {
            val intent = Intent(this, ErrorActivity::class.java)
            startActivity(intent)
        }

    }



    override fun onKeyDown(keyCode: Int, event: KeyEvent?): Boolean {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            finish()
        }
        return false
    }
}