package com.example.myapplication

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class MainActivityButton : AppCompatActivity(), View.OnClickListener {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main_button)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        val button2 = findViewById<Button>(R.id.btn2)
        button2.setOnClickListener {
            Toast.makeText(this, "clickBtn2", Toast.LENGTH_SHORT).show()
        }
        val button3 = findViewById<Button>(R.id.btn3)
        val button4 = findViewById<Button>(R.id.btn4)
        button3.setOnClickListener(this)
        button4.setOnClickListener(this)

    }

    fun clickBtn1(view: View) {
        //startActivity(Intent(this, TestMainActivity::class.java))
        Toast.makeText(this, "clickBtn1", Toast.LENGTH_SHORT).show()
    }

    override fun onClick(p0: View) {
        when(p0.id){
            R.id.btn3 ->{
                Toast.makeText(this, "clickBtn3", Toast.LENGTH_SHORT).show()
            }
            R.id.btn4 ->{
                Toast.makeText(this, "clickBtn4", Toast.LENGTH_SHORT).show()
            }
        }
    }
}