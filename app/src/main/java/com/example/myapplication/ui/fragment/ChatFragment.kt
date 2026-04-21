package com.example.myapplication.ui.fragment

import com.example.myapplication.ui.adapter.ConversationAdapter
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.myapplication.data.model.Conversation
import com.example.myapplication.databinding.FragmentChatBinding

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
        adapter = ConversationAdapter { conversation ->
            // 点击进入聊天详情页（暂不实现，可留空或Toast）
            // TODO: 跳转到 ChatDetailActivity，传入 conversationId, peerName, peerAvatar
        }
        binding.rvConversations.layoutManager = LinearLayoutManager(requireContext())
        binding.rvConversations.adapter = adapter
    }

    private fun loadConversations() {
        // 模拟会话数据（实际应从 ViewModel 获取）
        val mockList = listOf(
            Conversation(
                conversationId = "conv1",
                peerId = "user1",
                peerName = "张三",
                peerAvatar = "",
                lastMessage = "好的，我明天过去。",
                lastMessageTime = System.currentTimeMillis() - 3600_000,
                unreadCount = 3
            ),
            Conversation(
                conversationId = "conv2",
                peerId = "user2",
                peerName = "李四",
                peerAvatar = "",
                lastMessage = "在吗？",
                lastMessageTime = System.currentTimeMillis() - 86400_000,
                unreadCount = 1
            ),
            Conversation(
                conversationId = "conv3",
                peerId = "user3",
                peerName = "王五",
                peerAvatar = "",
                lastMessage = "文件已收到。",
                lastMessageTime = System.currentTimeMillis() - 1800_000,
                unreadCount = 0
            )
        )
        adapter.submitList(mockList)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}