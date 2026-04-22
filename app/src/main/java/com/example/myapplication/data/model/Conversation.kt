package com.example.myapplication.data.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    tableName = "conversation"
)
data class Conversation(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id")
    val id: Int = 0,

    @ColumnInfo(name = "conversation_id", defaultValue = "''")
    val conversationId: String = "",

    @ColumnInfo(name = "peer_id", defaultValue = "''")
    val peerId: String = "",

    @ColumnInfo(name = "peer_name", defaultValue = "''")
    val peerName: String = "",

    @ColumnInfo(name = "peer_avatar", defaultValue = "''")
    val peerAvatar: String = "",

    @ColumnInfo(name = "last_message", defaultValue = "''")
    val lastMessage: String = "",

    @ColumnInfo(name = "last_message_time")
    val lastMessageTime: Long = 0,

    @ColumnInfo(name = "unread_count", defaultValue = "0")
    val unreadCount: Int = 0
)