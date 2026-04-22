package com.example.myapplication.ui.activity

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.example.myapplication.ui.activity.MainActivity
import com.example.myapplication.data.database.AppDatabase
import com.example.myapplication.data.model.User
import com.example.myapplication.data.model.UserManager
import com.example.myapplication.databinding.ActivityLoginBinding
import kotlinx.coroutines.launch

class LoginActivity : AppCompatActivity() {

    private lateinit var binding: ActivityLoginBinding
    private var isLoginMode = true

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.btnLogin.setOnClickListener {
            if (isLoginMode) {
                handleLogin()
            } else {
                handleRegister()
            }
        }

        binding.tvSwitchMode.setOnClickListener {
            isLoginMode = !isLoginMode
            updateUI()
        }

        binding.btnSkip.setOnClickListener {
            handleSkipLogin()
        }
    }

    private fun handleSkipLogin() {
        lifecycleScope.launch {
            val db = AppDatabase.getInstance(applicationContext)
            val userDao = db.userDao()
            
            // 1. 先尝试获取zhangsan
            var user = userDao.getUserByUsername("zhangsan")
            
            // 2. 如果没有zhangsan，说明数据库可能被DestructiveMigration清空了，我们直接造一个
            if (user == null) {
                val newUser = User(username = "zhangsan", password = "123456", phone = "19293353407")
                userDao.insertUser(newUser)
                user = userDao.getUserByUsername("zhangsan")
            }
            
            if (user != null) {
                UserManager.login(user)
                Toast.makeText(this@LoginActivity, "测试模式：已登录zhangsan", Toast.LENGTH_SHORT).show()
                val intent = Intent(this@LoginActivity, MainActivity::class.java)
                startActivity(intent)
                finish()
            } else {
                Toast.makeText(this@LoginActivity, "无法创建测试用户，请检查数据库", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun updateUI() {
        if (isLoginMode) {
            binding.tvTitle.text = "欢迎登录"
            binding.btnLogin.text = "登录"
            binding.tvSwitchMode.text = "没有账号？去注册"
        } else {
            binding.tvTitle.text = "账号注册"
            binding.btnLogin.text = "注册"
            binding.tvSwitchMode.text = "已有账号？去登录"
        }
    }

    private fun handleLogin() {
        val username = binding.etUsername.text.toString().trim()
        val password = binding.etPassword.text.toString().trim()

        if (username.isEmpty() || password.isEmpty()) {
            Toast.makeText(this, "请输入用户名和密码", Toast.LENGTH_SHORT).show()
            return
        }

        lifecycleScope.launch {
            val userDao = AppDatabase.getInstance(applicationContext).userDao()
            val user = userDao.getUserByUsername(username)

            if (user != null) {
                // 如果数据库中密码为空，则校验是否为 123456；否则按原密码校验
                val isPasswordCorrect = if (user.password.isEmpty()) {
                    password == "123456"
                } else {
                    user.password == password
                }

                if (isPasswordCorrect) {
                    UserManager.login(user)
                    startActivity(Intent(this@LoginActivity, MainActivity::class.java))
                    finish()
                } else {
                    Toast.makeText(this@LoginActivity, "用户名或密码错误", Toast.LENGTH_SHORT).show()
                }
            } else {
                Toast.makeText(this@LoginActivity, "用户名或密码错误", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun handleRegister() {
        val username = binding.etUsername.text.toString().trim()
        val password = binding.etPassword.text.toString().trim()

        if (username.isEmpty() || password.isEmpty()) {
            Toast.makeText(this, "请输入用户名和密码", Toast.LENGTH_SHORT).show()
            return
        }

        lifecycleScope.launch {
            val userDao = AppDatabase.getInstance(applicationContext).userDao()
            
            // 检查用户名是否已存在
            if (userDao.getUserByUsername(username) != null) {
                Toast.makeText(this@LoginActivity, "用户名已存在", Toast.LENGTH_SHORT).show()
                return@launch
            }

            val newUser = User(username = username, password = password)
            userDao.insertUser(newUser)
            
            Toast.makeText(this@LoginActivity, "注册成功，请登录", Toast.LENGTH_SHORT).show()
            isLoginMode = true
            updateUI()
        }
    }
}