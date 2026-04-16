package com.example.myapplication

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.myapplication.databinding.ActivityRegisterBinding

class RegisterActivity : AppCompatActivity() {

    private lateinit var binding: ActivityRegisterBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRegisterBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // 注册按钮点击事件
        binding.btnDoRegister.setOnClickListener {
            val username = binding.etRegUsername.text.toString()
            val password = binding.etRegPassword.text.toString()
            val confirm = binding.etRegPasswordConfirm.text.toString()

            if (username.isEmpty() || password.isEmpty()) {
                Toast.makeText(this, "请输入完整信息", Toast.LENGTH_SHORT).show()
            } else if (password != confirm) {
                Toast.makeText(this, "两次输入的密码不一致", Toast.LENGTH_SHORT).show()
            } else {
                // 1. 保存到全局变量 (依然保留，作为全局数据池)
                MyApplication.registeredUsername = username
                MyApplication.registeredPassword = password
                
                // 2. 使用 Intent 回传数据给 LoginActivity
                val intent = android.content.Intent()
                intent.putExtra("username", username)
                intent.putExtra("password", password)
                setResult(android.app.Activity.RESULT_OK, intent)

                Toast.makeText(this, "注册成功！", Toast.LENGTH_SHORT).show()
                finish()
            }
        }

        // 返回登录按钮点击事件
        binding.btnBackToLogin.setOnClickListener {
            finish()
        }
    }
}