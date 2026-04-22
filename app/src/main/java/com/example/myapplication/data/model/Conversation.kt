package com.example.myapplication.data.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey


@Entity(
    tableName = "conversation",
    indices = [Index(value = ["sender_id", "receiver_id"], unique = true)]
)
data class Conversation(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id")
    val id: Int = 0,

    @ColumnInfo(name = "sender_id", defaultValue = "0")
    val senderId: Int,      // 发送方用户 ID

    @ColumnInfo(name = "receiver_id", defaultValue = "0")
    val receiverId: Int,    // 接收方用户 ID

    @ColumnInfo(name = "last_message", defaultValue = "''")
    val lastMessage: String = "",

    @ColumnInfo(name = "last_message_time")
    val lastMessageTime: Long = 0,

    // 未读数可以针对接收方
    @ColumnInfo(name = "unread_count", defaultValue = "0")
    val unreadCount: Int = 0
)