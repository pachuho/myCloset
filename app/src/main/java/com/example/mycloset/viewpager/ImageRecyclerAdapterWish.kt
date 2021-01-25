package com.example.mycloset.viewpager

import android.content.Intent
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.mycloset.utils.App
import com.example.mycloset.R
import com.example.mycloset.WebViewActivity
import com.example.mycloset.retrofit.RetrofitService
import com.example.mycloset.retrofit.Success
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ImageRecyclerAdapterWish(private var pageList: ArrayList<PageItem>) : RecyclerView.Adapter<ImageRecyclerAdapterWish.PagerViewHolder>(){

    interface OnItemClickListener {
        fun onItemClick(v: View?, position: Int)
    }

    // 리스너 객체 참조를 저장하는 변수
    private var mListener: OnItemClickListener? = null

    // OnItemClickListener 리스너 객체 참조를 어댑터에 전달하는 메서드
    fun setOnItemDeleteClickListener(listener: OnItemClickListener?) {
        mListener = listener
    }

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


    inner class PagerViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        private val itemImage = itemView.findViewById<ImageView>(R.id.wish_item_image)
        private val itemBrand = itemView.findViewById<TextView>(R.id.wish_item_brand)
        private val itemName = itemView.findViewById<TextView>(R.id.wish_item_name)
        private val itemPrice = itemView.findViewById<TextView>(R.id.wish_item_price)
        private val itemClose = itemView.findViewById<ImageView>(R.id.wish_item_close)

        fun holder(pageItem: PageItem){
            // 이미지 넣기
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
                deleteFavorite(pageItem)
                val pos = adapterPosition
                if (pos != RecyclerView.NO_POSITION) {
                    mListener?.onItemClick(it, pos)
                }
            }


        }

        // 선호상품 삭제
        private fun deleteFavorite(pageItem: PageItem){
            val retrofitService: RetrofitService = App.Common.retrofit.create(RetrofitService::class.java)
            App.prefs.userEmail?.let {
                retrofitService.deleteFavorite(it, pageItem.imageCode).enqueue(object :
                    Callback<Success> {
                    // 통신 성공
                    override fun onResponse(call: Call<Success>, response: Response<Success>) {
                        if (response.body()?.success == true) {
//                            Toast.makeText(itemView.context, "삭제", Toast.LENGTH_SHORT).show()
                        }
                    }

                    // 통신 실패
                    override fun onFailure(call: Call<Success>, t: Throwable) {
                        Log.e("code", t.message.toString())
                    }
                })
            }
        }


    }
}
