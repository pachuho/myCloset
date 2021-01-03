package com.example.mycloset.viewpager

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.mycloset.R
import kotlinx.android.synthetic.main.layout_image_pager_item.view.*


class PagerViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

    private val itemImage = itemView.pager_item_image

    fun bindWithView(pageItem: String){
        Glide.with(itemView.context).load(pageItem)
            .override(500, 500)
            .thumbnail(0.1f)
            .error(R.drawable.mongsil_7)
            .into(itemImage)
    }

}