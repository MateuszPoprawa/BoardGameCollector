package com.example.boardgamecollector

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import java.io.File

class DBHandler(context: Context, name:String?,
    factory: SQLiteDatabase.CursorFactory?, version: Int): SQLiteOpenHelper(context,
DATABASE_NAME, factory, DATABASE_VERSION) {

        companion object {
            private const val DATABASE_VERSION = 1
            private const val DATABASE_NAME = "BoardGameDB.db"
            private const val COLUMN_ID = "id"
            private const val COLUMN_TITLE = "title"
            private const val COLUMN_RELEASED = "released"
            private const val COLUMN_GAME_ID = "game_id"
            private const val COLUMN_EXTENSION_ID = "extension_id"
            private const val COLUMN_POSITION = "position"
            private const val COLUMN_IMAGE = "image"
            private const val COLUMN_DATE = "date"
            var USER_NAME = ""
            var GAMES_COUNT = 0
            var EXTENSIONS_COUNT = 0
            var SYNCHRONIZATION_DATE = ""
            const val TABLE_GAMES = "games"
            const val TABLE_EXTENSIONS = "extensions"
            const val TABLE_HISTORICAL = "historical"

        }

    override fun onCreate(db: SQLiteDatabase) {
        val createGamesTable: String = (
                "CREATE TABLE IF NOT EXISTS " + TABLE_GAMES + "("  + COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COLUMN_TITLE + " TEXT, " + " TEXT, " + COLUMN_RELEASED +
                " INTEGER, " + COLUMN_GAME_ID + " LONG, " + COLUMN_POSITION + " INTEGER, " +
                COLUMN_IMAGE + " TEXT " + ")" )
        db.execSQL(createGamesTable)

        val createExtensionsTable: String = (
                "CREATE TABLE IF NOT EXISTS " + TABLE_EXTENSIONS + "("  + COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COLUMN_TITLE + " TEXT, " +  " TEXT, " + COLUMN_RELEASED +
                " INTEGER, " + COLUMN_EXTENSION_ID + " LONG, " + COLUMN_IMAGE + " TEXT " + ")"
                )
        db.execSQL(createExtensionsTable)

        val createHistoricalPositionTable: String = (
        "CREATE TABLE IF NOT EXISTS " + TABLE_HISTORICAL + "("  + COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COLUMN_TITLE + " TEXT, " + COLUMN_POSITION + " INTEGER, " + COLUMN_DATE +
                " DATE " + ")" )
        db.execSQL(createHistoricalPositionTable)
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        db.execSQL("DROP TABLE IF EXISTS $TABLE_GAMES")
        db.execSQL("DROP TABLE IF EXISTS $TABLE_EXTENSIONS")
        db.execSQL("DROP TABLE IF EXISTS $TABLE_HISTORICAL")
        onCreate(db)
    }

    fun readInfo(filesDir:String){
        val file = File("$filesDir/user.txt")
        val list = ArrayList<String>()
        file.forEachLine { list.add(it) }
        USER_NAME = list[0]
        GAMES_COUNT = list[1].toInt()
        EXTENSIONS_COUNT = list[2].toInt()
        SYNCHRONIZATION_DATE = list[3]
    }

    fun deleteDB(filesDir: String){
        val db = this.writableDatabase
        db.execSQL("DROP TABLE IF EXISTS $TABLE_GAMES")
        db.execSQL("DROP TABLE IF EXISTS $TABLE_EXTENSIONS")
        db.execSQL("DROP TABLE IF EXISTS $TABLE_HISTORICAL")
        val file = File("$filesDir/user.txt")
        file.delete()
    }
}