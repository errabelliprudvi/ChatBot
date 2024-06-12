package com.example.chatbot

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.util.Log

class MyDatabaseHelper(context: Context) : SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {

    companion object {
        const val DATABASE_NAME = "ChatBot.db"
        const val DATABASE_VERSION = 1
    }

    override fun onCreate(db: SQLiteDatabase) {
        Log.d("MyDatabaseHelper","onCreate")
        db.execSQL("CREATE TABLE users (id INTEGER PRIMARY KEY, name TEXT)")

        val createTable = "CREATE TABLE messages (" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT," +
                "name TEXT,"+
                "msg TEXT," +
                "type TEXT," +
                "date TEXT," +
                "inROut INTEGER)"
        db.execSQL(createTable)
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        Log.d("MyDatabaseHelper","onUpdate")
        db.execSQL("DROP TABLE IF EXISTS messages")
        db.execSQL("DROP TABLE IF EXISTS users")
        onCreate(db)
    }
}
