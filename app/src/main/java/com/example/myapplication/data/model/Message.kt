package com.example.myapplication.data.model


data class Message(
    val id: String,
    val conversationId: String,
    val senderId: String,
    val receiverId: String,
    val content: String,
    val timestamp: Long,
    val isMine: Boolean,      // 是否自己发送
    val status: Int = 1       // 1:成功, 0:发送中, 2:失败
)