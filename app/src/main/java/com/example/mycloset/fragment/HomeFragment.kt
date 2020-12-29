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
import kotlinx.android.synthetic.main.fragment_home.view.*
import java.lang.Exception


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

        pageItemList.add(PageItem(imageSrc = R.drawable.mongsil_1))
        pageItemList.add(PageItem(imageSrc = R.drawable.mongsil_2))
        pageItemList.add(PageItem(imageSrc = R.drawable.mongsil_3))
        pageItemList.add(PageItem(imageSrc = R.drawable.mongsil_4))
        pageItemList.add(PageItem(imageSrc = R.drawable.mongsil_5))
        pageItemList.add(PageItem(imageSrc = R.drawable.mongsil_6))
        pageItemList.add(PageItem(imageSrc = R.drawable.mongsil_7))

        imageRecyclerAdapter = ImageRecyclerAdapter(pageItemList)

        // 뷰페이저에 설정
        view.image_view_pager.apply {
            adapter = imageRecyclerAdapter
            orientation = ViewPager2.ORIENTATION_HORIZONTAL
        }

        return view
    }

}