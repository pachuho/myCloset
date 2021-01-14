package com.example.mycloset.viewpager

import android.annotation.SuppressLint
import android.content.Intent
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.mycloset.R
import com.example.mycloset.WebViewActivity


class PagerViewHolderWish(itemView: View) : RecyclerView.ViewHolder(itemView) {

    private val itemImage = itemView.findViewById<ImageView>(R.id.wish_item_image)
    private val itemBrand = itemView.findViewById<TextView>(R.id.wish_item_brand)
    private val itemName = itemView.findViewById<TextView>(R.id.wish_item_name)
    private val itemPrice = itemView.findViewById<TextView>(R.id.wish_item_price)
    private val itemClose = itemView.findViewById<ImageView>(R.id.wish_item_close)



    fun bindWithView(pageItem: PageItem){
        Glide.with(itemView.context).load(pageItem.image)
            .override(400, 400)
            .thumbnail(0.1f)
            .error(R.drawable.img_error)
            .into(itemImage)

        itemBrand.text = pageItem.imageBrand
        itemName.text = pageItem.imageName
        itemPrice.text = "₩" + pageItem.imagePrice.toString()

        // 아이템 클릭 시
        itemView.setOnClickListener {
            val intent = Intent(itemView.context, WebViewActivity::class.java)
            intent.putExtra("link", pageItem.imageLink)
            itemView.context.startActivity(intent)

//            Toast.makeText(itemView.context, pageItem.imageLink, Toast.LENGTH_SHORT).show()
        }

        // 닫기 클릭 시
        itemClose.setOnClickListener {
            Toast.makeText(itemView.context, "닫기", Toast.LENGTH_SHORT).show()
        }
    }


}