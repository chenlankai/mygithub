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

    @Query("SELECT * FROM conversation WHERE sender_id = :senderId AND receiver_id = :receiverId LIMIT 1")
    suspend fun getConversation(senderId: Int, receiverId: Int): Conversation?

    // 更新最后一条消息
    @Query("UPDATE conversation SET last_message = :lastMsg, last_message_time = :lastTime WHERE id = :id")
    suspend fun updateLastMessageById(id: Int, lastMsg: String, lastTime: Long)

    // 增加未读数
    @Query("UPDATE conversation SET unread_count = unread_count + 1 WHERE id = :id")
    suspend fun incrementUnreadCount(id: Int)

    // 清除自己的未读数（当我进入聊天时）
    @Query("UPDATE conversation SET unread_count = 0 WHERE receiver_id = :myId AND sender_id = :otherId")
    suspend fun clearMyUnreadCount(myId: Int, otherId: Int)

    // 增加对方的未读数（当我发送消息时）
    @Query("UPDATE conversation SET unread_count = unread_count + 1 WHERE receiver_id = :otherId AND sender_id = :myId")
    suspend fun incrementPeerUnreadCount(myId: Int, otherId: Int)

    // 同时更新双向会话的最后一条消息（确保双方列表都更新）
    @Query("""
        UPDATE conversation 
        SET last_message = :lastMsg, last_message_time = :lastTime 
        WHERE (sender_id = :myId AND receiver_id = :otherId) 
           OR (sender_id = :otherId AND receiver_id = :myId)
    """)
    suspend fun updateLastMessageBidirectional(myId: Int, otherId: Int, lastMsg: String, lastTime: Long)

    @Query("""
        SELECT 
            c.*, 
            u.username AS peerName, 
            u.avatar AS peerAvatar
        FROM conversation c
        INNER JOIN user u ON c.sender_id = u.id
        WHERE c.receiver_id = :currentUserId
        ORDER BY c.last_message_time DESC
    """)
    fun getConversationsByReceiverIdFlow(currentUserId: Int): Flow<List<ConversationWithPeer>>


}