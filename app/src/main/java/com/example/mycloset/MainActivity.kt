package com.example.mycloset

import android.content.Context
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
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
        setSupportActionBar(toolbar)
        val ab = supportActionBar!!
        ab.setDisplayShowCustomEnabled(true)
        ab.setDisplayShowTitleEnabled(false) // 기본 제목 삭제


//        val email : String = intent.getStringExtra("email").toString()

        replaceFragment(HomeFragment())
        bottom_navigation.setOnNavigationItemSelectedListener(mOnNavigationiItemSelectedListener)
    }

    // 툴바 메뉴 버튼을 설정
    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.main_menu, menu)
        return true
    }

    // 툴바 메뉴 버튼 클릭 시
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item!!.itemId){
            R.id.menu_shopping->{
                Toast.makeText(this, "업데이트 예정", Toast.LENGTH_SHORT).show()
            }
        }
        return super.onOptionsItemSelected(item)
    }

    // 네비게이션 클릭
    private val mOnNavigationiItemSelectedListener = BottomNavigationView.OnNavigationItemSelectedListener { item ->
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