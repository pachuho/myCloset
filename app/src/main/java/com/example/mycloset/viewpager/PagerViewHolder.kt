package com.example.mycloset.viewpager

import android.util.Log
import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.mycloset.R
import kotlinx.android.synthetic.main.layout_image_pager_item.view.*
import java.text.NumberFormat
import java.util.*


class PagerViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

    private val itemImage = itemView.pager_item_image
    private val itemBrand = itemView.pager_item_brand
    private val itemName = itemView.pager_item_name
    private val itemPrice = itemView.pager_item_price

    fun bindWithView(pageItem: PageItem){
        Glide.with(itemView.context).load(pageItem.imageLink)
            .override(500, 500)
            .thumbnail(0.1f)
            .error(R.drawable.mongsil_7)
            .into(itemImage)

        itemBrand.text = pageItem.imageBrand
        itemName.text = pageItem.imageName
        itemPrice.text = "₩" + pageItem.imagePrice.toString()
    }

}