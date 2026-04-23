package com.example.myapplication.data.model

// 文件位置：com.example.myapplication.data.model.UserManager.kt


import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

object UserManager {
    private val _currentUser = MutableStateFlow(User())
    val currentUserFlow: StateFlow<User> = _currentUser.asStateFlow()

    val currentUser: User get() = _currentUser.value

    private const val PREF_NAME = "user_prefs"
    private const val KEY_USER_ID = "logged_user_id"
    private const val KEY_EXPIRE_TIME = "login_expire_time"
    private const val EXPIRE_DURATION =2 * 60 * 60 * 1000L


    fun login(context: android.content.Context, user: User) {
        _currentUser.value = user
        // 保存到本地
        val prefs = context.getSharedPreferences(PREF_NAME, android.content.Context.MODE_PRIVATE)
        prefs.edit()
            .putInt(KEY_USER_ID, user.id)
            .putLong(KEY_EXPIRE_TIME, System.currentTimeMillis() + EXPIRE_DURATION)
            .apply()
    }
    fun logout(context: android.content.Context) {
        _currentUser.value = User()
        val prefs = context.getSharedPreferences(PREF_NAME, android.content.Context.MODE_PRIVATE)
        prefs.edit().clear().apply()
    }
    // 尝试从本地自动登录
    suspend fun tryAutoLogin(context: android.content.Context): Boolean {
        val prefs = context.getSharedPreferences(PREF_NAME, android.content.Context.MODE_PRIVATE)
        val userId = prefs.getInt(KEY_USER_ID, 0)
        val expireTime = prefs.getLong(KEY_EXPIRE_TIME, 0L)
        val currentTime = System.currentTimeMillis()

        android.util.Log.d("UserManager", "尝试自动登录: userId=$userId, expireTime=$expireTime, currentTime=$currentTime")

        if (userId != 0 && currentTime < expireTime) {
            val db = com.example.myapplication.data.database.AppDatabase.getInstance(context)
            val user = db.userDao().getUserById(userId)
            android.util.Log.d("UserManager", "查找到用户: $user")
            if (user != null) {
                _currentUser.value = user
                android.util.Log.d("UserManager", "自动登录成功")
                return true
            } else {
                android.util.Log.d("UserManager", "自动登录失败: 数据库中找不到该 ID 的用户")
            }
        } else {
            android.util.Log.d("UserManager", "自动登录失败: 凭证无效或已过期 (过期时间: $expireTime)")
        }
        return false
    }
}