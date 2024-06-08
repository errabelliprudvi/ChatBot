package com.example.chatbot

import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import okhttp3.*

class WebSocketClient(private val listener: WebSocketListener){
    private var TAG = "WebSocketClient"
    private val client = OkHttpClient()
    private var webSocket: WebSocket? = null
    private var url: String? = null
    private var reconnectAttempts = 0
    private val maxReconnectAttempts = 5 // Maximum number of reconnection attempts
    private val reconnectInterval = 5L // Reconnection interval in seconds
    private var job: Job? = null

    @OptIn(DelicateCoroutinesApi::class)
    fun connect(url: String) {
        this.url = url
        job = GlobalScope.launch(Dispatchers.IO) {
            val request = Request.Builder().url(url).build()
            webSocket = client.newWebSocket(request, listener)
        }
    }

    fun disconnect() {
        webSocket?.cancel()
    }

    fun send(message: String) {
        webSocket?.send(message)
    }

    private fun reconnect() {
        if (reconnectAttempts < maxReconnectAttempts) {
            // Exponential backoff reconnect strategy
            val delay = reconnectInterval * Math.pow(2.0, reconnectAttempts.toDouble()).toLong()
            Thread.sleep(delay * 1000) // Convert seconds to milliseconds
            connect(url ?: return) // Reconnect
            reconnectAttempts++
        } else {
            // Maximum reconnect attempts reached, handle accordingly
            // For example, notify the user or log the error
        }
    }




}
