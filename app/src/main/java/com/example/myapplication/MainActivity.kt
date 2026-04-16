package com.example.myapplication

import android.os.Bundle
import android.util.Log
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.listeners.OnFragmentInteractionListener


class MainActivity : AppCompatActivity() , OnFragmentInteractionListener{
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)
        if (savedInstanceState == null){
            supportFragmentManager.beginTransaction()//直接替换，也就是清空所有的
                .replace(R.id.top_container, MyFragmentDemo1())
                .commit()
            supportFragmentManager.beginTransaction()// 替换并添加到返回栈（支持返回）
                .replace(R.id.top_container, AnotherFragment())
                .addToBackStack(null)
                .commit()
            supportFragmentManager.beginTransaction() //删除指定的fragment
                .remove(MyFragmentDemo1())
                .commit()
            supportFragmentManager.popBackStack()  //弹出栈顶的fragment

        }

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
    }

    override fun onFragmentInteraction(data: String) {
        Log.d("MainActivity","收到来自SecondFragment数据：$data")
    }
}