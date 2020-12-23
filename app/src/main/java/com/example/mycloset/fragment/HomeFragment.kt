package com.example.mycloset.fragment

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.viewpager2.widget.ViewPager2
import com.example.mycloset.R
import com.example.mycloset.viewpager.ImageRecyclerAdapter
import com.example.mycloset.viewpager.PageItem
import kotlinx.android.synthetic.main.fragment_home.*


class HomeFragment : Fragment(){

    private var pageItemList = ArrayList<PageItem>()
    private lateinit var imageRecyclerAdapter: ImageRecyclerAdapter
    private lateinit var mContext: Context
    override fun onAttach(context: Context) {
        super.onAttach(context)
        mContext = context
    }

    override fun onCreateView(

        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_home, container, false)
        val test = container
        pageItemList.add(PageItem(R.color.blue, R.drawable.ic_pager_item_1, "1"))
        pageItemList.add(PageItem(R.color.green, R.drawable.ic_pager_item_2, "2"))
        pageItemList.add(PageItem(R.color.grey, R.drawable.ic_pager_item_3, "3"))

        imageRecyclerAdapter = ImageRecyclerAdapter(mContext, pageItemList)

        // 뷰페이저에 설정
        image_view_pager.apply {


                try {
                    adapter = imageRecyclerAdapter
                } catch (e: Exception) {
                    Log.e("에러내용", e.toString())
                }
            orientation = ViewPager2.ORIENTATION_HORIZONTAL
//            dots_indicator.setViewPager2(this)
        }

        return view
    }

}