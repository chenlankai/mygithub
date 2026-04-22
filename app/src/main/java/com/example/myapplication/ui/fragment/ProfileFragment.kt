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

        loadUserData()

        // 保存按钮
        binding.btnSave.setOnClickListener {
            saveUserData()
        }

    }


    private fun loadUserData() {
        lifecycleScope.launch {
            //val user = appDatabase.userDao().getUser() ?: getDefaultUser()

            // 给你的所有输入框赋值
            //binding.etUsername.setText(user.username)
            //binding.etPhone.setText(user.phone)
            //binding.etEmail.setText(user.email)
            //binding.etAddress.setText(user.address)
        }
    }

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
            //appDatabase.userDao().insertUser(user)
        }
    }


    private suspend fun getDefaultUser(): User {
        val db = AppDatabase.getInstance(requireContext())
        val user = db.userDao().getFirstUser()
        // 如果数据库为空，返回一个备用的默认用户（可根据需要修改）
        return user ?: User(
            username = "默认用户",
            phone = "00000000000",
            email = "default@example.com",
            address = "默认地址"
        )
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}