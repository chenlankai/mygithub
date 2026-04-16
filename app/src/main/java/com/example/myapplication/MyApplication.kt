package com.example.myapplication

import android.app.Application

class MyApplication : Application() {
    
    // 使用伴生对象模拟全局数据库
    companion object {
        var registeredUsername: String? = "123"
        var registeredPassword: String? = "123"
    }

    override fun onCreate() {
        super.onCreate()
    }
}