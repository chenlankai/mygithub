package com.example.myapplication

import android.graphics.Color
import android.graphics.Typeface
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.myapplication.databinding.ItemTableCellBinding

class TableAdapter(
    private val data: List<List<String>>,        // 二维数据，第一行为标题
    private val columnCount: Int,
    private val onItemClick: (row: Int, col: Int, content: String) -> Unit   // 新增回调
) : RecyclerView.Adapter<TableAdapter.ViewHolder>() {

    private val flatList = data.flatten()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemTableCellBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val text = flatList[position]
        val isTitle = position < columnCount
        holder.bind(text, isTitle)

        // 计算行列
        val row = position / columnCount
        val col = position % columnCount

        // 设置点击事件（标题行不触发点击，可自行修改）
        holder.itemView.setOnClickListener {
            if (!isTitle) {
                onItemClick(row, col, text)
            }
        }
    }

    override fun getItemCount(): Int = flatList.size

    inner class ViewHolder(private val binding: ItemTableCellBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(text: String, isTitle: Boolean) {
            binding.textCell.text = text
            if (isTitle) {
                binding.textCell.setTypeface(null, Typeface.BOLD)
                binding.textCell.setBackgroundColor(Color.LTGRAY)
            } else {
                binding.textCell.setTypeface(null, Typeface.NORMAL)
                binding.textCell.setBackgroundColor(Color.WHITE)
            }
        }
    }
}