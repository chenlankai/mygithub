package com.example.myapplication.data.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    tableName = "message"
)
data class Message(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id")
    val id: Int = 0,

    @ColumnInfo(name = "conversation_id", defaultValue = "''")
    val conversationId: String = "",

    @ColumnInfo(name = "sender_id", defaultValue = "''")
    val senderId: String = "",

    @ColumnInfo(name = "receiver_id", defaultValue = "''")
    val receiverId: String = "",

    @ColumnInfo(name = "content", defaultValue = "''")
    val content: String = "",

    @ColumnInfo(name = "timestamp")
    val timestamp: Long = 0,      // 毫秒时间戳

    @ColumnInfo(name = "is_mine", defaultValue = "0")
    val isMine: Boolean = false,   // 是否自己发送

    @ColumnInfo(name = "status", defaultValue = "1")
    val status: Int = 1            // 1:成功, 0:发送中, 2:失败
)