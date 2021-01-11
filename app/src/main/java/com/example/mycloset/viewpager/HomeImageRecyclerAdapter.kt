package com.example.mycloset.viewpager

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.mycloset.R

class HomeImageRecyclerAdapter(private var pageList: ArrayList<PageItem>) : RecyclerView.Adapter<PagerViewHolder>(){


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PagerViewHolder {
        return PagerViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.layout_home_image_item, parent, false))
    }

    override fun getItemCount(): Int {
        return pageList.size
    }

    override fun onBindViewHolder(holder: PagerViewHolder, position: Int) {
        holder.bindWithView(pageList[position])
    }
}
