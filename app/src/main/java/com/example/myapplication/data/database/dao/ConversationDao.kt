package com.example.myapplication.data.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.example.myapplication.data.model.Conversation
import kotlinx.coroutines.flow.Flow

@Dao
interface ConversationDao {

    // 插入会话
    @Insert
    suspend fun insertConversation(conversation: Conversation)

    // 更新会话
    @Update
    suspend fun updateConversation(conversation: Conversation)

    // 获取所有会话（按时间倒序）
    @Query("SELECT * FROM Conversation ORDER BY lastMessageTime DESC")
    fun getAllConversationsFlow(): Flow<List<Conversation>>

    // 根据ID获取会话
    @Query("SELECT * FROM Conversation WHERE conversationId = :convId LIMIT 1")
    suspend fun getConversationById(convId: String): Conversation?

    // 清空未读数
    @Query("UPDATE Conversation SET unreadCount = 0 WHERE conversationId = :convId")
    suspend fun clearUnreadCount(convId: String)
}