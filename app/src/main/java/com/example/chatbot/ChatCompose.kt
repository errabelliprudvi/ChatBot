package com.example.chatbot


import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column


import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
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
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.TextUnitType
import androidx.compose.ui.unit.dp
import androidx.compose.runtime.Composable
import androidx.compose.ui.AbsoluteAlignment
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Shadow
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import com.example.chatbot.ui.theme.ChatBotTheme
import com.example.chatbot.ui.theme.FloatB
import com.example.chatbot.ui.theme.Head2
import com.example.chatbot.ui.theme.KeyPad
import com.example.chatbot.ui.theme.PurpleGrey80
import java.time.LocalDateTime

import androidx.compose.runtime.*

import androidx.lifecycle.asFlow
import java.time.format.DateTimeFormatter

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun ChatScreen(){
    Column(modifier = Modifier
        .background(color = MaterialTheme.colorScheme.background)) {
        ChatHeader()
        val modifier= Modifier
            .padding(top = 10.dp, start = 5.dp, end = 5.dp)
            .fillMaxWidth(1f)
            .weight(1f)
            .verticalScroll(rememberScrollState(initial = Int.MIN_VALUE))
        MiddleContainerDemo(modifier = modifier )
        ChatFooter()
    }
}

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun MiddleContainerDemo(modifier: Modifier){
    val viewModel =MVmodel
    val messagesLiveData = viewModel.messagesHandler[viewModel.userName.value]
    var messagesState by remember { mutableStateOf<List<CMessage>>(emptyList()) }
    var date1 = LocalDateTime.now().toLocalDate()


    //val scrollState = rememberScrollState()

    LaunchedEffect(messagesLiveData) {
        messagesLiveData?.asFlow()?.collect { messages ->
            messagesState = messages
        }
    }

    Column(modifier =modifier) {
        messagesState.forEach {
            if(messagesState[0]==it){
                MVmodel.prems =2
                DaysLbel(s = it)
                date1 = it.date.toLocalDate()
            }
            val result1 = date1.compareTo(it.date.toLocalDate())
            if(result1!=0){
            DaysLbel(s = it)
                date1 = it.date.toLocalDate()
            }

            if(it.inROut==0){
                if(MVmodel.prems==0) {
                    InChatBubble(it)
                }else{
                    InChatBubbleF(it)
                }
            }else{
                if(MVmodel.prems==1) {
                    OutChatBubble(it)
                }else{
                    OutChatBubbleF(it)
                }

            }
            MVmodel.prems = it.inROut
        }
        Log.e("Bluetooth", "MiddleContainer")
    }

}

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun InChatBubbleF(s: CMessage) {
    val formatter = DateTimeFormatter.ofPattern("hh:mm a")
    val time = s.date.format(formatter)

    Row(
        modifier = Modifier
            .fillMaxWidth(1f)
            .padding(end = 90.dp, top = 2.dp, bottom = 2.dp),
        horizontalArrangement = Arrangement.Absolute.Left
    ) {
        Box(contentAlignment = Alignment.BottomStart) {

            Text(
                s.msg,
                color = Color.Black,
                fontSize = TextUnit(15f, TextUnitType.Sp),
                modifier = Modifier
                    .background(
                        Color.Cyan,
                        RoundedCornerShape(topEnd = 20.dp, bottomStart = 20.dp, bottomEnd = 20.dp)
                    )
                    .padding(start = 10.dp, end = 45.dp, top = 5.dp, bottom = 10.dp),
                textAlign = TextAlign.Left,
                fontFamily = FontFamily.Default,
                lineHeight = TextUnit(20f, TextUnitType.Sp)
            )

            Text(
                time,
                modifier = Modifier
                    .padding(start = 10.dp, end = 15.dp),
                color = Color.Black,
                fontSize = TextUnit(10f, TextUnitType.Sp),
                textAlign = TextAlign.End,
                fontFamily = FontFamily.Default,
                lineHeight = TextUnit(10f, TextUnitType.Sp)
            )


        }
    }
}



