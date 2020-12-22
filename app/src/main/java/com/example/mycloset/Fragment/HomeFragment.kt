package com.example.mycloset.Fragment

import android.content.Context
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.viewpager2.widget.ViewPager2
import com.example.mycloset.R
import com.jeongdaeri.introslideproject.MyIntroPagerRecyclerAdapter
import com.jeongdaeri.introslideproject.PageItem
import kotlinx.android.synthetic.main.fragment_home.*

class HomeFragment : Fragment(){
    companion object {
        const val TAG: String = "로그"
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        val mContext = context
    }

    private var pageItemList = ArrayList<PageItem>()
    private lateinit var myIntroPagerRecyclerAdapter: MyIntroPagerRecyclerAdapter

    override fun onCreateView(

        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_home, container, false)

//        previous_btn.setOnClickListener {
//            Log.d(TAG, "MainActivity - 이전 버튼 클릭")
//
//            my_intro_view_pager.currentItem = my_intro_view_pager.currentItem - 1
//        }
//
//        next_btn.setOnClickListener {
//            Log.d(TAG, "MainActivity - 다음 버튼 클릭")
//            my_intro_view_pager.currentItem = my_intro_view_pager.currentItem + 1
//        }
//
//        pageItemList.add(PageItem(R.color.blue, R.drawable.ic_pager_item_1, "1"))
//        pageItemList.add(PageItem(R.color.green, R.drawable.ic_pager_item_2, "2"))
//        pageItemList.add(PageItem(R.color.grey, R.drawable.ic_pager_item_3, "3"))
//
//        myIntroPagerRecyclerAdapter = MyIntroPagerRecyclerAdapter(pageItemList)
//
//        if (Build.VERSION.SDK_INT < 16) {
//
//            activity?.window?.setFlags(
//                WindowManager.LayoutParams.FLAG_FULLSCREEN,
//                WindowManager.LayoutParams.FLAG_FULLSCREEN)
//        }
//
//        // Hide the status bar.
//        activity?.window?.decorView?.systemUiVisibility = View.SYSTEM_UI_FLAG_FULLSCREEN
//        // Remember that you should never show the action bar if the
//        // status bar is hidden, so hide that too if necessary.
//        activity?.actionBar?.hide()

        // 뷰페이저에 설정
        my_intro_view_pager.apply {

            adapter = myIntroPagerRecyclerAdapter
            orientation = ViewPager2.ORIENTATION_HORIZONTAL

//            this.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
//
//                override fun onPageSelected(position: Int) {
//                    super.onPageSelected(position)
////                    supportActionBar?.setBackgroundDrawable(ColorDrawable(resources.getColor(R.color.colorBlue)))
//                }
//
//            })

//            dots_indicator.setViewPager2(this)
        }




        return view
    }


}