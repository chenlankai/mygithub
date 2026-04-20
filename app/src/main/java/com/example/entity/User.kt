package com.example.entity

// entity/User.kt
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "user")
class User(
    @PrimaryKey(autoGenerate = true) val id:  Int = 0,
    val username: String = "",
    val phone: String = "",
    val email: String = "",
    val address: String = ""
)