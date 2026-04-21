package com.example.myapplication.ui.adapter
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.myapplication.R
import com.example.myapplication.databinding.ItemConversationBinding
import com.example.myapplication.data.model.Conversation
import java.text.SimpleDateFormat
import java.util.*

class ConversationAdapter(
    private val onItemClick: (Conversation) -> Unit
) : RecyclerView.Adapter<ConversationAdapter.ViewHolder>() {

    private var conversations = listOf<Conversation>()

    fun submitList(list: List<Conversation>) {
        conversations = list
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemConversationBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val conv = conversations[position]
        holder.bind(conv)
        holder.itemView.setOnClickListener { onItemClick(conv) }
    }

    override fun getItemCount() = conversations.size

    inner class ViewHolder(private val binding: ItemConversationBinding) : RecyclerView.ViewHolder(binding.root) {

        fun bind(conv: Conversation) {
            binding.tvName.text = conv.peerName
            binding.tvLastMessage.text = conv.lastMessage
            binding.tvTime.text = formatTime(conv.lastMessageTime)

            if (conv.unreadCount > 0) {
                binding.tvUnread.visibility = View.VISIBLE
                val text = if (conv.unreadCount > 99) "99+" else conv.unreadCount.toString()
                binding.tvUnread.text = text
            } else {
                binding.tvUnread.visibility = View.GONE
            }

            Glide.with(binding.ivAvatar.context)
                .load(conv.peerAvatar)
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