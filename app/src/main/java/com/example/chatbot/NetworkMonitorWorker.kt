package com.example.chatbot

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.util.Log
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.BufferedReader
import java.io.InputStreamReader
import java.io.PrintWriter
import java.net.Socket
import java.util.Scanner


class NetworkMonitorWorker(context: Context, workerParams: WorkerParameters): CoroutineWorker(context, workerParams) {

    companion object {
        private const val SERVER_ADDRESS = "34.100.250.235"
        private const val SERVER_PORT = 12345
        private const val TAG ="NetworkMonitorWorker"
    }

    override suspend fun doWork(): Result {
        val connectivityManager = applicationContext.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val network = connectivityManager.activeNetwork
        val capabilities = connectivityManager.getNetworkCapabilities(network)

        if (capabilities != null && capabilities.hasCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET)) {
            return withContext(Dispatchers.IO) {
                try {
                    Socket(SERVER_ADDRESS, SERVER_PORT).use { socket ->
                        val out = PrintWriter(socket.getOutputStream(), true)
                        val `in` = BufferedReader(InputStreamReader(socket.getInputStream()))
                       // val scanner = Scanner(System.`in`)
                       Log.d(TAG,`in`.readLine())
                        //println(`in`.readLine()) // "Enter your username:"
                        val username = "8184919199"
                            //scanner.nextLine()
                        out.println(username)
                       // println(`in`.readLine()) // "Welcome, username"
                        Log.d(TAG,`in`.readLine())
                        // Read messages from the server
                        val readerThread = Thread {
                            var message: String?
                            try {
                                while (`in`.readLine().also { message = it } != null) {
                                    println(message)
                                }
                            } catch (e: Exception) {
                                e.printStackTrace()
                            }
                        }
                        readerThread.start()

                        // Send messages to the server
                      //  val userInput="prudvi:hira"

                          //  out.println(userInput)
                        }

                    Result.success()
                }
                catch (e: Exception) {
                    e.printStackTrace()
                    Result.failure()
                }
            }

              }
        else {
            // Not connected to any network
            Log.d(TAG, "Not connected to any network")
        }

        return Result.failure()
    }
}