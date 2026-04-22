package com.example.myapplication.data.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.example.myapplication.data.model.Conversation
import com.example.myapplication.data.model.ConversationWithPeer
import kotlinx.coroutines.flow.Flow

@Dao
interface ConversationDao {
    @Insert(onConflict = OnConflictStrategy.IGNORE)  // 或 REPLACE
    suspend fun insertConversations(conversations: List<Conversation>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)  // 或 IGNORE，配合业务判断
    suspend fun insertConversation(conversation: Conversation): Long


    // 获取会话总数
    @Query("SELECT COUNT(*) FROM conversation")
    suspend fun getConversationCount(): Int

    @Query("SELECT * FROM conversation")
    suspend fun getAllConversations(): List<Conversation>

    @Query("""
        SELECT 
            c.*, 
            u.username AS peerName, 
            u.avatar AS peerAvatar
        FROM conversation c
        INNER JOIN user u ON 
            (c.sender_id = :currentUserId AND c.receiver_id = u.id) OR 
            (c.receiver_id = :currentUserId AND c.sender_id = u.id)
        ORDER BY c.last_message_time DESC
    """)
    fun getConversationsWithPeer(currentUserId: Int): Flow<List<ConversationWithPeer>>

}