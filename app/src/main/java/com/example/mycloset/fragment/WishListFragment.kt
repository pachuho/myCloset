package com.hochupa.mycloset.fragment

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import com.hochupa.mycloset.utils.App
import com.hochupa.mycloset.R
import com.hochupa.mycloset.databinding.FragmentWishListBinding
import com.hochupa.mycloset.retrofit.Dress
import com.hochupa.mycloset.retrofit.RetrofitService
import com.hochupa.mycloset.viewpager.ImageRecyclerAdapterWish
import com.hochupa.mycloset.viewpager.PageItem
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class WishListFragment : Fragment() {
    private var mBinding: FragmentWishListBinding? = null
    private val binding get() = mBinding!!

    private var wishItem = ArrayList<PageItem>()

    private lateinit var imageRecyclerAdapterWish: ImageRecyclerAdapterWish

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        mBinding = FragmentWishListBinding.inflate(inflater, container, false)

        imageRecyclerAdapterWish = ImageRecyclerAdapterWish(wishItem)

        // 이미지 정보 가져오기
        getImageData()

        // 아이템 삭제 클릭 이벤트
        imageRecyclerAdapterWish.setOnItemDeleteClickListener(object : ImageRecyclerAdapterWish.OnItemClickListener {
            override fun onItemClick(v: View?, position: Int) {

                wishItem.removeAt(position)
                imageRecyclerAdapterWish.notifyItemRemoved(position)

                if (wishItem.isEmpty()){
                    binding.layoutWishList.visibility = View.VISIBLE
                } else {
                    binding.layoutWishList.visibility = View.INVISIBLE
                }



            }

        })
        return binding.root
    }

    override fun onDestroyView() {
        mBinding = null
        super.onDestroyView()
    }

    // 이미지 정보 가져오기
    private fun getImageData() {
        val retrofitService: RetrofitService = App.Common.retrofit.create(RetrofitService::class.java)
        App.prefs.userEmail?.let {
            retrofitService.getWishList(it).enqueue(object : Callback<List<Dress>> {
                // 통신 성공
                override fun onResponse(call: Call<List<Dress>>, response: Response<List<Dress>>) {
                    val getData = response.body()
                    for (i: Int in 0 until getData?.size!!) {
                        val getCode = getData[i].code
                        val getPart = getData[i].part
                        val getBrand = getData[i].brand
                        val getName = getData[i].name
                        val getPrice = getData[i].price
                        val getImage = getData[i].image
                        val getLink = getData[i].link

                        Log.i("테스트", "브랜드: $getBrand, 이름: $getName, 가격: $getPrice, 이미지: $getImage, 링크: $getLink")
                        wishItem.add(PageItem(getCode, getBrand, getName, getPrice, getImage, getLink))
                    }

                    // 선호상품이 없는 경우
                    if (wishItem.isEmpty()) {
                        binding.layoutWishList.visibility = View.VISIBLE // 보이게
                        binding.wishListImage.visibility = View.INVISIBLE // 안보이게
                    }

                    // 리사이클러뷰 부착
                    binding.wishListImage.apply {
                        adapter = imageRecyclerAdapterWish
                        layoutManager = GridLayoutManager(context, 2)
                        imageRecyclerAdapterWish.notifyDataSetChanged()
                    }
                }

                // 통신 실패
                override fun onFailure(call: Call<List<Dress>>, t: Throwable) {
                    Log.e("getWishList", t.message.toString())
                    Toast.makeText(context, getString(R.string.confirm_network), Toast.LENGTH_SHORT)
                        .show()
                }
            })
        }
    }
}