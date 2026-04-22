package com.example.myapplication.data.model

// 文件位置：com.example.myapplication.data.model.UserManager.kt


import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

object UserManager {
    private val _currentUser = MutableStateFlow(User())
    val currentUserFlow: StateFlow<User> = _currentUser.asStateFlow()

    val currentUser: User get() = _currentUser.value

    fun login(user: User) {
        _currentUser.value = user
    }

    fun logout() {
        _currentUser.value = User()
    }
}