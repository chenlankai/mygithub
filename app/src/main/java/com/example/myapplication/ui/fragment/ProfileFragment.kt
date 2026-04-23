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
    private var currentAvatarPath: String = ""

    private val pickImageLauncher = registerForActivityResult(androidx.activity.result.contract.ActivityResultContracts.GetContent()) { uri: android.net.Uri? ->
        uri?.let {
            currentAvatarPath = it.toString()
            com.bumptech.glide.Glide.with(this)
                .load(it)
                .circleCrop()
                .into(binding.ivAvatar)
        }
    }

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

        // 点击头像选择图片
        binding.ivAvatar.setOnClickListener {
            pickImageLauncher.launch("image/*")
        }

        // 保存按钮
        binding.btnSave.setOnClickListener {
            saveUserData()
        }
    }


    private fun loadUserData() {
        val user = com.example.myapplication.data.model.UserManager.currentUser
        // 给输入框赋值
        binding.etUsername.setText(user.username)
        binding.etPhone.setText(user.phone)
        binding.etEmail.setText(user.email)
        binding.etAddress.setText(user.address)
        
        // 加载头像
        currentAvatarPath = user.avatar
        if (currentAvatarPath.isNotEmpty()) {
            com.bumptech.glide.Glide.with(this)
                .load(android.net.Uri.parse(currentAvatarPath))
                .circleCrop()
                .placeholder(com.example.myapplication.R.drawable.ic_default_avatar)
                .into(binding.ivAvatar)
        }
    }

    private fun saveUserData() {
        val currentUserId = com.example.myapplication.data.model.UserManager.currentUser.id
        if (currentUserId == 0) return

        val username = binding.etUsername.text.toString().trim()
        val phone = binding.etPhone.text.toString().trim()
        val email = binding.etEmail.text.toString().trim()
        val address = binding.etAddress.text.toString().trim()

        lifecycleScope.launch {
            val userDao = AppDatabase.getInstance(requireContext()).userDao()
            // 更新数据库，包含头像路径
            userDao.updateUser(currentUserId, username, phone, email, address, currentAvatarPath)
            
            // 获取更新后的用户数据并更新 UserManager
            val updatedUser = userDao.getUserById(currentUserId)
            if (updatedUser != null) {
                com.example.myapplication.data.model.UserManager.login(requireContext(), updatedUser)
                android.widget.Toast.makeText(requireContext(), "保存成功", android.widget.Toast.LENGTH_SHORT).show()
            }
        }
    }


    private suspend fun getDefaultUser(): User {
        val db = AppDatabase.getInstance(requireContext())
        val user = db.userDao().getFirstUser()
        // 如果数据库为空，返回一个备用的默认用户（可根据需要修改）
        return user ?: User(
            username = "defaultuser",
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