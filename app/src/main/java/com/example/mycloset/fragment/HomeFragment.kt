package com.example.mycloset.fragment

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.viewpager2.widget.ViewPager2
import com.example.mycloset.R
import com.example.mycloset.retrofit.Common
import com.example.mycloset.retrofit.Dress
import com.example.mycloset.retrofit.RetrofitService
import com.example.mycloset.viewpager.ImageRecyclerAdapter
import kotlinx.android.synthetic.main.fragment_home.view.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class HomeFragment : Fragment(){

    private var pageItemListOuter = ArrayList<String>()
    private var pageItemListTop = ArrayList<String>()
    private var pageItemListBottom = ArrayList<String>()
    private var pageItemListShoes = ArrayList<String>()
    private var pageItemListAccessories = ArrayList<String>()

    private lateinit var imageRecyclerAdapter: ImageRecyclerAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)



    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_home, container, false)

        val getLinkService: RetrofitService = Common.retrofit.create(RetrofitService::class.java)
        getLinkService.getImageLink().enqueue(object : Callback<List<Dress>> {
            // 통신 성공
            override fun onResponse(call: Call<List<Dress>>, response: Response<List<Dress>>) {
                val getData = response.body()

                for (i: Int in 0 until getData?.size!!){
                    val getPart = getData[i].part
                    val getLink = getData[i].link

                    if (getPart == "outer")
                        pageItemListOuter.add(getLink)
                }

                // 아우터
                imageRecyclerAdapter = ImageRecyclerAdapter(pageItemListOuter)
                view.image_view_pager.apply {
                    adapter = imageRecyclerAdapter
                    orientation = ViewPager2.ORIENTATION_HORIZONTAL
                }
                Log.i("resultLink", pageItemListOuter.toString())
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

        return view
    }

}