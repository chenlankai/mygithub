package com.example.myapplication.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class MainViewModel : ViewModel() {
    fun fetchData() {
        viewModelScope.launch(Dispatchers.IO) {
            // 模拟网络请求
            val result = networkCall()
            // 切换到主线程更新 UI
            withContext(Dispatchers.Main) {
                // 更新 LiveData 或 StateFlow
            }
        }
    }

    private suspend fun networkCall(): String {
        delay(1000) // 模拟耗时
        return "Hello"
    }
}