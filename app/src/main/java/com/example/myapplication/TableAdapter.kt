package com.example.myapplication

import android.graphics.Color
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.myapplication.databinding.ItemTableRowBinding

class TableAdapter(
    private var data: MutableList<List<String>>, // 改为 MutableList 方便删除
    private val onDeleteClick: (position: Int) -> Unit
) : RecyclerView.Adapter<TableAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemTableRowBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val rowData = data[position]
        val isTitle = position == 0
        holder.bind(rowData, isTitle, position)

    }

    override fun getItemCount(): Int = data.size

    inner class ViewHolder(private val binding: ItemTableRowBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(rowData: List<String>, isTitle: Boolean, position: Int) {
            binding.tvName.text = rowData.getOrNull(0) ?: ""
            binding.tvAge.text = rowData.getOrNull(1) ?: ""
            binding.tvCity.text = rowData.getOrNull(2) ?: ""

            if (isTitle) {
                binding.root.setBackgroundColor(Color.LTGRAY)
                binding.btnDelete.visibility = android.view.View.INVISIBLE // 标题行不显示删除
            } else {
                binding.root.setBackgroundColor(Color.WHITE)
                binding.btnDelete.visibility = android.view.View.VISIBLE
                binding.btnDelete.setOnClickListener {
                    onDeleteClick(position)
                }
            }
        }
    }
}