package com.example.myapplication.ui.fragment

import android.os.Bundle
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
        adapter = ConversationAdapter(currentUserId) { conversation ->
            // 点击进入聊天详情页
            // TODO: 跳转
        }
        binding.rvConversations.layoutManager = LinearLayoutManager(requireContext())
        binding.rvConversations.adapter = adapter
    }

    private fun loadConversations() {
        lifecycleScope.launch {
            val currentUserId = UserManager.currentUser.id
            if (currentUserId == 0) return@launch
            val conversationDao = AppDatabase.getInstance(requireContext()).conversationDao()
            val conversations = conversationDao.getConversationsWithPeer(currentUserId).first()
            adapter.submitList(conversations)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}