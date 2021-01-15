package com.example.mycloset.viewpager

import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.mycloset.Profile
import com.example.mycloset.R
import com.example.mycloset.WebViewActivity

class ImageRecyclerAdapterHome(private var pageList: ArrayList<PageItem>) : RecyclerView.Adapter<ImageRecyclerAdapterHome.PagerViewHolder>(){

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PagerViewHolder {
        return PagerViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.layout_home_image_item, parent, false))
    }

    override fun getItemCount(): Int = pageList.size

    override fun onBindViewHolder(holderHome: PagerViewHolder, position: Int) {
        val pageItem = pageList[position]

        holderHome.apply {
            holder(pageItem)
        }
    }

    class  PagerViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val itemImage = itemView.findViewById<ImageView>(R.id.pager_item_image)
        private val itemBrand = itemView.findViewById<TextView>(R.id.pager_item_brand)
        private val itemName = itemView.findViewById<TextView>(R.id.pager_item_name)
        private val itemPrice = itemView.findViewById<TextView>(R.id.pager_item_price)
        private val itemStar = itemView.findViewById<ImageView>(R.id.pager_item_star)
        private var favoriteCheck : Boolean = false

        fun holder(pageItem: PageItem) {
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
            itemStar.setOnClickListener {
                if (favoriteCheck) {
                    itemStar.setImageResource(R.drawable.img_star_outline)
                    favoriteCheck = false
                    Toast.makeText(it.context,  Profile.favoriteImage[0].toString(), Toast.LENGTH_SHORT).show()
                    Profile.favoriteImage.removeAt(0)
                } else {
                    itemStar.setImageResource(R.drawable.img_star_fill)
                    favoriteCheck = true
                    Profile.favoriteImage.add(pageItem)
                    Toast.makeText(it.context,  pageItem.toString(), Toast.LENGTH_SHORT).show()
                }
            }
        }
    }
}