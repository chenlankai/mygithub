package com.example.myapplication
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.myapplication.databinding.ItemListBinding

class ListAdapter(private val items: List<ItemData>) : RecyclerView.Adapter<ListAdapter.ViewHolder>() {

    class ViewHolder(val binding: ItemListBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemListBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = items[position]
        holder.binding.tvTitle.text = item.title
        holder.binding.tvSubtitle.text = item.subtitle
        holder.itemView.setOnClickListener {
            Log.d("ListAdapter", "Item clicked: ${item.title}")
            // 处理点击事件
        }
        holder.binding.btnMore.setOnClickListener {
            Log.d("ListAdapter", "More button clicked: ${item.title}")
            // 处理更多按钮点击事件
        }
    }

    override fun getItemCount() = items.size
}