package com.example.myapplication

import android.content.Context
import android.net.Uri
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.contract.ActivityResultContracts
import com.bumptech.glide.Glide
import com.example.myapplication.databinding.FragmentProfileBinding

class ProfileFragment : Fragment() {

    private var _binding: FragmentProfileBinding? = null
    private val binding get() = _binding!!


    private val pickImageLauncher = registerForActivityResult(ActivityResultContracts.GetContent()) { uri: Uri? ->
        uri?.let {

            Glide.with(this)
                .load(it)
                .circleCrop()
                .into(binding.ivAvatar)

            saveAvatarUri(it.toString())
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentProfileBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        loadUserData()
        binding.btnSave.setOnClickListener { saveUserData() }
        binding.btnLike.setOnClickListener { toggleLike() }
        binding.btnSetting.setOnClickListener { toggleSetting() }
        binding.ivAvatar.setOnClickListener { toggleAvatar() }
    }

    fun toggleAvatar() {
        // 打开相册选择图片
        pickImageLauncher.launch("image/*")
    }

    fun toggleSetting() {
        Log.d("ProfileFragment", "setting")
    }

    fun toggleLike() {
        Log.d("ProfileFragment", "like")
    }

    fun saveUserData() {
        Log.d("ProfileFragment", "save")
    }

    private fun loadUserData() {
        // 尝试加载已保存的头像，如果没有则显示默认
        val savedUri = getSavedAvatarUri()
        if (savedUri != null) {
            Glide.with(this)
                .load(savedUri)
                .circleCrop()
                .into(binding.ivAvatar)
        } else {
            binding.ivAvatar.setImageResource(R.drawable.ic_my_avatar)
        }
        Log.d("ProfileFragment", "头像加载完成")
    }

    private fun saveAvatarUri(uriString: String) {
        requireContext().getSharedPreferences("user_prefs", Context.MODE_PRIVATE)
            .edit()
            .putString("avatar_uri", uriString)
            .apply()
    }

    private fun getSavedAvatarUri(): String? {
        return requireContext().getSharedPreferences("user_prefs", Context.MODE_PRIVATE)
            .getString("avatar_uri", null)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    companion object {
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            ProfileFragment().apply {
                arguments = Bundle().apply {
                    putString("param1", param1)
                    putString("param2", param2)
                }
            }
    }
}