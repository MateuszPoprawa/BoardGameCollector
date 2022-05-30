package com.example.boardgamecollector

import android.content.ContentValues
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
            const val COLUMN_TITLE = "title"
            const val COLUMN_RELEASED = "released"
            private const val COLUMN_GAME_ID = "game_id"
            private const val COLUMN_EXTENSION_ID = "extension_id"
            const val COLUMN_RANK = "rank"
            private const val COLUMN_IMAGE = "image"
            private const val COLUMN_DATE = "date"
            var USER_NAME = ""
            var GAMES_COUNT = 0
            var EXTENSIONS_COUNT = 0
            var SYNCHRONIZATION_DATE = ""
            var SYNCHRONIZATION_TIME = ""
            const val TABLE_GAMES = "games"
            const val TABLE_EXTENSIONS = "extensions"
            const val TABLE_HISTORICAL = "historical"
        }

    override fun onCreate(db: SQLiteDatabase) {
        val createGamesTable: String = (
                "CREATE TABLE IF NOT EXISTS " + TABLE_GAMES + "("  + COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COLUMN_TITLE + " TEXT, " + COLUMN_RELEASED +
                " INTEGER, " + COLUMN_GAME_ID + " LONG, " + COLUMN_RANK + " INTEGER, " +
                COLUMN_IMAGE + " TEXT " + ")" )
        db.execSQL(createGamesTable)

        val createExtensionsTable: String = (
                "CREATE TABLE IF NOT EXISTS " + TABLE_EXTENSIONS + "("  + COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COLUMN_TITLE + " TEXT, " + COLUMN_RELEASED +
                " INTEGER, " + COLUMN_EXTENSION_ID + " LONG, " + COLUMN_IMAGE + " TEXT " + ")"
                )
        db.execSQL(createExtensionsTable)

        val createHistoricalPositionTable: String = (
        "CREATE TABLE IF NOT EXISTS " + TABLE_HISTORICAL + "("  + COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COLUMN_TITLE + " TEXT, " + COLUMN_RANK + " INTEGER, " + COLUMN_DATE +
                " DATE " + ")" )
        db.execSQL(createHistoricalPositionTable)

        db.close()
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        db.execSQL("DROP TABLE IF EXISTS $TABLE_GAMES")
        db.execSQL("DROP TABLE IF EXISTS $TABLE_EXTENSIONS")
        db.execSQL("DROP TABLE IF EXISTS $TABLE_HISTORICAL")
        onCreate(db)
    }

    fun create(){
        onCreate(this.writableDatabase)
    }

    fun readInfo(filesDir:String){
        val file = File("$filesDir/user.txt")
        val list = ArrayList<String>()
        file.forEachLine { list.add(it) }
        USER_NAME = list[0]
        GAMES_COUNT = list[1].toInt()
        EXTENSIONS_COUNT = list[2].toInt()
        SYNCHRONIZATION_DATE = list[3]
        SYNCHRONIZATION_TIME = list[4]
    }

    fun deleteDB(filesDir: String){
        val db = this.writableDatabase
        db.execSQL("DROP TABLE IF EXISTS $TABLE_GAMES")
        db.execSQL("DROP TABLE IF EXISTS $TABLE_EXTENSIONS")
        db.execSQL("DROP TABLE IF EXISTS $TABLE_HISTORICAL")
        db.close()
        val file = File("$filesDir/user.txt")
        file.delete()
        GAMES_COUNT = 0
        EXTENSIONS_COUNT = 0
    }

    fun addGame(title: String, date: Int, rank: Int, img: String, gameID: Long){
        val values = ContentValues()
        values.put(COLUMN_TITLE, title)
        values.put(COLUMN_RELEASED, date)
        values.put(COLUMN_RANK, rank)
        values.put(COLUMN_IMAGE, img)
        values.put(COLUMN_GAME_ID, gameID)
        val db = this.writableDatabase
        db.insert(TABLE_GAMES, null, values)
        db.close()
    }

    fun addExtension(title: String, date: Int, img: String, gameID: Long){
        val values = ContentValues()
        values.put(COLUMN_TITLE, title)
        values.put(COLUMN_RELEASED, date)
        values.put(COLUMN_IMAGE, img)
        values.put(COLUMN_EXTENSION_ID, gameID)
        val db = this.writableDatabase
        db.insert(TABLE_EXTENSIONS, null, values)
        db.close()
    }

    fun findGame(pos:Int, order:String):Game? {

        var query = "SELECT * FROM  $TABLE_GAMES ORDER BY $order"
        if (order == COLUMN_RELEASED)
            query += " DESC"
        val db = this.writableDatabase
        val cursor = db.rawQuery(query, null)
        var game: Game? = null
        if (cursor.moveToPosition(pos)) {
            val img = cursor.getString(5)
            val title = cursor.getString(1)
            val year = cursor.getString(2)
            val rank = Integer.parseInt(cursor.getString(4))
            game = Game(pos, img, title, year, rank)
            cursor.close()
        }
        db.close()
        return game
    }

    fun findExtension(pos:Int, order:String):Extension? {

        val query = "SELECT * FROM  $TABLE_EXTENSIONS $order"
        val db = this.writableDatabase
        val cursor = db.rawQuery(query, null)
        var ext: Extension? = null
        if (cursor.moveToPosition(pos)) {
            val img = cursor.getString(4)
            val title = cursor.getString(1)
            val year = cursor.getString(2)
            ext = Extension(pos, img, title, year)
            cursor.close()
        }
        db.close()
        return ext
    }


}