@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun DaysLbel(s: CMessage) {
    val formatter = DateTimeFormatter.ofPattern("yyyy MMMM dd")
    val time = s.date.format(formatter)

    Row(
        modifier = Modifier
            .fillMaxWidth(1f)
            .padding(5.dp),
        horizontalArrangement = Arrangement.Center
    ) {


            Text(
                time,
                color = MaterialTheme.colorScheme.background,
                fontSize = TextUnit(15f, TextUnitType.Sp),
                modifier = Modifier
                    .background(
                        MaterialTheme.colorScheme.onBackground,
                        RoundedCornerShape(
                            topEnd = 20.dp,
                            bottomStart = 20.dp,
                            bottomEnd = 20.dp,
                            topStart = 20.dp
                        )
                    )
                    .padding(10.dp),
                textAlign = TextAlign.Center,
                fontFamily = FontFamily.Default,
                lineHeight = TextUnit(20f, TextUnitType.Sp)
            )




        }
    }


@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun OutChatBubbleF(s: CMessage) {
    val formatter = DateTimeFormatter.ofPattern("hh:mm a")
    val time = s.date.format(formatter)


    Row(
        modifier = Modifier
            .fillMaxWidth(1f)
            .padding(start = 90.dp, top = 2.dp, bottom = 2.dp),
        horizontalArrangement = Arrangement.Absolute.Right
    ) {

        Box(contentAlignment = Alignment.BottomEnd) {

            Text(
                s.msg,
                color = Color.Black,
                fontSize = TextUnit(15f, TextUnitType.Sp),
                modifier = Modifier
                    .background(
                        PurpleGrey80,
                        RoundedCornerShape(bottomStart = 20.dp, bottomEnd = 20.dp, topStart = 20.dp)
                    )
                    .padding(start = 15.dp, end = 40.dp, top = 5.dp, bottom = 10.dp),
                textAlign = TextAlign.Left,
                fontFamily = FontFamily.Default,
                lineHeight = TextUnit(20f, TextUnitType.Sp)
            )



            Text(
                time,
                modifier = Modifier
                    .padding(top = 20.dp, end = 15.dp),
                color = Color.Black,
                fontSize = TextUnit(10f, TextUnitType.Sp),
                textAlign = TextAlign.End,
                fontFamily = FontFamily.Default,
                lineHeight = TextUnit(10f, TextUnitType.Sp)
            )
            Image(
                painter = painterResource(id = R.drawable.doublecheckmark),

                modifier = Modifier
                    .padding(top = 0.dp, end = 15.dp, bottom = 15.dp)
                    .size(17.dp)
                    .clickable {
                    },
                contentDescription = stringResource(id = R.string.dog_content_description)
            )


        }
    }
}

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun ChatHeader(){
    val viewModel= MVmodel
val username:String = viewModel.userName.value.toString()
    Row(
        modifier = Modifier
            .height(55.dp)
            .fillMaxWidth(1f)
            .background(
                color = Head2,
                RoundedCornerShape(bottomStart = 10.dp, bottomEnd = 10.dp)
            )

    ) {
        Row(modifier = Modifier
            .weight(.1f)
            .size(55.dp,),
            verticalAlignment = Alignment.CenterVertically){
            Image(painter = painterResource(id = R.drawable.arrowleft),
                modifier = Modifier.size(30.dp),
                contentDescription ="Left Arrow" )
            Image(
                painter = painterResource(id = R.drawable.chat),
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .size(50.dp)
                    .clip(CircleShape)
                    .clickable {
                    },
                contentDescription = stringResource(id = R.string.dog_content_description)
            )

        }
        Row(modifier = Modifier
            .padding(start = 0.dp)
            .weight(.2f),
          verticalAlignment = Alignment.Bottom
        ){
            Column(modifier = Modifier.fillMaxSize(1f),
                verticalArrangement = Arrangement.Center, horizontalAlignment = AbsoluteAlignment.Left) {
                Text(
                    text=username,
                    modifier = Modifier.fillMaxWidth(1f),
                    color = Color.Black,
                    fontSize = TextUnit(25f, TextUnitType.Sp),
                    fontFamily = FontFamily.Default,
                    maxLines =1,

                    )
            }

        }
        Row(modifier = Modifier.size(50.dp),
            verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.Center) {
            Image(
                painter = painterResource(id = R.drawable.telecommunication),
                modifier = Modifier
                    .size(30.dp)
                    .clickable {
                    },
                contentDescription = stringResource(id = R.string.dog_content_description)
            )
        }
        Row(modifier = Modifier.size(50.dp),
            verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.Center) {
            Image(
                painter = painterResource(id = R.drawable.video),

                modifier = Modifier
                    .size(30.dp)
                    .clickable {
                    },
                contentDescription = stringResource(id = R.string.dog_content_description)
            )
        }
        Row(modifier = Modifier
            .fillMaxHeight(1f)
            .size(30.dp),
            verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.Center) {
            Image(
                painter = painterResource(id = R.drawable.more),

                modifier = Modifier
                    .size(30.dp)
                    .clickable {
                    },
                contentDescription = stringResource(id = R.string.dog_content_description)
            )
        }

    }

}

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun InChatBubble(s: CMessage){
    val formatter = DateTimeFormatter.ofPattern("hh:mm a")
    val time = s.date.format(formatter)

    Row(
        modifier = Modifier
            .fillMaxWidth(1f)
            .padding(end = 90.dp, top = 2.dp, bottom = 2.dp),
        horizontalArrangement = Arrangement.Absolute.Left
    ) {
        Box(contentAlignment = Alignment.BottomStart) {

            Text(
                s.msg,
                color = Color.Black,
                fontSize = TextUnit(15f, TextUnitType.Sp),
                modifier = Modifier
                    .background(
                        Color.Cyan,
                        RoundedCornerShape(
                            topEnd = 20.dp,
                            bottomStart = 20.dp,
                            bottomEnd = 20.dp,
                            topStart = 20.dp
                        )
                    )
                    .padding(start = 10.dp, end = 45.dp, top = 5.dp, bottom = 10.dp),
                textAlign = TextAlign.Left,
                fontFamily = FontFamily.Default,
                lineHeight = TextUnit(20f, TextUnitType.Sp)
            )

            Text(
                time,
                modifier = Modifier
                    .padding(start = 10.dp, end = 15.dp),
                color = Color.Black,
                fontSize = TextUnit(10f, TextUnitType.Sp),
                textAlign = TextAlign.End,
                fontFamily = FontFamily.Default,
                lineHeight = TextUnit(10f, TextUnitType.Sp)
            )


        }
    }

}


