package com.example.myapplication.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey


@Entity(
    tableName = "user"
)
data class User(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,

    val username: String = "",    // 用户名
    val phone: String = "",      // 手机号
    val email: String = "",      // 邮箱
    val address: String = "",    // 地址
    val avatar: String = ""     // 头像（本地/网络路径）
)
