package com.example.boardgamecollector

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

class DBHandler(context: Context, name:String?,
    factory: SQLiteDatabase.CursorFactory?, version: Int): SQLiteOpenHelper(context,
DATABASE_NAME, factory, DATABASE_VERSION) {

        companion object {
            private const val DATABASE_VERSION = 1
            private const val DATABASE_NAME = "BoardGameDB.db"
            private var USER_NAME = ""

            fun setUserName(name: String){
                USER_NAME = name
            }

            fun getUserName(): String {
                return USER_NAME
            }
        }

    override fun onCreate(p0: SQLiteDatabase?) {
        TODO("Not yet implemented")
    }

    override fun onUpgrade(p0: SQLiteDatabase?, p1: Int, p2: Int) {
        TODO("Not yet implemented")
    }
}