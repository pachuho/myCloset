package com.example.mycloset.fragment

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.viewpager2.widget.ViewPager2
import com.example.mycloset.Common
import com.example.mycloset.R
import com.example.mycloset.WebViewActivity
import com.example.mycloset.databinding.FragmentHomeBinding
import com.example.mycloset.retrofit.Dress
import com.example.mycloset.retrofit.RetrofitService
import com.example.mycloset.viewpager.ImageRecyclerAdapterHome
import com.example.mycloset.viewpager.PageItem
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class HomeFragment : Fragment(){

    private var mBinding: FragmentHomeBinding? = null
    private val binding get() = mBinding!!

    private var pageItemListOuter = ArrayList<PageItem>()
    private var pageItemListTop = ArrayList<PageItem>()
    private var pageItemListBottom = ArrayList<PageItem>()
    private var pageItemListShoes = ArrayList<PageItem>()
    private var pageItemListAccessories = ArrayList<PageItem>()

    private lateinit var imageRecyclerAdapterHome: ImageRecyclerAdapterHome

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        mBinding = FragmentHomeBinding.inflate(inflater, container, false)
        getImageData()

        binding.homeTvSale.setOnClickListener(commonLink)
        binding.homeBtnSale.setOnClickListener(commonLink)
        binding.tvShoppingMain.setOnClickListener(commonLink)

        return binding.root
    }

    // 이미지 정보 가져오기
    private fun getImageData(){
        val getLinkService: RetrofitService = Common.retrofit.create(RetrofitService::class.java)
        getLinkService.getImageLink().enqueue(object : Callback<List<Dress>> {
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

                    when (getPart) {
                        "outer" -> pageItemListOuter.add(PageItem(getCode, getBrand, getName, getPrice, getImage, getLink))
                        "top" -> pageItemListTop.add(PageItem(getCode, getBrand, getName, getPrice, getImage, getLink))
                        "bottom" -> pageItemListBottom.add(PageItem(getCode, getBrand, getName, getPrice, getImage, getLink))
                        "shoes" -> pageItemListShoes.add(PageItem(getCode, getBrand, getName, getPrice, getImage, getLink))
                        "accessories" -> pageItemListAccessories.add(PageItem(getCode, getBrand, getName, getPrice, getImage, getLink))
                    }
                }

                // 리사이클러뷰 부착
                // 아우터
                imageRecyclerAdapterHome = ImageRecyclerAdapterHome(pageItemListOuter)
                binding.imageViewPagerOuter.apply {
                    adapter = imageRecyclerAdapterHome
                    orientation = ViewPager2.ORIENTATION_HORIZONTAL
                    binding.indicatorOuter.setViewPager2(this)
                }



//                Log.i("resultLink", pageItemListOuter.toString())

                // 상의

                // 하의

                // 신발

                // 악세사리

            }

            // 통신 실패
            override fun onFailure(call: Call<List<Dress>>, t: Throwable) {
                Log.e("getImageLink", t.message.toString())
                Toast.makeText(context, getString(R.string.confirm_network), Toast.LENGTH_SHORT).show()
            }
        })
    }


    private val commonLink : View.OnClickListener = View.OnClickListener {
        val intent = Intent(view?.context, WebViewActivity::class.java)
        intent.putExtra("link", "https://store.musinsa.com/app/?gclid=EAIaIQobChMI96ORvvWI6wIVDXZgCh1rDApGEAAYASAAEgL7DfD_BwE")
        startActivity(intent)
    }

    override fun onDestroyView() {
        mBinding = null
        super.onDestroyView()
    }
}