@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun OutChatBubble(s:CMessage) {
    val formatter = DateTimeFormatter.ofPattern("hh:mm a")
    val time = s.date.format(formatter)


    Row(
        modifier = Modifier
            .fillMaxWidth(1f)
            .padding(start = 90.dp, top = 2.dp, bottom = 2.dp),
        horizontalArrangement = Arrangement.Absolute.Right
    ) {

        Box(contentAlignment = Alignment.BottomEnd) {

            Text(
                s.msg,
                color = Color.Black,
                fontSize = TextUnit(15f, TextUnitType.Sp),
                modifier = Modifier
                    .background(
                        PurpleGrey80,
                        RoundedCornerShape(
                            bottomStart = 20.dp,
                            bottomEnd = 20.dp,
                            topStart = 20.dp,
                            topEnd = 20.dp
                        )
                    )
                    .padding(start = 15.dp, end = 40.dp, top = 5.dp, bottom = 10.dp),
                textAlign = TextAlign.Left,
                fontFamily = FontFamily.Default,
                lineHeight = TextUnit(20f, TextUnitType.Sp)
            )



            Text(
                time,
                modifier = Modifier
                    .padding(start = 20.dp, end = 15.dp),
                color = Color.Black,
                fontSize = TextUnit(10f, TextUnitType.Sp),
                textAlign = TextAlign.End,
                fontFamily = FontFamily.Default,
                lineHeight = TextUnit(10f, TextUnitType.Sp)
            )
            Image(
                painter = painterResource(id = R.drawable.doublecheckmark),

                modifier = Modifier
                    .padding(top = 0.dp, end = 15.dp, bottom = 15.dp)
                    .size(17.dp)
                    .clickable {
                    },
                contentDescription = stringResource(id = R.string.dog_content_description)
            )


        }
    }
    }









