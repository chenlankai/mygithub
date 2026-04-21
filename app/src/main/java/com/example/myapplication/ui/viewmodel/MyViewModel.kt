package com.example.myapplication.ui.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.launch

class MyViewModel : ViewModel() {
    private val _count = MutableLiveData<Int>().apply { value = 0 }
    val count: LiveData<Int> = _count

    fun increment() {
        viewModelScope.launch {
            try {
                val result = riskyOperation() // 网络/数据库
            } catch (e: Exception) {
                // 处理异常
            }
        }
        _count.value = (_count.value ?: 0) + 1
    }

    private fun riskyOperation(): String {
        return "result"
    }
    //创建Flow数据流
    fun simpleFlow(): Flow<Int> = flow {
        for (i in 1..3) {
            delay(1000)
            emit(i)
        }
    }

}