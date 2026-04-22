package com.example.myapplication.ui.adapter
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.myapplication.R
import com.example.myapplication.databinding.ItemConversationBinding
import com.example.myapplication.data.model.Conversation
import com.example.myapplication.data.model.ConversationWithPeer
import java.text.SimpleDateFormat
import java.util.*
class ConversationAdapter(
    private var currentUserId: Int,
    private val onItemClick: (Conversation) -> Unit
) : RecyclerView.Adapter<ConversationAdapter.ViewHolder>() {

    private var items = listOf<ConversationWithPeer>()

    fun updateCurrentUserId(userId: Int) {
        currentUserId = userId
    }

    fun submitList(list: List<ConversationWithPeer>) {
        items = list
        notifyDataSetChanged() // 刷新列表
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemConversationBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = items[position]
        holder.bind(item)
        holder.itemView.setOnClickListener { onItemClick(item.conversation) }
    }

    override fun getItemCount() = items.size

    inner class ViewHolder(private val binding: ItemConversationBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(item: ConversationWithPeer) {
            val conv = item.conversation
            binding.tvName.text = item.peerName
            binding.tvLastMessage.text = conv.lastMessage
            binding.tvTime.text = formatTime(conv.lastMessageTime)

            // 未读数：只有当前用户是接收方才显示
            val unread = if (conv.receiverId == currentUserId) conv.unreadCount else 0
            binding.tvUnread.visibility = if (unread > 0) View.VISIBLE else View.GONE
            if (unread > 0) {
                binding.tvUnread.text = if (unread > 99) "99+" else unread.toString()
            }

            Glide.with(binding.ivAvatar.context)
                .load(item.peerAvatar)
                .placeholder(R.drawable.ic_default_avatar)
                .circleCrop()
                .into(binding.ivAvatar)
        }

        private fun formatTime(timestamp: Long): String {
            val now = System.currentTimeMillis()
            val diff = now - timestamp
            return when {
                diff < 60_000 -> "刚刚"
                diff < 3600_000 -> "${diff / 60_000}分钟前"
                diff < 86400_000 -> "${diff / 3600_000}小时前"
                else -> SimpleDateFormat("MM-dd", Locale.getDefault()).format(Date(timestamp))
            }
        }
    }
}