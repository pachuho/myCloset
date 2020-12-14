package com.example.mycloset

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentTransaction
import com.example.mycloset.Fragment.HomeFragment
import com.example.mycloset.Fragment.MyInfoFragment
import com.example.mycloset.Fragment.SearchFragment
import com.example.mycloset.Fragment.WishListFragment
import com.google.android.material.bottomnavigation.BottomNavigationView
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

//        val email : String = intent.getStringExtra("email").toString()

        replaceFragment(HomeFragment())
        bottom_navigation.setOnNavigationItemSelectedListener(mOnNavigationiItemSelectedListener)
    }

    private val mOnNavigationiItemSelectedListener = BottomNavigationView.OnNavigationItemSelectedListener {item ->
        when(item.itemId){
            R.id.menu_home -> {
                replaceFragment(HomeFragment())
            }
            R.id.menu_search -> {
                replaceFragment(SearchFragment())
            }
            R.id.menu_wishList -> {
                replaceFragment(WishListFragment())
            }
            R.id.menu_myInfo -> {
                replaceFragment(MyInfoFragment())
            }
            else -> false
        }
    }

    private fun replaceFragment(fragment: Fragment): Boolean {
        val fragmentTransaction = supportFragmentManager.beginTransaction()
        fragmentTransaction.replace(R.id.container, fragment)
        fragmentTransaction.commit()
        return true
    }
}