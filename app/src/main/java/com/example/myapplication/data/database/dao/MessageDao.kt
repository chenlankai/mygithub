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

    // 根据会话ID获取所有消息（按时间升序）
    @Query("SELECT * FROM Message WHERE id = :convId ORDER BY timestamp ASC")
    fun getMessagesByConversationId(convId: String): Flow<List<Message>>

    // 获取某个会话最后一条消息（用于会话列表显示）
    @Query("SELECT * FROM Message WHERE id = :convId ORDER BY timestamp DESC LIMIT 1")
    suspend fun getLastMessage(convId: String): Message?
}