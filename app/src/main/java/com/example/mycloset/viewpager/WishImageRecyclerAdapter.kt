package com.example.mycloset.viewpager

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.mycloset.R

class WishImageRecyclerAdapter(private var pageList: ArrayList<PageItem>) : RecyclerView.Adapter<PagerViewHolderWish>(){


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PagerViewHolderWish {
        return PagerViewHolderWish(LayoutInflater.from(parent.context).inflate(R.layout.layout_wish_image_item, parent, false))
    }

    override fun getItemCount(): Int {
        return pageList.size
    }

    override fun onBindViewHolder(holder: PagerViewHolderWish, position: Int) {
        holder.bindWithView(pageList[position])
    }
}
