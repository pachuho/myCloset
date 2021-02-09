package com.hochupa.mycloset.utils

import android.app.Dialog
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import com.hochupa.mycloset.R

class LoadingDialog constructor(context: Context) : Dialog(context) {
    init {
        setContentView(R.layout.dialog_loading)
        setCanceledOnTouchOutside(false) // 외부 터치 시 취소 허용
        setCancelable(false) // 뒤로가기 허용
        window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT)) // 배경화면 투명
    }
}