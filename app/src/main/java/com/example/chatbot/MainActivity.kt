package com.example.chatbot

import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import com.example.chatbot.ui.theme.ChatBotTheme
import okhttp3.*
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.WebSocket

class MainActivity : ComponentActivity() {
private lateinit var webSocket: WebSocket
private val client =OkHttpClient()

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)


        // Check if email and password are stored in SharedPreferences
        val sharedPref = getSharedPreferences("ChatBot", Context.MODE_PRIVATE)
        val userName = sharedPref.getString("UserNumber", null)
        val password = sharedPref.getString("password", null)
        Log.d("MainActivity",userName.toString())
        if (userName.isNullOrEmpty() || password.isNullOrEmpty()) {
            // If email or password is not available, navigate to LoginActivity
            val intent = Intent(this, LoginActivity::class.java).apply {
                flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            }
            startActivity(intent)
            finish()
            return
        }
        val mainViewModel = MVmodel
        mainViewModel.adduserName(userName.trim())
        //mainViewModel.load(userName.trim())


        val request =Request.Builder().url("ws://13.200.235.181:8080/WebChat/chat").build()
        val listener =MyWebSocketListener()
        webSocket = client.newWebSocket(request, listener)

        setContent {
            ChatBotTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                MainScreen()
                }
            }
        }



    }



}












