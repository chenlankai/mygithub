package com.example.myapplication.data.model

import androidx.room.Dao
import androidx.room.Embedded
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

data class ConversationWithPeer(
    @Embedded val conversation: Conversation,
    val peerName: String,
    val peerAvatar: String
)