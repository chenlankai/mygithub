package com.example.myapplication.ui.fragment

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.myapplication.data.database.AppDatabase
import com.example.myapplication.data.model.UserManager   // 正确的导入
import com.example.myapplication.databinding.FragmentChatBinding
import com.example.myapplication.ui.adapter.ConversationAdapter
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch

class ChatFragment : Fragment() {

    private var _binding: FragmentChatBinding? = null
    private val binding get() = _binding!!
    private lateinit var adapter: ConversationAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentChatBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupRecyclerView()
        loadConversations()
    }

    private fun setupRecyclerView() {
        // 从 UserManager 获取当前用户 ID
        val currentUserId = UserManager.currentUser.id
        adapter = ConversationAdapter(currentUserId) { conversationWithPeer ->
            val conversation = conversationWithPeer.conversation
            // 确定对方的 ID（如果我是发送者，对方就是接收者；反之亦然）
            val otherUserId = if (conversation.senderId == currentUserId) conversation.receiverId else conversation.senderId
            
            val intent = android.content.Intent(requireContext(), com.example.myapplication.ui.activity.ChatDetailActivity::class.java).apply {
                putExtra("otherUserId", otherUserId)
                putExtra("conversationId", conversation.id)
            }
            startActivity(intent)
        }
        binding.rvConversations.layoutManager = LinearLayoutManager(requireContext())
        binding.rvConversations.adapter = adapter
    }

    private fun loadConversations() {
        lifecycleScope.launch {

            UserManager.currentUserFlow.collect { user ->
                val currentUserId = user.id
                Log.d("ChatFragment", "UserManager 状态更新，当前 userId: $currentUserId")
                
                if (currentUserId != 0) {

                    adapter.updateCurrentUserId(currentUserId)

                    Log.d("ChatFragment", "开始从数据库读取发送给 userId 为 $currentUserId 的会话")
                    val conversationDao = AppDatabase.getInstance(requireContext()).conversationDao()
                    

                    conversationDao.getConversationsByReceiverIdFlow(currentUserId).collect { conversations ->
                        Log.d("ChatFragment", "读取到会话数量: ${conversations.size}")
                        

                        conversations.forEachIndexed { index, item ->
                            val conv = item.conversation
                            Log.d("ChatFragment", "查找到的会话[$index]: ID=${conv.id}, " +
                                    "发送者ID=${conv.senderId}, 接收者ID=${conv.receiverId}, 对方名称=${item.peerName}, 最后消息=${conv.lastMessage}" +
                                    ", 未查看的消息数量=${conv.unreadCount}")
                        }

                        adapter.submitList(conversations)
                    }
                } else {
                    Log.w("ChatFragment", "当前未登录 (userId=0)")
                }
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}