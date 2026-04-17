package com.example.myapplication

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import com.example.myapplication.databinding.FragmentChatBinding

class ChatFragment : Fragment() {

    private var _binding: FragmentChatBinding? = null
    private val binding get() = _binding!!

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
        initTable()
    }

    private fun initTable() {
        val data = listOf(
            listOf("姓名", "年龄", "城市"),   // 标题行
            listOf("张三", "25", "北京"),
            listOf("李四", "30", "上海"),
            listOf("王五", "28", "广州"),
            listOf("赵六", "22", "深圳"),
            listOf("张三", "25", "北京"),
            listOf("李四", "30", "上海"),
            listOf("王五", "28", "广州"),
            listOf("赵六", "22", "深圳"),
            listOf("张三", "25", "北京"),
            listOf("李四", "30", "上海"),
            listOf("王五", "28", "广州"),
            listOf("赵六", "22", "深圳"),
            listOf("张三", "25", "北京"),
            listOf("李四", "30", "上海"),
            listOf("王五", "28", "广州"),
            listOf("赵六", "22", "深圳"),
        )
        val columnCount = 3

        val adapter = TableAdapter(data, columnCount) { row, col, content ->
            // 在这里实现具体的点击逻辑（输出日志）
            Log.d("ChatFragment", "点击表格项: row=$row, col=$col, content=$content")
        }

        binding.recyclerViewTable.apply {
            layoutManager = GridLayoutManager(requireContext(), columnCount)
            this.adapter = adapter
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}