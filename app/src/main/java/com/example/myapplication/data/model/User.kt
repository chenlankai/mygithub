package com.example.myapplication.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey


@Entity(tableName = "user")
data class User(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,

    val username: String = "",    // 用户名
    val phone: String = "",      // 手机号
    val email: String = "",      // 邮箱
    val address: String = "",    // 地址
    val avatar: String = ""     // 头像（本地/网络路径）
)

object UserManager {
    // 当前登录的用户
    var currentUser: User = User(
        username = "张三",
        phone = "19293353407",
        email = "zhangsan@163.com",
        address = "河南省郑州市"
    )
    fun updateUser(user: User) {
        currentUser = user
    }
}