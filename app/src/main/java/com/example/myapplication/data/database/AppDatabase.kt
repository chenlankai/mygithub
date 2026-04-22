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
@Database(entities = [User::class,Conversation::class,Message::class], version = 2, exportSchema = false)
abstract class AppDatabase : RoomDatabase() {
    abstract fun userDao(): UserDao
    abstract fun conversationDao(): ConversationDao
    abstract fun messageDao(): MessageDao

    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null
        private val defaultConversations = listOf(
            Conversation(
                senderId = 1,          // 张三
                receiverId = 2,        // 李四
                lastMessage = "你好，在吗？",
                lastMessageTime = System.currentTimeMillis() - 3600000,
                unreadCount = 2        // 李四有2条未读
            ),
            Conversation(
                senderId = 2,          // 李四
                receiverId = 1,        // 张三
                lastMessage = "在的，你忙吗？",
                lastMessageTime = System.currentTimeMillis() - 3600000,
                unreadCount = 2        // 张三两条未读
            ),
            Conversation(
                senderId = 3,          // 王五
                receiverId = 1,        // 张三
                lastMessage = "你好，在吗？",
                lastMessageTime = System.currentTimeMillis() - 3600000,
                unreadCount = 2        //张三两条未读
            ),
            Conversation(
                senderId = 3,          // 王五
                receiverId = 2,        // 李四
                lastMessage = "你好，在吗？",
                lastMessageTime = System.currentTimeMillis() - 3600000,
                unreadCount = 2        //李四两条未读
            ),
        )
        private val defaultUsers = listOf(
            User(username = "张三", phone = "19293353407", email = "zhangsan@163.com", address = "河南省郑州市"),
            User(username = "李四", phone = "13812345678", email = "lisi@example.com", address = "北京市朝阳区"),
            User(username = "王五", phone = "13987654321", email = "wangwu@example.com", address = "上海市浦东新区")
        )

        // AppDatabase.kt
        fun getInstance(context: Context): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "app_database.db"
                )
                    .fallbackToDestructiveMigration()   // 必须添加这一行
                    .build()
                INSTANCE = instance
                instance
            }
        }

        // 调用此方法初始化数据（在 Application 或首次使用时调用一次）
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
            Log.d("DatabaseInit", "========== 当前所有会话信息 ==========")
            allConversations.forEach { conversation ->
                Log.d("DatabaseInit", "ID: ${conversation.id}, 发送方: ${conversation.senderId}, 接收方: ${conversation.receiverId}," +
                        " 最后一条消息: ${conversation.lastMessage}")
            }
            Log.d("DatabaseInit", "=====================================")
        }
    }
}