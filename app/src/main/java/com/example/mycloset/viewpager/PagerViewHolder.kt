package com.example.mycloset.viewpager

import android.annotation.SuppressLint
import android.content.Intent
import android.view.View
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.mycloset.R
import com.example.mycloset.SignUpActivity
import com.example.mycloset.WebViewActivity
import kotlinx.android.synthetic.main.layout_image_pager_item.view.*


class PagerViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

    private val itemImage = itemView.pager_item_image
    private val itemBrand = itemView.pager_item_brand
    private val itemName = itemView.pager_item_name
    private val itemPrice = itemView.pager_item_price


    @SuppressLint("SetTextI18n")
    fun bindWithView(pageItem: PageItem){
        Glide.with(itemView.context).load(pageItem.image)
            .override(500, 500)
            .thumbnail(0.1f)
            .error(R.drawable.mongsil_7)
            .into(itemImage)

        itemBrand.text = pageItem.imageBrand
        itemName.text = pageItem.imageName
        itemPrice.text = "₩" + pageItem.imagePrice.toString()

        itemView.setOnClickListener {
            val intent = Intent(itemView.context, WebViewActivity::class.java)
            intent.putExtra("link", pageItem.imageLink)
            itemView.context.startActivity(intent)

//            Toast.makeText(itemView.context, pageItem.imageLink, Toast.LENGTH_SHORT).show()
        }
    }

}