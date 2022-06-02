package com.example.boardgamecollector

import android.app.Activity
import android.os.Bundle
import android.widget.Button
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.DialogFragment
import java.util.*

class SynchronizationScreen : AppCompatActivity(), StartSynchronizationDialogFragment.NoticeDialogListener {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_synchronization_screen)

        val p: ProgressBar = findViewById(R.id.progressBar2)
        p.progress = 0

        findViewById<TextView>(R.id.dateSync).text = DBHandler.SYNCHRONIZATION_DATE

        findViewById<Button>(R.id.synchronization_Button2).setOnClickListener{

            if(!compareDate()) {
                showNoticeDialog()
            }
            else
                synchronization()

        }

        findViewById<Button>(R.id.back_Button).setOnClickListener {
            finish()
        }
    }

    override fun finish() {
        setResult(Activity.RESULT_OK)
        super.finish()
    }

    private fun synchronization(){
        val db = DBHandler(applicationContext, null, null, 1)
        db.deleteDB("$filesDir")
        db.create()
        val sync = Synchronizer()
        val result = sync.start("$filesDir", applicationContext, findViewById(R.id.progressBar2))
        runOnUiThread {
            Toast.makeText(applicationContext, result, Toast.LENGTH_SHORT).show()
        }
        if (result == "Synchronization complete") {
            db.close()
            finish()
        }
        else
            db.deleteDB("$filesDir")
    }

    private fun compareDate(): Boolean{
        val d1 = Calendar.getInstance().timeInMillis
        val d2 = DBHandler.SYNCHRONIZATION_TIME.toLong()
        return (d1-24*60*60*1000) > d2
    }

    private fun showNoticeDialog() {
        val dialog = StartSynchronizationDialogFragment()
        dialog.show(supportFragmentManager, "NoticeDialogFragment")
    }

    override fun onDialogPositiveClick(dialog: DialogFragment) {
        val p: ProgressBar = findViewById(R.id.progressBar2)
        p.visibility = android.view.View.VISIBLE
        Thread {
            try {
                synchronization()
            } catch (ex: Exception) {
                ex.printStackTrace()
            }
        }.start()
    }

    override fun onDialogNegativeClick(dialog: DialogFragment) {
        val p: ProgressBar = findViewById(R.id.progressBar2)
        p.visibility = android.view.View.GONE
    }
}