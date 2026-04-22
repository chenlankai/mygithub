package com.example.myapplication.ui.activity

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.viewpager2.widget.ViewPager2
import com.example.myapplication.R
import com.example.myapplication.data.database.AppDatabase
import com.example.myapplication.data.model.UserManager
import com.example.myapplication.databinding.ActivityMainBinding
import com.example.myapplication.ui.adapter.ViewPagerAdapter
import com.example.myapplication.ui.viewmodel.MyViewModel
import com.example.myapplication.utils.MyObserver
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity() {
    private lateinit var viewModel: MyViewModel
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // 如果未登录且 UserManager 也没有缓存用户，则跳转
        if (UserManager.currentUser.id == 0) {
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
            finish()
            return
        }

        // 使用 ViewBinding
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // 添加生命周期观察者
        lifecycle.addObserver(MyObserver())



        // 初始化 ViewModel
        viewModel = ViewModelProvider(this)[MyViewModel::class.java]
        Log.d("MainActivity", "使用了 ViewModel")

        // 触发 ViewModel 中的操作（示例）
        viewModel.increment()

        // 收集 Flow（生命周期感知）
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.simpleFlow().collect { value ->
                    Log.d("MainActivity", "收集到 Flow 值: $value")
                }
            }
        }

        // 初始化 TabLayout 和 ViewPager2
        initTabAndViewPager()
        initTabSelectListener()
        initViewPagerCallback()
    }

    // 1. 初始化 TabLayout + ViewPager2 绑定
    private fun initTabAndViewPager() {
        val tabLayout = binding.tabLayout
        val viewPager = binding.viewPager

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

    // 2. Tab 选中监听
    private fun initTabSelectListener() {
        val tabLayout = binding.tabLayout
        tabLayout.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
            override fun onTabSelected(tab: TabLayout.Tab?) {
                Log.d("MainActivity", "选中了 ${tab?.text}")
            }

            override fun onTabReselected(tab: TabLayout.Tab?) {
                Log.d("MainActivity", "再次选中了 ${tab?.text}")
            }

            override fun onTabUnselected(tab: TabLayout.Tab?) {
                Log.d("MainActivity", "取消选中了 ${tab?.text}")
            }
        })
    }

    // 3. ViewPager2 滑动监听
    private fun initViewPagerCallback() {
        val viewPager = binding.viewPager
        viewPager.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
            override fun onPageSelected(position: Int) {
                super.onPageSelected(position)
                Log.d("MainActivity", "选中页面位置: $position")
            }

            override fun onPageScrollStateChanged(state: Int) {
                super.onPageScrollStateChanged(state)
                Log.d("MainActivity", "滑动状态改变: $state")
            }

            override fun onPageScrolled(
                position: Int,
                positionOffset: Float,
                positionOffsetPixels: Int
            ) {
                super.onPageScrolled(position, positionOffset, positionOffsetPixels)
                Log.d("MainActivity", "滑动中 position: $position")
            }
        })
    }
}