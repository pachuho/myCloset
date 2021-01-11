package com.example.mycloset.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.GridLayoutAnimationController
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.mycloset.Profile
import com.example.mycloset.R
import com.example.mycloset.viewpager.WishImageRecyclerAdapter
import kotlinx.android.synthetic.main.fragment_wish_list.view.*


class WishListFragment : Fragment() {

    private lateinit var wishImageRecyclerAdapter: WishImageRecyclerAdapter
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_wish_list, container, false)

        wishImageRecyclerAdapter = WishImageRecyclerAdapter(Profile.favoriteImage)

        view.wishList_image.apply {
            adapter = wishImageRecyclerAdapter
            layoutManager = GridLayoutManager(view.context, 2)
//            addItemDecoration(DividerItemDecoration(view.context, LinearLayoutManager.VERTICAL))
        }



        return view
    }
}