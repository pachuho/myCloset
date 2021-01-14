package com.example.mycloset.viewpager

import android.annotation.SuppressLint
import android.content.Intent
import android.view.View
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.mycloset.Profile
import com.example.mycloset.R
import com.example.mycloset.WebViewActivity


class PagerViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

    private val itemImage = itemView.pager_item_image
    private val itemBrand = itemView.pager_item_brand
    private val itemName = itemView.pager_item_name
    private val itemPrice = itemView.pager_item_price
    private var favoriteCheck : Boolean = false

    @SuppressLint("SetTextI18n")
    fun bindWithView(pageItem: PageItem){
        Glide.with(itemView.context).load(pageItem.image)
            .override(500, 500)
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

        // 별 클릭 시
        itemView.pager_item_star.setOnClickListener {
            if (favoriteCheck) {
                itemView.pager_item_star.setImageResource(R.drawable.img_star_outline)
                favoriteCheck = false
                Profile.favoriteImage.remove(pageItem)
            } else {
                itemView.pager_item_star.setImageResource(R.drawable.img_star_fill)
                favoriteCheck = true
                Profile.favoriteImage.add(pageItem)
            }
        }
    }
}