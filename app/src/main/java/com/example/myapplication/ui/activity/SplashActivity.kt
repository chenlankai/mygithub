package com.example.myapplication.ui.activity

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.example.myapplication.data.model.UserManager
import kotlinx.coroutines.launch

class SplashActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        
        // 启动时立即检查登录状态
        lifecycleScope.launch {
            val success = UserManager.tryAutoLogin(applicationContext)
            
            if (success) {
                // 凭证有效且未过期，直接去主页
                startActivity(Intent(this@SplashActivity, MainActivity::class.java))
            } else {
                // 无凭证或已过期，去登录页
                startActivity(Intent(this@SplashActivity, LoginActivity::class.java))
            }
            finish() // 销毁自己，防止用户按返回键回到闪屏页
        }
    }
}
