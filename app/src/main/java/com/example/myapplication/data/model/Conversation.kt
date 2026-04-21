package com.example.myapplication.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey


data class Conversation(
    val conversationId: String,
    val peerId: String,
    val peerName: String,
    val peerAvatar: String,
    val lastMessage: String,
    val lastMessageTime: Long,
    val unreadCount: Int
)