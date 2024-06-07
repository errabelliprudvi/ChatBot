package com.example.chatbot

import android.content.Intent
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.TextUnitType
import androidx.compose.ui.unit.dp
import com.example.chatbot.ui.theme.ChatBotTheme
import com.example.chatbot.ui.theme.Detail
import  androidx.lifecycle.viewmodel.compose.viewModel


@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun MainScreen(){
    Column {
        MainScreenHeader()
        val modifier= Modifier
            .padding(top = 10.dp, start = 5.dp, end = 5.dp)
            .fillMaxWidth(1f)
            .fillMaxHeight(1f)
            .weight(1f)
            .verticalScroll(rememberScrollState(initial = Int.MAX_VALUE))
        MainScreenContainer(modifier = modifier)
    }
}


@Composable
fun MainScreenHeader(){
    Row (modifier= Modifier
        .fillMaxWidth(1f)
        .height(50.dp)
        .background(color = MaterialTheme.colorScheme.onBackground), verticalAlignment = Alignment.CenterVertically){
Text(text = "ChatBot",
    modifier= Modifier
        .weight(.2f)
        .padding(start = 5.dp),
    color = Color.Black,
    fontSize = TextUnit(25f, TextUnitType.Sp),
    fontFamily = FontFamily.Cursive,
    maxLines =1)

        Image(
            painter = painterResource(id =  R.drawable.qrcode),
            //contentScale = ContentScale.Crop,
            modifier = Modifier
                .padding(end = 15.dp)
                .size(30.dp)
                // .clip(CircleShape)
                .clickable {
                },
            contentDescription = stringResource(id = R.string.dog_content_description)
        )

        Image(
            painter = painterResource(id =  R.drawable.camera),
           // contentScale = ContentScale.Crop,
            modifier = Modifier
                .padding(end = 15.dp)
                .size(30.dp)
                // .clip(CircleShape)
                .clickable {
                },
            contentDescription = stringResource(id = R.string.dog_content_description)
        )
        Image(
            painter = painterResource(id =  R.drawable.more),
            // contentScale = ContentScale.Crop,
            modifier = Modifier
                .padding(end = 10.dp)
                .size(30.dp)
                // .clip(CircleShape)
                .clickable {
                },
            contentDescription = stringResource(id = R.string.dog_content_description)
        )
    }

}

@RequiresApi(Build.VERSION_CODES.O)

