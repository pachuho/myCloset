package com.example.mycloset.fragment

import android.content.Context.INPUT_METHOD_SERVICE
import android.os.Bundle
import android.util.Log
import android.view.*
import android.view.KeyEvent.KEYCODE_ENTER
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import com.example.mycloset.Common
import com.example.mycloset.R
import com.example.mycloset.databinding.FragmentSearchBinding
import com.example.mycloset.retrofit.Dress
import com.example.mycloset.retrofit.RetrofitService
import com.example.mycloset.viewpager.ImageRecyclerAdapterSearch
import com.example.mycloset.viewpager.PageItem
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class SearchFragment : Fragment() {
    private var mBinding: FragmentSearchBinding? = null
    private val binding get() = mBinding!!

    private var searchItemList = ArrayList<PageItem>()
    lateinit var searchEditText: EditText

    private lateinit var imageRecyclerAdapterSearch: ImageRecyclerAdapterSearch

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        mBinding = FragmentSearchBinding.inflate(inflater, container, false)
        searchEditText = binding.searchEditText

        // 리사이클러뷰 초기화
        imageRecyclerAdapterSearch = ImageRecyclerAdapterSearch(searchItemList)

        searchEditText.setOnKeyListener { v, keyCode, event ->
            if (event.action == KeyEvent.ACTION_DOWN && keyCode == KEYCODE_ENTER) {

                if (searchEditText.text.isNullOrEmpty()) {
                    Toast.makeText(context, "검색어를 입력해주세요.", Toast.LENGTH_SHORT).show()
                    binding.searchImage.visibility = View.INVISIBLE
                    binding.searchLlClassification.visibility = View.VISIBLE
                } else {
                    // 키보드 내리기
                    val mInputMethodManager = context!!.getSystemService(INPUT_METHOD_SERVICE) as InputMethodManager
                    mInputMethodManager.hideSoftInputFromWindow(searchEditText.windowToken, 0)

//                    searchItem.clear()
                    Log.i("검색", "입력 텍스트:" + searchEditText.text.toString())
                    getImageData(searchEditText.text.toString())
                    binding.searchImage.visibility = View.VISIBLE
                    binding.searchLlClassification.visibility = View.INVISIBLE
                }
            }
            true
        }


        return binding.root
    }


    // 상품 정보 가져오기
    private fun getImageData(query: String){
        val getItemService: RetrofitService = Common.retrofit.create(RetrofitService::class.java)
        getItemService.searchItem(query).enqueue(object : Callback<List<Dress>> {
            // 통신 성공
            override fun onResponse(call: Call<List<Dress>>, response: Response<List<Dress>>) {
                val getData = response.body()

                for (i: Int in 0 until getData?.size!!) {
                    val getCode = getData[i].code
                    val getBrand = getData[i].brand
                    val getName = getData[i].name
                    val getPrice = getData[i].price
                    val getImage = getData[i].image
                    val getLink = getData[i].link

                    searchItemList.add(PageItem(getCode, getBrand, getName, getPrice, getImage, getLink))
                    Log.i("검색", "아이템 : " + searchItemList[0].image)
                }

                // 리사이클러뷰 부착
                binding.searchImage.apply {
                    adapter = imageRecyclerAdapterSearch
                    layoutManager = GridLayoutManager(context, 2)
                    imageRecyclerAdapterSearch.notifyDataSetChanged()
                }

            }

            // 통신 실패
            override fun onFailure(call: Call<List<Dress>>, t: Throwable) {
                Log.e("getItem", t.message.toString())
                Toast.makeText(context, getString(R.string.confirm_network), Toast.LENGTH_SHORT)
                    .show()
            }
        })
    }

    override fun onDestroyView() {
        mBinding = null
        super.onDestroyView()
    }

}