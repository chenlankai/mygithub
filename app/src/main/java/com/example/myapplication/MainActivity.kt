package com.example.myapplication

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.viewpager2.widget.ViewPager2
import com.example.myapplication.data.database.AppDatabase
import com.example.myapplication.data.model.UserManager
import com.example.myapplication.ui.adapter.ViewPagerAdapter
import com.example.myapplication.ui.viewmodel.MyViewModel
import com.example.myapplication.utils.MyObserver
import com.example.myapplication.databinding.ActivityMainBinding
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class MainActivity : AppCompatActivity() {
    private lateinit var viewModel: MyViewModel
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // 使用 ViewBinding
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // 异步初始化数据库（只执行一次，不阻塞主线程）
        lifecycleScope.launch(Dispatchers.IO) {
            try {
                // 插入默认数据（内部应做好幂等性检查，如检查表是否为空）
                AppDatabase.populateInitialData(applicationContext)

                // 获取第一个用户作为登录用户
                val userDao = AppDatabase.getInstance(applicationContext).userDao()
                val firstUser = userDao.getFirstUser()
                withContext(Dispatchers.Main) {
                    if (firstUser != null) {
                        UserManager.login(firstUser)
                        Log.d("MainActivity", "用户登录成功: ${firstUser.username}")
                    } else {
                        // 处理无用户场景：可创建默认用户或跳转登录页
                        Log.w("MainActivity", "数据库中没有用户，请检查初始化逻辑")
                    }
                }
            } catch (e: Exception) {
                Log.e("MainActivity", "数据库初始化失败", e)
            }
        }

        // 添加生命周期观察者
        lifecycle.addObserver(MyObserver())

        // 示例：在协程中模拟数据加载（带异常处理）
        lifecycleScope.launch {
            try {
                val data = fetchData()
                Log.d("MainActivity", "获取数据成功: $data")
            } catch (e: Exception) {
                Log.e("MainActivity", "获取数据失败", e)
            }
        }

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

    // 模拟数据加载（可能抛异常）
    private suspend fun fetchData(): String {
        delay(1000)
        // 可在此模拟网络或数据库异常
        // if (Random.nextBoolean()) throw IOException("网络错误")
        return "Result"
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