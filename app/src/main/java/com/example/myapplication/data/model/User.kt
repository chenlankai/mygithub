package com.example.myapplication.data.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "user")
data class User(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id")
    val id: Int = 0,

    @ColumnInfo(name = "username", defaultValue = "''")
    val username: String = "",

    @ColumnInfo(name = "password", defaultValue = "'123456'")
    val password: String = "",

    @ColumnInfo(name = "phone", defaultValue = "''")
    val phone: String = "",

    @ColumnInfo(name = "email", defaultValue = "''")
    val email: String = "",

    @ColumnInfo(name = "address", defaultValue = "''")
    val address: String = "",

    @ColumnInfo(name = "avatar", defaultValue = "''")
    val avatar: String = ""
)