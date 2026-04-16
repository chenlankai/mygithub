package com.example.myapplication

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import com.example.myapplication.databinding.ActivityLoginBinding

class LoginActivity : AppCompatActivity() {

    private lateinit var binding: ActivityLoginBinding

    // 1. 定义并注册 ActivityResultLauncher
    private val registerLauncher: ActivityResultLauncher<Intent> =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == Activity.RESULT_OK) {
                // 接收从 RegisterActivity 回传的数据
                val data = result.data
                val username = data?.getStringExtra("username")
                val password = data?.getStringExtra("password")

                // 自动填充到登录框，方便用户直接登录
                binding.etUsername.setText(username)
                binding.etPassword.setText(password)
                
                Toast.makeText(this, "注册信息已同步，请登录", Toast.LENGTH_SHORT).show()
            }
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.btnLogin.setOnClickListener {
            val username = binding.etUsername.text.toString()
            val password = binding.etPassword.text.toString()

            if (username.isEmpty() || password.isEmpty()) {
                Toast.makeText(this, "请输入完整信息", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            // 依然对比全局变量中的数据
            val savedUsername = MyApplication.registeredUsername
            val savedPassword = MyApplication.registeredPassword

            if (username == savedUsername && password == savedPassword && savedUsername != null) {
                Toast.makeText(this, "登录成功", Toast.LENGTH_SHORT).show()
                val intent = Intent(this, MainActivity::class.java)
                startActivity(intent)
                finish()
            } else {
                Toast.makeText(this, "账号或密码错误", Toast.LENGTH_SHORT).show()
            }
        }

        binding.btnRegister.setOnClickListener {
            // 2. 使用 Launcher 启动注册页面
            val intent = Intent(this, RegisterActivity::class.java)
            registerLauncher.launch(intent)
        }
    }
}