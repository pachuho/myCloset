package com.example.mycloset

import android.app.Dialog
import android.content.Context
import android.view.Window
import android.widget.Button
import android.widget.DatePicker
import android.widget.TextView

class BirthdayDialog(context: Context) {
    private val dialog = Dialog(context)   //부모 액티비티의 context 가 들어감
    private lateinit var datePicker: DatePicker
    private lateinit var tvOk : TextView
    private lateinit var tvCancel : TextView
    private lateinit var listener : BirthdayDialogOKClickedListener

//    fun start(content : String) {
    fun start() {
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)   //타이틀바 제거
        dialog.setContentView(R.layout.birthday_dialog)     //다이얼로그에 사용할 xml 파일을 불러옴

        tvOk = dialog.findViewById(R.id.tv_ok)
        tvOk.setOnClickListener {

            //TODO: 부모 액티비티로 내용을 돌려주기 위해 작성할 코드
            listener.onOKClicked("확인을 눌렀습니다")
            dialog.dismiss()
        }

        tvCancel = dialog.findViewById(R.id.tv_cancel)
        tvCancel.setOnClickListener {
            dialog.dismiss()
        }

        dialog.show()
    }

    fun setOnOKClickedListener(listener: (String) -> Unit) {
        this.listener = object: BirthdayDialogOKClickedListener {
            override fun onOKClicked(content: String) {
                listener(content)
            }
        }
    }


    interface BirthdayDialogOKClickedListener {
        fun onOKClicked(content : String)
    }

}