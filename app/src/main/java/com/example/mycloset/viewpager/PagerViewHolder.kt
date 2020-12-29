package com.example.mycloset.viewpager

import android.graphics.Color
import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.example.mycloset.R
import kotlinx.android.synthetic.main.layout_image_pager_item.view.*


class PagerViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

    private val itemImage = itemView.pager_item_image

    fun bindWithView(pageItem: PageItem){
        itemImage.setImageResource(pageItem.imageSrc)


    }

}
