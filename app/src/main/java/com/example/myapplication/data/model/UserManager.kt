package com.example.myapplication.data.model

// 文件位置：com.example.myapplication.data.model.UserManager.kt


object UserManager {
    var currentUser: User = User()   // 默认空用户
        private set

    fun login(user: User) {
        currentUser = user
    }

    fun logout() {
        currentUser = User()
    }
}