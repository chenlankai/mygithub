package com.example.myapplication.data.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.example.myapplication.data.model.Message
import kotlinx.coroutines.flow.Flow

@Dao
interface MessageDao {

    // 插入消息
    @Insert
    suspend fun insertMessage(message: Message): Long

    // 根据两个用户的 ID 获取所有双向消息（最稳妥的私聊查询方式）
    @Query("""
        SELECT * FROM Message 
        WHERE (sender_id = :user1Id AND receiver_id = :user2Id) 
           OR (sender_id = :user2Id AND receiver_id = :user1Id) 
        ORDER BY timestamp ASC
    """)
    fun getMessagesBetweenUsers(user1Id: String, user2Id: String): Flow<List<Message>>

    // 根据会话ID获取所有消息（按时间升序）
    @Query("SELECT * FROM Message WHERE conversation_id = :convId ORDER BY timestamp ASC")
    fun getMessagesByConversationId(convId: Int): Flow<List<Message>>

    // 获取某个会话最后一条消息（用于会话列表显示）
    @Query("SELECT * FROM Message WHERE conversation_id = :convId ORDER BY timestamp DESC LIMIT 1")
    suspend fun getLastMessage(convId: Int): Message?

    // 获取所有消息（仅用于调试打印）
    @Query("SELECT * FROM Message")
    suspend fun getAllMessages(): List<Message>
}