@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun ChatFooter() {
    val viewModel= MVmodel
    var text by remember { mutableStateOf("") }
   // val viewModel = LocalContext.current as Application

    val brush = remember {
        Brush.sweepGradient(
            listOf(
                Color(0xFF9575CD),
                Color(0xFFBA68C8),
                Color(0xFFE57373),
                Color(0xFFFFB74D),
                Color(0xFFFFF176),
                Color(0xFFAED581),
                Color(0xFF4DD0E1),
                Color(0xFF9575CD)
            )
        )
    }
    Row(
        modifier = Modifier
            .fillMaxWidth(1f)
            .padding(2.dp)
            .height(55.dp)
            .background(color = Color.Transparent, shape = CircleShape)
        , verticalAlignment = Alignment.CenterVertically
    ) {
        Row (modifier = Modifier
            .weight(.5f)
            .padding(end = 5.dp)
            .background(KeyPad, RoundedCornerShape(50.dp))
            ,
            verticalAlignment = Alignment.CenterVertically){
            Box(modifier = Modifier.size(40.dp,50.dp),
                contentAlignment = Alignment.Center){

                Image(
                    painter = painterResource(id = R.drawable.emoji),
                    contentScale = ContentScale.Crop,
                    modifier = Modifier
                        .size(25.dp)
                        // .clip(CircleShape)
                        .clickable {

                        },
                    contentDescription = stringResource(id = R.string.dog_content_description)
                )

            }
            BasicTextField(
                modifier = Modifier
                    .weight(1f)
                ,
                value = text, onValueChange = { text = it }, singleLine = true,
                textStyle = TextStyle(color= Color.White,fontSize =TextUnit(20f, TextUnitType.Sp), shadow = Shadow(
                    Color.Black, Offset.Infinite,.5f)),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Text, imeAction = ImeAction.Send),
                keyboardActions = KeyboardActions (onSend = {
                    if(text.trim().isNotEmpty()) {
                        viewModel.userName.value?.let { //viewModel.sendMessage("$it:$text")
                            MyWebSocketListener.sendMes("$it:$text")
                        }
                        // MVmodel.addMessage(CMessage(text,"text", LocalDateTime.now(),1))
                        viewModel.userName.value?.let {
                            viewModel.addMessageMap(
                                it,
                                CMessage(text, "text", LocalDateTime.now(), 1)
                            )
                            DbOperations.insertMs(it,CMessage(text, "text", LocalDateTime.now(), 1))
                        }
                        text = ""
                    }
                })
            )

            Box(modifier = Modifier.size(40.dp,50.dp),
                contentAlignment = Alignment.Center){
                Image(
                    painter = painterResource(id = R.drawable.attachment2),
                    contentScale = ContentScale.Crop,
                    modifier = Modifier
                        .size(25.dp)
                        // .clip(CircleShape)
                        .clickable {

                        },
                    contentDescription = stringResource(id = R.string.dog_content_description)
                )
            }

        }


        Box(
            modifier = Modifier
                .size(50.dp)
                .background(color = FloatB, shape = CircleShape),
            contentAlignment = Alignment.Center
        ) {
            Image(
                painter = painterResource(id = if(text.isEmpty()) R.drawable.voice else R.drawable.send),
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .size(25.dp)
                    // .clip(CircleShape)
                    .clickable {
                        if (text
                                .trim()
                                .isNotEmpty()
                        ) {
                            viewModel.userName.value?.let { Log.d("ChatSend.....UserName", it) }
                            viewModel.userName.value?.let {
                                // viewModel.sendMessage("$it:$text")
                                MyWebSocketListener.sendMes("$it:$text")
                            }
                            // MVmodel.addMessage(CMessage(text, "text", LocalDateTime.now(), 1))
                            viewModel.userName.value?.let {
                                viewModel.addMessageMap(
                                    it,
                                    CMessage(text, "text", LocalDateTime.now(), 1)
                                )
                                DbOperations.insertMs(
                                    it,
                                    CMessage(text, "text", LocalDateTime.now(), 1)
                                )
                            }
                            text = ""
                        }
                    },
                contentDescription = stringResource(id = R.string.dog_content_description)
            )
        }


    }
    Spacer(modifier = Modifier
        .fillMaxWidth(1f)
        .padding(bottom = 15.dp)
        .background(Color.Transparent))
}

@RequiresApi(Build.VERSION_CODES.O)
@Preview(showBackground = true)
@Composable
fun Preview(){
    ChatBotTheme {
        Column (modifier=Modifier.fillMaxSize(1f)){
            ChatScreen()
        }

    }

}