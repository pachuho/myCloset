package com.example.mycloset

import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.example.mycloset.databinding.ActivityMainBinding
import com.example.mycloset.fragment.HomeFragment
import com.example.mycloset.fragment.MyInfoFragment
import com.example.mycloset.fragment.SearchFragment
import com.example.mycloset.fragment.WishListFragment
import com.google.android.material.bottomnavigation.BottomNavigationView

class MainActivity : AppCompatActivity() {
    private var mBinding: ActivityMainBinding? = null
    private val binding get() = mBinding!!

    var lastBackPressedTime: Long = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mBinding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setSupportActionBar(binding.toolbar)
        val ab = supportActionBar!!
        ab.setDisplayShowCustomEnabled(true)
        ab.setDisplayShowTitleEnabled(false) // 기본 제목 삭제


//        val email : String = intent.getStringExtra("email").toString()

        replaceFragment(HomeFragment())
        binding.bottomNavigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener)

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
                Toast.makeText(this, "장바구니", Toast.LENGTH_SHORT).show()
            }
        }
        return super.onOptionsItemSelected(item)
    }

    // 네비게이션 클릭

    private val mOnNavigationItemSelectedListener = BottomNavigationView.OnNavigationItemSelectedListener { item ->
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

    // 뒤로가기
    override fun onBackPressed() {
        val currentTime = System.currentTimeMillis()
        val differenceTime = currentTime - lastBackPressedTime
        if (differenceTime in 0..2000) {
            finish()
        } else {
            lastBackPressedTime = currentTime
            Toast.makeText(this, getString(R.string.one_more_touch_end), Toast.LENGTH_SHORT).show()
        }
    }

    override fun onDestroy() {
        mBinding = null
        super.onDestroy()
    }
}