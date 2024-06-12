package com.example.chatbot

import android.app.Application
import android.bluetooth.BluetoothDevice
import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateMapOf
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.BufferedReader
import java.io.IOException
import java.io.InputStreamReader
import java.io.PrintWriter
import java.net.InetSocketAddress
import java.net.Socket
import java.time.LocalDateTime

@RequiresApi(Build.VERSION_CODES.O)
class MVmodel: ViewModel() {


  /*  init {
        if (!isSocketInitialized) {
            GlobalScope.launch(Dispatchers.IO) {
                try {
                    socket = Socket().apply {
                        connect(InetSocketAddress(SERVER_ADDRESS, SERVER_PORT), 1080000)
                    }
                    // Call methods to send and receive messages here
                    isSocketInitialized = true
                    readMessage()

                    Log.d(TAG, "socket intialization")
                } catch (e: IOException) {
                    e.printStackTrace()
                    Log.d(TAG, "socket intialization failed }" + e.message)
                }
            }

        }
    }*/

    companion object {



        private const val SERVER_ADDRESS = "13.200.235.181"
        private const val SERVER_PORT = 12345
        private const val TAG = "MVmodel"
        private lateinit var socket: Socket
        private var isSocketInitialized: Boolean = false
        lateinit var chatUser: String


        @RequiresApi(Build.VERSION_CODES.O)
        private val _messages: MutableList<CMessage> = mutableStateListOf()
        private val _displayUsers: MutableList<String> = mutableStateListOf()
        private val _sebMs: MutableList<String> = mutableStateListOf()
        private val _deviceList: MutableList<BluetoothDevice> = mutableStateListOf()
        private val _userName = MutableLiveData<String>("")
        private val _dpStatus = MutableLiveData<Boolean>(false)
        private val _messagesHandler: MutableMap<String, MutableLiveData<List<CMessage>>> = mutableStateMapOf()
        val messagesHandler: Map<String, LiveData<List<CMessage>>> = _messagesHandler

        private val _unseenMessagesCount: MutableMap<String, MutableLiveData<Int>> = mutableStateMapOf()
        val unseenMessagesCount: Map<String, LiveData<Int>> get() = _unseenMessagesCount


        /* private val _messagesHandler: MutableMap<String,MutableList<CMessage>> = mutableStateMapOf()
          var messagesHandler: Map<String,List<CMessage>>  = _messagesHandler
*/


        @RequiresApi(Build.VERSION_CODES.O)
        var messages: List<CMessage> = _messages
        var displayUsers: List<String> = _displayUsers
        var sebMs: MutableList<String> = _sebMs
        var deviceList: List<BluetoothDevice> = _deviceList
        val dpStatus: LiveData<Boolean> get() = _dpStatus
        val userName: LiveData<String> get() = _userName

        fun load(user:String){
            if (!isSocketInitialized) {
                GlobalScope.launch(Dispatchers.IO) {
                    try {
                        socket = Socket().apply {
                            connect(InetSocketAddress(SERVER_ADDRESS, SERVER_PORT), 1080000)
                        }
                        // Call methods to send and receive messages here
                        isSocketInitialized = true
                        readMessage()
                        sendMessage(user)
                        Log.d(TAG, "socket intialization")
                    } catch (e: IOException) {
                        e.printStackTrace()
                        Log.d(TAG, "socket intialization failed }" + e.message)
                    }
                }

            }
        }

        fun cnDpStatus(sts: Boolean) {
            _dpStatus.value = sts
        }

        @RequiresApi(Build.VERSION_CODES.O)
        fun addMessage(string: CMessage) {
            _messages.add(string)
            Log.e(TAG, "InMsg: " + _messages.size)
        }

         fun addDisplayUser(user: String) {

            _displayUsers.add(user)
            Log.e(TAG, "new user added")
        }

         fun addMessageMap(name: String, cMessage: CMessage) {
            _messagesHandler.putIfAbsent(name, MutableLiveData(mutableListOf()))
            val messagesLiveData = _messagesHandler[name] ?: MutableLiveData()
            val currentMessages = messagesLiveData.value ?: emptyList()
            val updatedMessages = currentMessages.toMutableList().apply { add(cMessage) }
            messagesLiveData.value = updatedMessages
            _messagesHandler[name] = messagesLiveData
            // _messagesHandler[name]!!.add(cMessage)

        }
        fun increaseUnSeenMessageCount(name:String){

            _unseenMessagesCount.putIfAbsent(name,MutableLiveData(0))
            val countLiveData = _unseenMessagesCount[name]?:MutableLiveData()
            val currentCount = countLiveData.value
            val updatedCount = currentCount!! + 1
            countLiveData.value = updatedCount
            _unseenMessagesCount[name]= countLiveData
        }
        fun clearUnSeenMessageCount(name:String){
            if(_unseenMessagesCount.contains(name)){
                _unseenMessagesCount.remove(name)
            }

        }

         fun adduserName(name: String) {
            _userName.value = name

        }

        fun addChatUser(name: String) {
            chatUser = name

        }

         fun sendMessage(string: String) {
            GlobalScope.launch {
                val out = PrintWriter(socket.getOutputStream(), true)
                out.println(string)
                Log.d(TAG, "ms sent $string")

            }
        }

        @RequiresApi(Build.VERSION_CODES.O)
        fun readMessage() {
            GlobalScope.launch {
                var message: String
                try {
                    val input = BufferedReader(InputStreamReader(socket.getInputStream()))
                    while (input.readLine().also { message = it } != null) {
                        Log.d(TAG, "listenin for mes")
                        withContext(Dispatchers.Main) {
                            Log.d(TAG, message)
                            var lst = message.split("===>", limit = 2)
                            var user = lst[0].replace("Message from ", "").trim()

                            if (lst.size == 2) {
                                if (!displayUsers.contains(user)) {
                                    addDisplayUser(user)
                                }
                                addMessage(CMessage(lst[1], "text", LocalDateTime.now(), 0))
                                addMessageMap(
                                    user,
                                    CMessage(lst[1], "text", LocalDateTime.now(), 0)
                                )
                            }

                        }
                    }
                } catch (e: Exception) {
                    e.printStackTrace()
                }


            }
        }


    }/*
    fun adduserName(name: String){
        adduserNameMain(name)
    }
    fun addMessageMap(name: String, cMessage: CMessage){
        addMessageMapMain(name, cMessage)
    }
    fun addDisplayUser(user: String) {
        addDisplayUserMain(user)
    }
    fun sendMessage(string: String){
        sendMessageMain(string)
    }*/
}



data class CMessage(
    val msg: String,
    val type: String,
    val date: LocalDateTime,
    val inROut: Int
)
