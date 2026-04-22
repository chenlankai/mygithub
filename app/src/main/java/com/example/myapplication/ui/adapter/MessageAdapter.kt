package com.example.myapplication.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.myapplication.R
import com.example.myapplication.data.model.Message
import com.example.myapplication.data.model.UserManager
import com.example.myapplication.databinding.ItemMessageLeftBinding
import com.example.myapplication.databinding.ItemMessageRightBinding
import java.text.SimpleDateFormat
import java.util.*

class MessageAdapter : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private var messages: List<Message> = emptyList()
    private var otherAvatar: String = ""

    companion object {
        private const val VIEW_TYPE_SENT = 1
        private const val VIEW_TYPE_RECEIVED = 2
    }

    fun setMessages(newMessages: List<Message>, otherAvatarPath: String) {
        this.messages = newMessages
        this.otherAvatar = otherAvatarPath
        notifyDataSetChanged()
    }

    override fun getItemViewType(position: Int): Int {
        val message = messages[position]
        return if (message.senderId == UserManager.currentUser.id.toString()) {
            VIEW_TYPE_SENT
        } else {
            VIEW_TYPE_RECEIVED
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return if (viewType == VIEW_TYPE_SENT) {
            val binding = ItemMessageRightBinding.inflate(LayoutInflater.from(parent.context), parent, false)
            SentViewHolder(binding)
        } else {
            val binding = ItemMessageLeftBinding.inflate(LayoutInflater.from(parent.context), parent, false)
            ReceivedViewHolder(binding)
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val message = messages[position]
        if (holder is SentViewHolder) {
            holder.bind(message)
        } else if (holder is ReceivedViewHolder) {
            holder.bind(message, otherAvatar)
        }
    }

    override fun getItemCount(): Int = messages.size

    class SentViewHolder(private val binding: ItemMessageRightBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(message: Message) {
            binding.tvContent.text = message.content
            binding.tvTime.text = formatTime(message.timestamp)
            Glide.with(binding.ivAvatar).load(UserManager.currentUser.avatar).circleCrop().placeholder(R.drawable.ic_default_avatar).into(binding.ivAvatar)
        }
    }

    class ReceivedViewHolder(private val binding: ItemMessageLeftBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(message: Message, avatar: String) {
            binding.tvContent.text = message.content
            binding.tvTime.text = formatTime(message.timestamp)
            Glide.with(binding.ivAvatar).load(avatar).circleCrop().placeholder(R.drawable.ic_default_avatar).into(binding.ivAvatar)
        }
    }
}

private fun formatTime(timestamp: Long): String {
    val sdf = SimpleDateFormat("HH:mm", Locale.getDefault())
    return sdf.format(Date(timestamp))
}