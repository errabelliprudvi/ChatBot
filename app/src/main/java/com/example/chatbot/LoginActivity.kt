package com.example.chatbot

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.example.chatbot.ui.theme.ChatBotTheme

class LoginActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            ChatBotTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {

                    LoginScreen(onLoginSuccess = { email, password ->
                        // Save login details in SharedPreferences
                        val sharedPref: SharedPreferences = getSharedPreferences("ChatBot", Context.MODE_PRIVATE)
                        with(sharedPref.edit()) {
                            putString("UserNumber", email)
                            putString("password", password)
                            putString("serverAddress","ws://43.205.24.69:8080/WebChat/chat")
                            apply()
                        }

                        // Navigate to MainActivity
                        val intent = Intent(this, MainActivity::class.java).apply {
                            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                        }
                        startActivity(intent)
                        finish() // Optional: Call finish() if you don't want the user to return to the login screen
                    })
                }
            }
        }
    }
}
