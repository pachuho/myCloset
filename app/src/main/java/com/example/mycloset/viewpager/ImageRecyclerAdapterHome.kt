package com.hochupa.mycloset.viewpager

import android.content.Intent
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestListener
import com.hochupa.mycloset.*
import com.hochupa.mycloset.retrofit.Favorite
import com.hochupa.mycloset.retrofit.RetrofitService
import com.hochupa.mycloset.retrofit.Success
import com.hochupa.mycloset.utils.App
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

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

            // 별 체크
            getFavorite(pageItem)

            // 이미지 넣기
            Glide.with(itemView.context).load(pageItem.image)
                    .placeholder(R.drawable.image_preview)
                    .override(500, 500)
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
                    deleteFavorite(pageItem)
                } else {
                    addFavorite(pageItem)
                }
            }
        }

        // 선호상품 체크
        private fun getFavorite(pageItem: PageItem){
            val code: ArrayList<Int> = ArrayList()
            val getFavoriteService: RetrofitService = App.Common.retrofit.create(RetrofitService::class.java)
            App.prefs.userEmail?.let {
                getFavoriteService.getFavorite(it).enqueue(object : Callback<List<Favorite>> {
                    // 통신 성공
                    override fun onResponse(call: Call<List<Favorite>>, response: Response<List<Favorite>>) {
                        val getData = response.body()
                        for (i: Int in 0 until getData?.size!!) {
                            code.add(getData[i].code)
                        }
                        Log.i("code", code.toString())

                        // 별 넣기
                        if (pageItem.imageCode in code){ // 포함되어 있다면
                            itemStar.setImageResource(R.drawable.img_star_fill)
                            favoriteCheck = true
                        } else{ // 아니라면
                            itemStar.setImageResource(R.drawable.img_star_outline)
                            favoriteCheck = false
                        }
                    }
                    // 통신 실패
                    override fun onFailure(call: Call<List<Favorite>>, t: Throwable) {
                        Log.e("code", t.message.toString())
                    }
                })
            }
        }

        // 선호상품 추가
        private fun addFavorite(pageItem: PageItem){
            val retrofitService: RetrofitService = App.Common.retrofit.create(RetrofitService::class.java)
            App.prefs.userEmail?.let {
                retrofitService.addFavorite(it, pageItem.imageCode).enqueue(object : Callback<Success> {
                    // 통신 성공
                    override fun onResponse(call: Call<Success>, response: Response<Success>) {
                        if(response.body()?.success == true){
                            itemStar.setImageResource(R.drawable.img_star_fill)
                            favoriteCheck = true
                        }
                    }
                    // 통신 실패
                    override fun onFailure(call: Call<Success>, t: Throwable) {
                        Log.e("code", t.message.toString())
                    }
                })
            }
        }

        // 선호상품 삭제
        private fun deleteFavorite(pageItem: PageItem){
            val retrofitService: RetrofitService = App.Common.retrofit.create(RetrofitService::class.java)
            App.prefs.userEmail?.let {
                retrofitService.deleteFavorite(it, pageItem.imageCode).enqueue(object : Callback<Success> {
                    // 통신 성공
                    override fun onResponse(call: Call<Success>, response: Response<Success>) {
                        if(response.body()?.success == true){
                            itemStar.setImageResource(R.drawable.img_star_outline)
                            favoriteCheck = false
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