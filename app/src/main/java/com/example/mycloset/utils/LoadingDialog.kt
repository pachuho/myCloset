package com.example.mycloset.utils

import android.app.Dialog
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import com.example.mycloset.R

class LoadingDialog constructor(context: Context) : Dialog(context) {
    init {
        setContentView(R.layout.dialog_loading)
        setCanceledOnTouchOutside(false) // 터치 허용
        setCancelable(false) // 뒤로가기 허용
        window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT)) // 배경화면 투명
    }
}