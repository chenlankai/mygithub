package com.example.myapplication.ui.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import okhttp3.*
import okio.ByteString

class ChatViewModel : ViewModel() {

    // 固定连接你的 Mac 服务端
    private val WEBSOCKET_URL = "ws://10.0.2.2:8080"

    private val client = OkHttpClient()
    private var webSocket: WebSocket? = null

    init {
        connectWebSocket()
    }

    private fun connectWebSocket() {
        val request = Request.Builder()
            .url(WEBSOCKET_URL)
            .build()

        webSocket = client.newWebSocket(request, object : WebSocketListener() {
            override fun onOpen(webSocket: WebSocket, response: Response) {
                super.onOpen(webSocket, response)
                Log.d("WebSocket", "✅ 连接聊天服务器成功")
            }

            override fun onMessage(webSocket: WebSocket, text: String) {
                super.onMessage(webSocket, text)
                Log.d("WebSocket", "📩 收到消息：$text")
            }

            override fun onMessage(webSocket: WebSocket, bytes: ByteString) {
                super.onMessage(webSocket, bytes)
            }

            override fun onFailure(webSocket: WebSocket, t: Throwable, response: Response?) {
                super.onFailure(webSocket, t, response)
                Log.e("WebSocket", "❌ 连接失败: ${t.message}")
            }

            override fun onClosed(webSocket: WebSocket, code: Int, reason: String) {
                super.onClosed(webSocket, code, reason)
                Log.d("WebSocket", "🔌 连接关闭")
            }
        })
    }

    // 发送消息
    fun sendMessage(content: String) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                webSocket?.send(content)
                Log.d("WebSocket", "✅ 发送消息：$content")
            } catch (e: Exception) {
                Log.e("WebSocket", "❌ 发送失败", e)
            }
        }
    }

    override fun onCleared() {
        super.onCleared()
        try {
            webSocket?.close(1000, "退出页面")
        } catch (e: Exception) {
            e.printStackTrace()
        }
        webSocket = null
    }
}