package com.example.chatbot

import android.annotation.SuppressLint
import android.content.ContentValues
import android.content.Context
import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import java.time.LocalDateTime

class DbOperations(context: Context) {
    init{
        dbHelper = MyDatabaseHelper(context)
    }
    companion object {
        private lateinit  var dbHelper: MyDatabaseHelper

        fun insertUser(name: String) {
            Log.d("DbOperations","users inserted")

            val db = dbHelper.writableDatabase

            val values = ContentValues().apply {
                put("name", name)
            }

            db.insert("users", null, values)


        }

        @RequiresApi(Build.VERSION_CODES.O)
        fun fetchUsers() {
            Log.d("DbOperations","users fetched")
            val db = dbHelper.readableDatabase
            val cursor = db.query(
                "users",
                arrayOf("name"),
                null,
                null,
                null,
                null,
                null
            )

            with(cursor) {
                while (moveToNext()) {
                    //val msg = getString(getColumnIndexOrThrow("msg"))
                    MVmodel.addDisplayUser(getString(getColumnIndexOrThrow("name")))
                    // Use the data
                }
            }
            cursor.close()

        }

        fun insertMs(name: String, cMessage: CMessage) {
            //val dbHelper = MyDatabaseHelper(context)
            Log.d("DbOperations","message inserted")
            val db = dbHelper.writableDatabase

            val values = ContentValues().apply {
                put("name", name)
                put("msg", cMessage.msg)
                put("type", cMessage.type)
                put("date", cMessage.date.toString())
                put("inROut", cMessage.inROut)
            }

            db.insert("messages", null, values)

        }

        @RequiresApi(Build.VERSION_CODES.O)
        fun fetchMs() {
            Log.d("DbOperations","messages fetched")
            val db = dbHelper.readableDatabase
           // val selection = "name = $name"
            val cursor = db.query(
                "messages",
                arrayOf("name", "msg", "type", "date", "inROut"),
                null,
                null,
                null,
                null,
                null
            )

            with(cursor) {
                while (moveToNext()) {
                    val name = getString(getColumnIndexOrThrow("name"))
                    val msg = getString(getColumnIndexOrThrow("msg"))
                    val type = getString(getColumnIndexOrThrow("type"))
                    val date = LocalDateTime.parse(getString(getColumnIndexOrThrow("date")))

                    val inROut = getInt(getColumnIndexOrThrow("inROut"))
                    MVmodel.addMessageMap(name, CMessage(msg, type, date, inROut))
                    // Use the data
                }
            }
            cursor.close()

        }

        fun update() {
            val db = dbHelper.writableDatabase

            val values = ContentValues().apply {
                put("name", "Jane Doe")
            }

            val selection = "id = ?"
            val selectionArgs = arrayOf("1")

            db.update("my_table", values, selection, selectionArgs)

        }

        fun delete() {
            val db = dbHelper.writableDatabase

            val selection = "id = ?"
            val selectionArgs = arrayOf("1")

            db.delete("my_table", selection, selectionArgs)

        }
    }


}