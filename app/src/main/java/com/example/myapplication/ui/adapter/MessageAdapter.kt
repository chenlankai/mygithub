package com.example.myapplication.ui.adapter

// 放在 adapter 包下，例如 com.example.myapplication.ui.adapter.MessageAdapter
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.myapplication.R
import com.example.myapplication.databinding.ItemMessageMineBinding
import com.example.myapplication.databinding.ItemMessageOtherBinding
import com.example.myapplication.data.model.Message
import java.text.SimpleDateFormat
import java.util.*

class MessageAdapter : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    companion object {
        private const val TYPE_MINE = 0
        private const val TYPE_OTHER = 1
    }

    private var messages = listOf<Message>()

    fun submitList(list: List<Message>) {
        messages = list
        notifyDataSetChanged()
    }

    override fun getItemViewType(position: Int): Int {
        return if (messages[position].isMine) TYPE_MINE else TYPE_OTHER
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when (viewType) {
            TYPE_MINE -> {
                val binding = ItemMessageMineBinding.inflate(LayoutInflater.from(parent.context), parent, false)
                MineViewHolder(binding)
            }
            else -> {
                val binding = ItemMessageOtherBinding.inflate(LayoutInflater.from(parent.context), parent, false)
                OtherViewHolder(binding)
            }
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val message = messages[position]
        when (holder) {
            is MineViewHolder -> holder.bind(message)
            is OtherViewHolder -> holder.bind(message)
        }
    }

    override fun getItemCount() = messages.size

    inner class MineViewHolder(private val binding: ItemMessageMineBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(message: Message) {
            binding.tvContent.text = message.content
            binding.tvTime.text = formatTime(message.timestamp)
        }
    }

    inner class OtherViewHolder(private val binding: ItemMessageOtherBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(message: Message) {
            binding.tvContent.text = message.content
            binding.tvTime.text = formatTime(message.timestamp)
        }
    }

    private fun formatTime(timestamp: Long): String {
        val sdf = SimpleDateFormat("HH:mm", Locale.getDefault())
        return sdf.format(Date(timestamp))
    }
}