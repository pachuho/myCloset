package com.example.mycloset

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

        // 툴바 뒤로가기
        setSupportActionBar(main_toolbar)
        val ab = supportActionBar!!
        ab.setDisplayShowTitleEnabled(false)
        ab.setDisplayHomeAsUpEnabled(true)
//        val email : String = intent.getStringExtra("email").toString()

        replaceFragment(HomeFragment())
        bottom_navigation.setOnNavigationItemSelectedListener(mOnNavigationiItemSelectedListener)
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_toolbar, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId) {
            R.id.menu_toolbar_home -> Toast.makeText(this, "장바구니, 업데이트 예정", Toast.LENGTH_SHORT).show()
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