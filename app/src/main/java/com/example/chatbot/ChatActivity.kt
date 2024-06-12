package com.example.chatbot

import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.example.chatbot.ui.theme.ChatBotTheme

class ChatActivity : ComponentActivity() {

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        val mainViewModel = MVmodel
        super.onCreate(savedInstanceState)
        val userName = intent.getStringExtra("UserName")
        if (userName != null) {
            mainViewModel.adduserName(userName.trim())
           // mainViewModel.readMessage()
            mainViewModel.clearUnSeenMessageCount(userName.trim())

        }
        setContent {
            ChatBotTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {

                    ChatScreen()
                }
            }
        }
    }
}


