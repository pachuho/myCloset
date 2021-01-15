package com.example.mycloset.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import com.example.mycloset.Profile
import com.example.mycloset.databinding.FragmentWishListBinding
import com.example.mycloset.viewpager.ImageRecyclerAdapterWish


class WishListFragment : Fragment() {
    private var mBinding: FragmentWishListBinding? = null
    private val binding get() = mBinding!!
    private lateinit var imageRecyclerAdapterWish: ImageRecyclerAdapterWish
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        mBinding = FragmentWishListBinding.inflate(inflater, container, false)

        imageRecyclerAdapterWish = ImageRecyclerAdapterWish(Profile.favoriteImage)


        binding.wishListImage.apply {
            adapter = imageRecyclerAdapterWish
            layoutManager = GridLayoutManager(context, 2)
//            addItemDecoration(DividerItemDecoration(view.context, LinearLayoutManager.VERTICAL))
        }



        return binding.root
    }

    override fun onDestroyView() {
        mBinding = null
        super.onDestroyView()
    }
}