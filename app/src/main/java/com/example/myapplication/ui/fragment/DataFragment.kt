package com.example.myapplication.ui.fragment

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import com.example.myapplication.R
import com.example.myapplication.ui.adapter.TableAdapter
import com.example.myapplication.databinding.FragmentDataBinding
import androidx.recyclerview.widget.LinearLayoutManager

class DataFragment : Fragment() {

    private var _binding: FragmentDataBinding? = null
    private val binding get() = _binding!!

    // 将数据改为可变的 MutableList，以便执行删除操作
    private val tableData = mutableListOf(
        listOf("Name", "Age", "City"),   // Header
        listOf("zhangsan", "25", "Beijing"),
        listOf("lisi", "30", "Shanghai"),
        listOf("wangwu", "28", "Guangzhou"),
        listOf("zhaoliu", "22", "Shenzhen"),
        listOf("zhangsan", "25", "Beijing"),
        listOf("lisi", "30", "Shanghai"),
        listOf("wangwu", "28", "Guangzhou"),
        listOf("zhaoliu", "22", "Shenzhen"),
    )



    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentDataBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initTable()
    }

    private fun initTable() {
        // 创建适配器，传入删除回调逻辑
        val adapter = TableAdapter(tableData) { position ->
            if (position > 0) { // 保护逻辑：不删除标题行

                AlertDialog.Builder(this.requireContext())
                    .setTitle("删除记录")
                    .setMessage("确定要删除这条记录吗？")
                    .setPositiveButton("确定") { dialog, _ ->
                        // 执行删除操作
                        tableData.removeAt(position)
                        binding.recyclerViewTable.adapter?.notifyItemRemoved(position)
                        // 更新受影响的索引范围
                        binding.recyclerViewTable.adapter?.notifyItemRangeChanged(position, tableData.size)
                        dialog.dismiss()
                    }
                    .setNegativeButton("取消", null)
                    .show()


            }
        }

        binding.recyclerViewTable.apply {

            layoutManager = LinearLayoutManager(requireContext())
            this.adapter = adapter
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}