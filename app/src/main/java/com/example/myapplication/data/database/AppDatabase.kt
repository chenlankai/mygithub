package com.example.myapplication.data.database

import android.content.Context
import android.util.Log
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import com.example.myapplication.data.database.dao.ConversationDao
import com.example.myapplication.data.database.dao.MessageDao
import com.example.myapplication.data.database.dao.UserDao
import com.example.myapplication.data.model.Conversation
import com.example.myapplication.data.model.Message
import com.example.myapplication.data.model.User
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
@Database(entities = [User::class,Conversation::class,Message::class], version = 4, exportSchema = false)
abstract class AppDatabase : RoomDatabase() {
    abstract fun userDao(): UserDao
    abstract fun conversationDao(): ConversationDao
    abstract fun messageDao(): MessageDao

    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null
        private val defaultConversations = listOf(
            Conversation(
                senderId = 1,
                receiverId = 2,
                lastMessage = "Hello, are you there?",
                lastMessageTime = System.currentTimeMillis() - 3600000,
                unreadCount = 2
            ),
            Conversation(
                senderId = 2,
                receiverId = 1,
                lastMessage = "Yes, I am. Are you busy?",
                lastMessageTime = System.currentTimeMillis() - 3600000,
                unreadCount = 2
            ),
            Conversation(
                senderId = 1,
                receiverId = 3,
                lastMessage = "Hi, how are you?",
                lastMessageTime = System.currentTimeMillis() - 3600000,
                unreadCount = 2
            ),
            Conversation(
                senderId = 3,
                receiverId = 1,
                lastMessage = "Hi, how are you?",
                lastMessageTime = System.currentTimeMillis() - 3600000,
                unreadCount = 2
            ),
            Conversation(
                senderId = 3,
                receiverId = 2,
                lastMessage = "Good morning!",
                lastMessageTime = System.currentTimeMillis() - 3600000,
                unreadCount = 2
            ),
            Conversation(
                senderId = 2,
                receiverId = 3,
                lastMessage = "Good morning!",
                lastMessageTime = System.currentTimeMillis() - 3600000,
                unreadCount = 2
            ),
        )
        private val defaultUsers = listOf(
            User(username = "zhangsan", phone = "19293353407", email = "zhangsan@163.com", address = "Henan, Zhengzhou",password = "123456"),
            User(username = "lisi", phone = "13812345678", email = "lisi@example.com", address = "Beijing, Chaoyang",password = "123456"),
            User(username = "wangwu", phone = "13987654321", email = "wangwu@example.com", address = "Shanghai, Pudong",password = "123456")
        )


        fun getInstance(context: Context): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "app_database.db"
                )
                    .fallbackToDestructiveMigration()
                    .build()
                INSTANCE = instance
                instance
            }
        }

        // 调用此方法初始化数据（在 Application 或首次使用时调用一次）同时打印数据库中的信息
        suspend fun populateInitialData(context: Context) {
            Log.d("DatabaseInit", "populateInitialData 开始执行")
            val db = getInstance(context)
            val userDao = db.userDao()
            val userCount = userDao.getUserCount()
            Log.d("DatabaseInit", "当前数据库中的用户数量: $userCount")
            if (userCount == 0) {
                Log.d("DatabaseInit", "开始插入默认用户数据，数量: ${defaultUsers.size}")
                userDao.insertUsers(defaultUsers)
                Log.d("DatabaseInit", "插入完成")
            } else {
                Log.d("DatabaseInit", "数据库已有数据，跳过初始化")
            }
            // 查询并展示所有用户信息
            val allUsers = userDao.getAllUsersOnce()
            Log.d("DatabaseInit", "========== 当前所有用户信息 ==========")
            allUsers.forEach { user ->
                Log.d("DatabaseInit", "ID: ${user.id}, 用户名: ${user.username}, 手机: ${user.phone}, " +
                        "邮箱: ${user.email}, 地址: ${user.address}, 头像: ${user.avatar}")
            }
            Log.d("DatabaseInit", "=====================================")

            val conversationDao = db.conversationDao()
            val conversationCount = conversationDao.getConversationCount()
            Log.d("DatabaseInit", "当前数据库中的会话数量: $conversationCount")

            Log.d("DatabaseInit", "开始插入默认用户数据，数量: ${defaultUsers.size}")
            conversationDao.insertConversations(defaultConversations)
            Log.d("DatabaseInit", "插入完成")

            val allConversations = conversationDao.getAllConversations()
            val sdf = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault())
            Log.d("DatabaseInit", "========== 当前所有会话信息 ==========")
            allConversations.forEach { conversation ->
                val timeString = sdf.format(Date(conversation.lastMessageTime))
                Log.d("DatabaseInit", "ID: ${conversation.id}, 发送方: ${conversation.senderId}, 接收方: ${conversation.receiverId}," +
                        " 最后一条消息: ${conversation.lastMessage}" +
                        ", 时间: $timeString" +
                        ", 未查看消息数量：${conversation.unreadCount}")
            }
            Log.d("DatabaseInit", "=====================================")

            val allMessages = db.messageDao().getAllMessages()
            Log.d("DatabaseInit", "========== 当前所有消息信息 ==========")
            allMessages.forEach { message ->
                val timeString = sdf.format(Date(message.timestamp))
                Log.d("DatabaseInit", "ID: ${message.id}, 会话ID: ${message.conversationId}, " +
                        "发送方: ${message.senderId}, 内容: ${message.content}, 时间: $timeString")
            }
            Log.d("DatabaseInit", "=====================================")

        }
    }
}