@Composable
fun MainScreenContainer(modifier: Modifier,) {
    val viewModel = MVmodel
    val showImageOverlay = viewModel.dpStatus.observeAsState()
   val stateValue :Boolean? =showImageOverlay.value
    val users:List<String> = viewModel.displayUsers


    var addUserState by remember { mutableStateOf(false) }
    var text by remember { mutableStateOf("") }





    Box {
        Column(modifier = modifier) {
            // repeat(5) { UserDetailsBox("Prudvi") }
            users.forEach {
                UserDetailsBox(str = it)
            }

        }
        AnimatedVisibility(
            visible = addUserState,
            enter = fadeIn(),
            exit = fadeOut()
        ) {
            Box(
                modifier = Modifier
                    .background(color = Color.White)
                    .pointerInput(Unit) {
                        //detectTapGestures { MVmodel.cnDpStatus(false) }
                    },
                contentAlignment = Alignment.Center
            )
            {
                Column(
                    modifier = Modifier
                        .fillMaxWidth(1f)
                        .height(100.dp),
                ) {
                    Text(
                        text = "Enter UserName to Add",
                        modifier = Modifier
                            .weight(.5f)
                            .padding(start = 5.dp),
                        color = Color.Black,
                        textAlign = TextAlign.Left,
                        fontSize = TextUnit(25f, TextUnitType.Sp),
                        fontFamily = FontFamily.Cursive,
                        fontWeight = FontWeight.Bold,
                        maxLines = 1
                    )

                    BasicTextField(
                        modifier = Modifier
                            .weight(.5f),
                        value = text, onValueChange = { text = it }, singleLine = true,
                        textStyle = TextStyle(
                            color = Color.Black, fontSize = TextUnit(25f, TextUnitType.Sp),
                            fontFamily = FontFamily.Cursive,
                            fontWeight = FontWeight.Bold,
                        ),
                        keyboardOptions = KeyboardOptions(
                            keyboardType = KeyboardType.Text,
                            imeAction = ImeAction.Send
                        ),
                        keyboardActions = KeyboardActions(onSend = {
                            if (!viewModel.displayUsers.contains(text.trim())) {
                                viewModel.addDisplayUser(text.trim())
                            }
                            addUserState = false
                            text = ""
                        })
                    )

                }

            }
        }
        if (stateValue != null) {
            AnimatedVisibility(
                visible = stateValue,
                enter = fadeIn(),
                exit = fadeOut()
            ) {
                Box(
                    modifier = Modifier
                        .background(color = Color.Black.copy(alpha = 0.8f))
                        .pointerInput(Unit) {
                            detectTapGestures { viewModel.cnDpStatus(false) }
                        },
                    contentAlignment = Alignment.Center
                )
                {
                    Image(
                        painter = painterResource(id = R.drawable.chat),
                        contentScale = ContentScale.Crop,
                        modifier = Modifier
                            .size(300.dp)
                            .clip(CircleShape),
                        contentDescription = stringResource(id = R.string.dog_content_description)
                    )

                }
            }
        }

        Image(
            painter = painterResource(id = R.drawable.plus),
            contentScale = ContentScale.Crop,
            modifier = Modifier
                .offset(350.dp, 750.dp)
                .size(55.dp)
                .clip(CircleShape)
                .clickable { addUserState = true },
            contentDescription = stringResource(id = R.string.dog_content_description)
        )
    }

}


@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun UserDetailsBox(str:String){
    //val text by remember { mutableStateOf(str) }
    val viewModel=MVmodel
    val context = LocalContext.current


    Row (modifier = Modifier
        .height(70.dp)
        .padding(5.dp)
        .fillMaxWidth(1f)
        .background(Detail, RoundedCornerShape(5.dp)),
        verticalAlignment = Alignment.CenterVertically

        ) {
        Box() {
        Image(
            painter = painterResource(id = R.drawable.chat),
            contentScale = ContentScale.Crop,
            modifier = Modifier
                .size(55.dp)
                .clip(CircleShape)
                .clickable {
                    viewModel.cnDpStatus(true)
                },
            contentDescription = stringResource(id = R.string.dog_content_description)
        )
    }
        Text(text = str,
            modifier= Modifier
                .weight(.1f)
                .padding(start = 5.dp)
                .clickable {
                    val intent = Intent(context, ChatActivity::class.java)
                    intent.putExtra("UserName", str.trim())
                    context.startActivity(intent)
                },
            color = Color.Black,
            textAlign = TextAlign.Left,
            fontSize = TextUnit(25f, TextUnitType.Sp),
            fontFamily = FontFamily.Cursive,
            fontWeight = FontWeight.Bold,
            maxLines =1)

        Box (modifier = Modifier
            .size(30.dp)
            .background(Color.Transparent, RoundedCornerShape(30.dp))
            .padding(end = 10.dp), Alignment.Center){
            Text(text = "",
                modifier= Modifier
                    .padding(start = 5.dp),
                color = Color.Black,
                fontSize = TextUnit(20f, TextUnitType.Sp),
                fontFamily = FontFamily.Cursive,
                fontWeight = FontWeight.Bold,
                maxLines =1)
        }
    }


}

@RequiresApi(Build.VERSION_CODES.O)
@Composable
@Preview(showBackground = true)
fun DefaultPreview(){
ChatBotTheme {
    Column {
        //LoginScreen()
    }

}
}