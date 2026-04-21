package com.example.myapplication.ui.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import com.example.myapplication.data.database.AppDatabase
import com.example.myapplication.data.model.User
import com.example.myapplication.databinding.FragmentProfileBinding
import kotlinx.coroutines.launch

class ProfileFragment : Fragment() {

    private var _binding: FragmentProfileBinding? = null
    private val binding get() = _binding!!

    // 数据库
    private lateinit var appDatabase: AppDatabase

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentProfileBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // 初始化数据库
        appDatabase = AppDatabase.getInstance(requireActivity().application)

        // ====================== 【核心：初始化所有信息】 ======================
        loadUserData()

        // 保存按钮
        binding.btnSave.setOnClickListener {
            saveUserData()
        }
    }

    // ====================== 加载用户信息，显示到输入框 ======================
    private fun loadUserData() {
        lifecycleScope.launch {
            val user = appDatabase.userDao().getUser() ?: getDefaultUser()

            // 给你的所有输入框赋值
            binding.etUsername.setText(user.username)
            binding.etPhone.setText(user.phone)
            binding.etEmail.setText(user.email)
            binding.etAddress.setText(user.address)
        }
    }

    // ====================== 保存用户信息到数据库 ======================
    private fun saveUserData() {
        val username = binding.etUsername.text.toString().trim()
        val phone = binding.etPhone.text.toString().trim()
        val email = binding.etEmail.text.toString().trim()
        val address = binding.etAddress.text.toString().trim()

        val user = User(
            username = username,
            phone = phone,
            email = email,
            address = address
        )

        lifecycleScope.launch {
            appDatabase.userDao().insertUser(user)
        }
    }

    // ====================== 默认用户数据（第一次打开APP时显示） ======================
    private fun getDefaultUser(): User {
        return User(
            username = "张三",
            phone = "13812345678",
            email = "zhangsan@example.com",
            address = "北京市朝阳区xxx"
        )
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}