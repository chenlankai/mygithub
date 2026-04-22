package com.example.myapplication

import android.app.Application
import android.util.Log
import com.example.myapplication.data.database.AppDatabase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class MyApplication : Application() {

    override fun onCreate() {
        super.onCreate()
        Log.d("MyApplication", "MyApplication onCreate 执行 - 这是应用启动的第一个入口")

        // 在这里进行全局初始化
        instance = this

        // 异步预加载数据库
        CoroutineScope(Dispatchers.IO).launch {
            try {
                AppDatabase.populateInitialData(applicationContext)
                Log.d("MyApplication", "数据库全局初始化完成")
            } catch (e: Exception) {
                Log.e("MyApplication", "数据库初始化失败", e)
            }
        }
    }

    companion object {
        lateinit var instance: MyApplication
            private set
    }
}