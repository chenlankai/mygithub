package com.example.myapplication

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.myapplication.databinding.FragmentDiscoverBinding

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [DiscoverFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class DiscoverFragment : Fragment() {
        // TODO: Rename and change types of parameters
        private var param1: String? = null
        private var param2: String? = null
        private var _binding: FragmentDiscoverBinding?=null
        private val binding get() = _binding!!

        override fun onCreate(savedInstanceState: Bundle?) {
            super.onCreate(savedInstanceState)
            arguments?.let {
                param1 = it.getString(ARG_PARAM1)
                param2 = it.getString(ARG_PARAM2)
            }
        }

        override fun onCreateView(
            inflater: LayoutInflater, container: ViewGroup?,
            savedInstanceState: Bundle?
        ): View? {
            // Inflate the layout for this fragment
            _binding = FragmentDiscoverBinding.inflate(inflater, container, false)
            return binding.root
            //return inflater.inflate(R.layout.fragment_discover, container, false)
        }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initList()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
    private fun initList(){
        // 准备数据
        val dataList = listOf(
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
            ItemData("标题12", "这是副标题12"),
        )

        // 设置布局管理器（线性布局）
        binding.recyclerView.layoutManager = LinearLayoutManager(requireContext())
        // 设置适配器
        binding.recyclerView.adapter = MyAdapter(dataList)

        // 显式开启嵌套滚动，解决在 ViewPager2 中可能出现的滑动冲突
        binding.recyclerView.isNestedScrollingEnabled = true
    }
    companion object {
            /**
             * Use this factory method to create a new instance of
             * this fragment using the provided parameters.
             *
             * @param param1 Parameter 1.
             * @param param2 Parameter 2.
             * @return A new instance of fragment DiscoverFragment.
             */
            // TODO: Rename and change types and number of parameters
            @JvmStatic
            fun newInstance(param1: String, param2: String) =
                DiscoverFragment().apply {
                    arguments = Bundle().apply {
                        putString(ARG_PARAM1, param1)
                        putString(ARG_PARAM2, param2)
                    }
                }
        }
}