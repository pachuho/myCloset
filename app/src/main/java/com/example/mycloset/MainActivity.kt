package com.example.mycloset

import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModel
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

//        Toast.makeText(this, App.prefs.userEmail, Toast.LENGTH_SHORT).show()

        setFragment("home", HomeFragment())
        binding.bottomNavigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener)

    }

    // 네비게이션 클릭
    private val mOnNavigationItemSelectedListener = BottomNavigationView.OnNavigationItemSelectedListener { item ->
        when(item.itemId){
            R.id.menu_home -> {
                setFragment("home", HomeFragment())
            }
            R.id.menu_search -> {
                setFragment("search", SearchFragment())
            }
            R.id.menu_wishList -> {
                setFragment("wish", WishListFragment())
            }
            R.id.menu_myInfo -> {
                setFragment("info", MyInfoFragment())
            }
            else -> false
        }
    }

    private fun setFragment(tag: String, fragment: Fragment): Boolean {
        val manager: FragmentManager = supportFragmentManager
        val ft = manager.beginTransaction()

        if (manager.findFragmentByTag(tag) == null) {
            ft.add(R.id.container, fragment, tag)
        }

        val home = manager.findFragmentByTag("home")
        val search = manager.findFragmentByTag("search")
        val wish = manager.findFragmentByTag("wish")
        val info = manager.findFragmentByTag("info")

        // 프래그먼트 hide
        if(home != null) ft.hide(home)
        if(search != null) ft.hide(search)
        if(wish != null) ft.hide(wish)
        if(info != null) ft.hide(info)

        // 프래그먼트 show
        if (tag == "home" && home != null) ft.show(home)
        if (tag == "search" && search != null) ft.show(search)
        if (tag == "wish" && wish != null) ft.show(wish)
        if (tag == "info" && info != null) ft.show(info)


        ft.commitAllowingStateLoss()
//        ft.replace(R.id.container, fragment)
//        ft.commit()
        return true
    }

    // AAC ViewModel
    class MyViewModel : ViewModel(){

    }

    // 툴바 메뉴 버튼을 설정
    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.main_menu, menu)
        return true
    }

    // 툴바 메뉴 버튼 클릭 시
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId){
            R.id.menu_shopping->{
                Toast.makeText(this, "장바구니", Toast.LENGTH_SHORT).show()
            }
        }
        return super.onOptionsItemSelected(item)
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