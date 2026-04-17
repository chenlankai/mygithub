package com.example.myapplication

// Song.kt
data class Song(
    val id: Int,
    val title: String,      // 歌名
    val artist: String,     // 歌手
    val duration: String,   // 时长 (03:45 格式)
    val resId: Int,         // 资源 ID (R.raw.xxx)
    val cover: android.graphics.Bitmap? = null // 可选：从 MP3 提取的封面图
)