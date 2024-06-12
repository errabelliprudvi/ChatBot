package com.example.chatbot

import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import okhttp3.*
import okio.ByteString
import java.time.LocalDateTime

class MyWebSocketListener (): WebSocketListener() {
    companion object {

    private var TAG = "MyWebSocketListener"
    private lateinit var websocket: WebSocket

        @OptIn(DelicateCoroutinesApi::class)
        fun sendMes(me:String){
            GlobalScope.launch {
                websocket.send(me)
            }
        }
}
    @RequiresApi(Build.VERSION_CODES.O)
    override fun onOpen(webSocket: WebSocket, response: Response) {
       // super.onOpen(webSocket, response)
        websocket=webSocket
        println("WebSocket Connection Opened")
        Log.d("websocket","WebSocket Connection Opened")
        MVmodel.userName.value?.let { sendMes(it) }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onMessage(webSocket: WebSocket, text: String) {
        //super.onMessage(webSocket, text)
        println("Received Message: $text")
        GlobalScope.launch {
            withContext(Dispatchers.Main) {
                Log.d(TAG, text)
                var lst = text.split("===>", limit = 2)
                var user = lst[0].replace("Message from ", "").trim()

                if (lst.size == 2) {

                    // MVmodel.addMessage(CMessage(lst[1], "text", LocalDateTime.now(), 0))
                    MVmodel.addMessageMap(user, CMessage(lst[1], "text", LocalDateTime.now(), 0))
                    DbOperations.insertMs(user,CMessage(lst[1], "text", LocalDateTime.now(), 0))

                    if (!MVmodel.displayUsers.contains(user)) {
                        MVmodel.addDisplayUser(user)
                        DbOperations.insertUser(user)

                    }
                    if(MVmodel.userName.value!=user){
                        MVmodel.increaseUnSeenMessageCount(user)
                    }


                }
            }
        }

    }

    override fun onMessage(webSocket: WebSocket, bytes: ByteString) {
       // super.onMessage(webSocket, bytes)
        println("Received ByteString Message: ${bytes.hex()}")
        Log.d("websocket"," ${bytes.hex()}")
    }

    override fun onClosing(webSocket: WebSocket, code: Int, reason: String) {
        //super.onClosing(webSocket, code, reason)
        webSocket.close(1000, null)
        println("WebSocket Connection Closing: $code / $reason")
    }

    override fun onFailure(webSocket: WebSocket, t: Throwable, response: Response?) {
       // super.onFailure(webSocket, t, response)
        println("WebSocket Connection Failed: ${t.message}")
        Log.d("websocket","${t.message}")

    }


}