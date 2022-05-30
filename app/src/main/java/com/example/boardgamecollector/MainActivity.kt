package com.example.boardgamecollector

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import java.io.File
import java.io.FileNotFoundException
import kotlin.system.exitProcess

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val db = DBHandler(this, null, null, 1)
        db.create()
        val configuration = !File("$filesDir/user.txt").exists()

        if (configuration) {
            val intent = Intent(this, ConfigurationScreen::class.java)
            activityLauncher.launch(intent)
        }
        try {
            db.readInfo("$filesDir")
        }
        catch (e: FileNotFoundException){
            DBHandler.USER_NAME = ""
            DBHandler.GAMES_COUNT = 0
            DBHandler.EXTENSIONS_COUNT = 0
            DBHandler.SYNCHRONIZATION_DATE = ""
            DBHandler.SYNCHRONIZATION_TIME = ""

        }
        setInfo()

        findViewById<Button>(R.id.gameList_Button).setOnClickListener {
            val intent = Intent(this, GameList::class.java)
            activityLauncher.launch(intent)
        }

        findViewById<Button>(R.id.extensionList_Button).setOnClickListener {
            val intent = Intent(this, ExtensionList::class.java)
            activityLauncher.launch(intent)
        }

        findViewById<Button>(R.id.synchronization_Button).setOnClickListener {
            val intent = Intent(this, SynchronizationScreen::class.java)
            activityLauncher.launch(intent)
        }

        findViewById<Button>(R.id.clear_Button).setOnClickListener {
            db.deleteDB("$filesDir")
            finish()
            exitProcess(0)
        }
    }

    private var activityLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
            result ->
        if (result.resultCode == Activity.RESULT_OK) {
            val db = DBHandler(this, null, null, 1)
            db.readInfo("$filesDir")
            setInfo()
        }
    }

    private fun setInfo(){
        findViewById<TextView>(R.id.userName).text = DBHandler.USER_NAME
        findViewById<TextView>(R.id.gamesCount).text = DBHandler.GAMES_COUNT.toString()
        findViewById<TextView>(R.id.expansionsCount).text = DBHandler.EXTENSIONS_COUNT.toString()
        findViewById<TextView>(R.id.lastSync).text = DBHandler.SYNCHRONIZATION_DATE
    }

}