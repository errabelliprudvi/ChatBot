package com.example.chatbot

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

@Composable
fun DpView() {
    Box(modifier = Modifier, contentAlignment = Alignment.Center ) {
        Image(
            painter = painterResource(id = R.drawable.chat),
            contentScale = ContentScale.Crop,
            modifier = Modifier
                .size(300.dp)
                .clip(CircleShape)
                .clickable {
                },
            contentDescription = stringResource(id = R.string.dog_content_description)
        )
    }

}


@Composable
@Preview(showBackground = true)
fun Demo(){
    Column (modifier=Modifier.fillMaxSize(1f)){
        DpView()
    }

}
