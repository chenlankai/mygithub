package com.example.myapplication

import android.os.Bundle
import android.util.Log
import android.widget.TableLayout
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.viewpager2.widget.ViewPager2
import com.example.myapplication.databinding.ActivityMainBinding
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import kotlin.math.log
class MainActivity : AppCompatActivity() {
    private lateinit var viewModel: MyViewModel
    private lateinit var binding: ActivityMainBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        lifecycle.addObserver(MyObserver())

        // 使用 viewModel.count
        viewModel = ViewModelProvider(this).get(MyViewModel::class.java)
        Log.d("MainActivity","使用了viewModel")

        // 想测试哪块 就留哪块，不想用直接注释掉！
        initTabAndViewPager()
        initTabSelectListener()
        initViewPagerCallback()
    }

    // 1. 初始化 TabLayout + ViewPager2 绑定
    private fun initTabAndViewPager() {
        val tabLayout = findViewById<TabLayout>(R.id.tabLayout)
        val viewPager = findViewById<ViewPager2>(R.id.viewPager)

        viewPager.adapter = ViewPagerAdapter(this)

        viewPager.offscreenPageLimit = 1

        TabLayoutMediator(tabLayout, viewPager) { tab, position ->
            when (position) {
                0 -> {
                    tab.text = "聊天"
                    tab.setIcon(R.drawable.ic_chat)
                }
                1 -> {
                    tab.text = "数据"
                    tab.setIcon(R.drawable.ic_table)
                }
                2 -> {
                    tab.text = "发现"
                    tab.setIcon(R.drawable.ic_discover)
                }
                else -> {
                    tab.text = "我的"
                    tab.setIcon(R.drawable.ic_my)
                }
            }
        }.attach()
    }
    // 2. 标签点击监听（想测试就开，不想测试注释）
    private fun initTabSelectListener() {
        val tabLayout = findViewById<TabLayout>(R.id.tabLayout)
        tabLayout.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
            override fun onTabSelected(tab: TabLayout.Tab?) {
                Log.d("MainActivity", "选中了${tab?.text}")
            }
            override fun onTabReselected(tab: TabLayout.Tab?) {
                Log.d("MainActivity", "再次选中了${tab?.text}")
            }
            override fun onTabUnselected(tab: TabLayout.Tab?) {
                Log.d("MainActivity", "取消选中了${tab?.text}")
            }
        })
    }

    // 3. 页面滑动监听（想测试就开）
    private fun initViewPagerCallback() {
        val viewPager = findViewById<ViewPager2>(R.id.viewPager)
        viewPager.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
            override fun onPageSelected(position: Int) {
                super.onPageSelected(position)
                Log.d("MainActivity", "选中了$position")
            }
            override fun onPageScrollStateChanged(state: Int) {
                super.onPageScrollStateChanged(state)
                Log.d("MainActivity", "状态改变了$state")
            }
            override fun onPageScrolled(
                position: Int,
                positionOffset: Float,
                positionOffsetPixels: Int
            ) {
                super.onPageScrolled(position, positionOffset, positionOffsetPixels)
                Log.d("MainActivity", "滑动中 position:$position")
            }
        })
    }


}