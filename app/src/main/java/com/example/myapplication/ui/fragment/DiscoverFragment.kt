package com.example.myapplication.ui.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.myapplication.data.model.ItemData
import com.example.myapplication.ui.adapter.ListAdapter
import com.example.myapplication.databinding.FragmentDiscoverBinding

class DiscoverFragment : Fragment() {

    private var _binding: FragmentDiscoverBinding? = null
    private val binding get() = _binding!!

    private lateinit var adapter: ListAdapter
    private val dataList = mutableListOf(
        ItemData("标题1", "这是副标题1"),
        ItemData("标题2", "这是副标题2"),
        ItemData("标题3", "这是副标题3"),
        ItemData("标题4", "这是副标题4"),
        ItemData("标题5", "这是副标题5"),
        ItemData("标题6", "这是副标题6"),
        ItemData("标题7", "这是副标题7"),
        ItemData("标题8", "这是副标题8"),
        ItemData("标题9", "这是副标题9"),
        ItemData("标题10", "这是副标题10"),
        ItemData("标题11", "这是副标题11"),
        ItemData("标题12", "这是副标题12")
    )

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentDiscoverBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initList()

    }

    private fun initList() {
        binding.recyclerView.layoutManager = LinearLayoutManager(requireContext())
        adapter = ListAdapter(dataList) { action, position ->
            when (action) {
                "share" -> Toast.makeText(requireContext(), "分享第${position + 1}项", Toast.LENGTH_SHORT).show()
                "favorite" -> Toast.makeText(requireContext(), "收藏第${position + 1}项", Toast.LENGTH_SHORT).show()
                "delete" -> {
                    dataList.removeAt(position)            // 删除数据
                    adapter.notifyItemRemoved(position)           // 触发删除动画
                    adapter.notifyItemRangeChanged(position, dataList.size - position)
                    Toast.makeText(requireContext(), "已删除", Toast.LENGTH_SHORT).show()
                }
            }
        }
        binding.recyclerView.adapter = adapter
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
