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

class ImageRecyclerAdapterWish(private var pageList: ArrayList<PageItem>) : RecyclerView.Adapter<ImageRecyclerAdapterWish.PagerViewHolder>(){

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PagerViewHolder {
        return PagerViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.layout_wish_image_item, parent, false))
    }

    override fun getItemCount(): Int =pageList.size

    override fun onBindViewHolder(holderWish: PagerViewHolder, position: Int) {
        val pageItem = pageList[position]

        holderWish.apply {
            holder(pageItem)
        }
    }

    class PagerViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val itemImage = itemView.findViewById<ImageView>(R.id.wish_item_image)
        private val itemBrand = itemView.findViewById<TextView>(R.id.wish_item_brand)
        private val itemName = itemView.findViewById<TextView>(R.id.wish_item_name)
        private val itemPrice = itemView.findViewById<TextView>(R.id.wish_item_price)
        //    private val itemStar = itemView.findViewById<ImageView>(R.id.pager_item_star)
        private val itemClose = itemView.findViewById<ImageView>(R.id.wish_item_close)



        fun holder(pageItem: PageItem){
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
//            itemStar.setImageResource(R.drawable.img_star_outline)
                Profile.favoriteImage.remove(pageItem)
            }
        }
    }
}
