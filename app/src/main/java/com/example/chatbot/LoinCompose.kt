package com.example.chatbot

import android.content.Context
import androidx.compose.animation.animateColor
import androidx.compose.animation.core.*
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.delay

@Composable
fun LoginScreen(onLoginSuccess: (String, String) -> Unit) {
    var email by remember { mutableStateOf(TextFieldValue("")) }
    var password by remember { mutableStateOf(TextFieldValue("")) }
    var isLoading by remember { mutableStateOf(false) }
    var context = LocalContext.current

    val transition = rememberInfiniteTransition()
    val backgroundColor by transition.animateColor(
        initialValue = Color(0xFF2196F3),
        targetValue = Color(0xFF1976D2),
        animationSpec = infiniteRepeatable(
            animation = tween(2000),
            repeatMode = RepeatMode.Reverse
        )
    )

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(backgroundColor)
            .padding(16.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "Welcome Back!",
            fontSize = 24.sp,
            color = Color.White,
            modifier = Modifier.padding(bottom= 16.dp),
            textAlign = TextAlign.Center
        )

        OutlinedTextField(
            value = email,
            onValueChange = { email = it },
            label = { Text("Mobile Number") },
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom=8.dp),
        singleLine = true
        )

        OutlinedTextField(
            value = password,
            onValueChange = { password = it },
            label = { Text("Password") },
            visualTransformation = PasswordVisualTransformation(),
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom= 16.dp),
        singleLine = true
        )

        Button(
            onClick = {
                isLoading = true
            },
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(8.dp)),
            colors = ButtonDefaults.buttonColors( Color.White)
        ) {
            Text(
                text = if (isLoading) "Loading..." else "Login",
                color = Color(0xFF1976D2),
                modifier = Modifier
                    .padding(vertical = 8.dp)
            )
        }

        if (isLoading) {
            LaunchedEffect(isLoading) {
                val sharedPref = context.getSharedPreferences("ChatBot", Context.MODE_PRIVATE)
                with(sharedPref.edit()) {
                    putString("key", "value")
                    apply()
                }
                delay(2000) // Simulate network request
                isLoading = false
                onLoginSuccess(email.text,password.text)
            }
        }
    }
}
