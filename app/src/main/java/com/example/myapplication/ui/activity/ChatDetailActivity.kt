package com.example.myapplication.ui.activity

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.myapplication.data.database.AppDatabase
import com.example.myapplication.data.model.Conversation
import com.example.myapplication.data.model.Message
import com.example.myapplication.data.model.UserManager
import com.example.myapplication.databinding.ActivityChatDetailBinding
import com.example.myapplication.ui.adapter.MessageAdapter
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class ChatDetailActivity : AppCompatActivity() {

    private lateinit var binding: ActivityChatDetailBinding
    private lateinit var adapter: MessageAdapter
    private var otherUserId: Int = 0
    private var conversationId: Int = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityChatDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        otherUserId = intent.getIntExtra("otherUserId", 0)
        conversationId = intent.getIntExtra("conversationId", 0)

        initViews()
        loadOtherUserInfo()
        observeMessages()
        clearMyUnread()
    }

    private fun clearMyUnread() {
        lifecycleScope.launch(Dispatchers.IO) {
            val db = AppDatabase.getInstance(applicationContext)
            db.conversationDao().clearMyUnreadCount(UserManager.currentUser.id, otherUserId)
        }
    }

    private fun initViews() {
        adapter = MessageAdapter()
        binding.rvMessages.layoutManager = LinearLayoutManager(this).apply {
            stackFromEnd = true // 从底部开始堆叠
        }
        binding.rvMessages.adapter = adapter

        binding.btnSend.setOnClickListener {
            sendMessage()
        }

        binding.toolbar.setNavigationOnClickListener { finish() }
    }

    private fun loadOtherUserInfo() {
        lifecycleScope.launch(Dispatchers.IO) {
            val user = AppDatabase.getInstance(applicationContext).userDao().getUserById(otherUserId)
            withContext(Dispatchers.Main) {
                user?.let {
                    binding.toolbar.title = it.username
                    // 适配器需要对方头像来显示在左侧
                    adapter.setMessages(emptyList(), it.avatar)
                }
            }
        }
    }

    private fun observeMessages() {
        lifecycleScope.launch {
            AppDatabase.getInstance(applicationContext).messageDao()
                .getMessagesByConversationId(conversationId)
                .collect { messages ->
                    val otherUser = AppDatabase.getInstance(applicationContext).userDao().getUserById(otherUserId)
                    adapter.setMessages(messages, otherUser?.avatar ?: "")

                    if (messages.isNotEmpty()) {
                        binding.rvMessages.scrollToPosition(messages.size - 1)
                    }
                }
        }
    }

    private fun sendMessage() {
        val content = binding.etMessage.text.toString().trim()
        if (content.isEmpty()) return

        val now = System.currentTimeMillis()
        val myId = UserManager.currentUser.id

        lifecycleScope.launch(Dispatchers.IO) {
            val db = AppDatabase.getInstance(applicationContext)
            val convDao = db.conversationDao()

            // 1. 处理“我看到的”会话 (对方发给我，ID是我用于查询列表的依据)
            var myConv = convDao.getConversation(otherUserId, myId)
            if (myConv == null) {
                val newId = convDao.insertConversation(Conversation(senderId = otherUserId, receiverId = myId))
                myConv = Conversation(id = newId.toInt(), senderId = otherUserId, receiverId = myId)
            }
            convDao.updateLastMessageById(myConv.id, content, now)

            // 2. 处理“对方看到的”会话 (我发给对方)
            var peerConv = convDao.getConversation(myId, otherUserId)
            if (peerConv == null) {
                val newId = convDao.insertConversation(Conversation(senderId = myId, receiverId = otherUserId))
                peerConv = Conversation(id = newId.toInt(), senderId = myId, receiverId = otherUserId)
            }
            convDao.updateLastMessageById(peerConv.id, content, now)
            convDao.incrementUnreadCount(peerConv.id)

            // 3. 插入消息 (关联到当前的会话)
            val newMessage1 = Message(
                conversationId = myConv.id.toString(),
                senderId = myId.toString(),
                receiverId = otherUserId.toString(),
                content = content,
                timestamp = now
            )
            db.messageDao().insertMessage(newMessage1)
            // 4. 插入消息 (关联到对方的会话)
            val newMessage2 = Message(
                conversationId = peerConv.id.toString(),
                senderId = myId.toString(),
                receiverId = otherUserId.toString(),
                content = content,
                timestamp = now
            )
            db.messageDao().insertMessage(newMessage2)


            withContext(Dispatchers.Main) {
                binding.etMessage.text.clear()
            }
        }
    }
}