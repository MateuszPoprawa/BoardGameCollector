package com.example.boardgamecollector

import android.app.Activity
import android.os.AsyncTask
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.ProgressBar
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import java.io.*
import java.net.MalformedURLException
import java.net.URL

class ConfigurationScreen : AppCompatActivity() {

    private var foundUser = false
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_configuration_screen)
        val button = findViewById<Button>(R.id.button)
        button.setOnClickListener { searchUser() }
    }

    override fun finish() {
        setResult(Activity.RESULT_OK)
        super.finish()
    }

    private fun searchUser() {
        val userName: EditText = findViewById(R.id.editTextUserName)
        DBHandler.USER_NAME = userName.text.toString()
        userName.setText("")
        val xml = XmlDownloader()
        xml.execute()
    }

    private fun checkResult(){
        if  (foundUser) {
            val sync = Synchronizer()
            val result = sync.start("$filesDir")
            Toast.makeText(applicationContext, result, Toast.LENGTH_SHORT).show()
            if (result == "Synchronization complete")
                finish()
        }
        else
            DBHandler.USER_NAME = ""
    }

    @Suppress("DEPRECATION")
    private inner class XmlDownloader: AsyncTask<String, Int, String>() {
        @Deprecated("Deprecated in Java",
            ReplaceWith("super.onPreExecute()", "android.os.AsyncTask")
        )
        override fun onPreExecute(){
            super.onPreExecute()
            val p =  findViewById<ProgressBar>(R.id.progressBar)
            p.visibility = android.view.View.VISIBLE
        }

        @Deprecated("Deprecated in Java",
            ReplaceWith("super.onPostExecute(result)", "android.os.AsyncTask")
        )
        override fun onPostExecute(result: String?) {
            super.onPostExecute(result)
            val p =  findViewById<ProgressBar>(R.id.progressBar)
            p.visibility = android.view.View.GONE
            Toast.makeText(applicationContext, result, Toast.LENGTH_LONG).show()
            foundUser = result == "User Found"
            checkResult()
        }

        @Deprecated("Deprecated in Java")
        override fun doInBackground(vararg p0: String?): String {
            try {
                val url = URL("https://boardgamegeek.com/xmlapi2/user?name=" + DBHandler.USER_NAME)
                val connection = url.openConnection()
                connection.connect()
                val isStream = url.openStream()
                val directory = File("$filesDir/XML")
                if (!directory.exists()) directory.mkdir()
                val fos = FileOutputStream("$directory/user.xml")
                val data = ByteArray(1024)
                var count: Int
                var total: Long = 0
                count = isStream.read(data)
                while (count != -1) {
                    total += count.toLong()
                    fos.write(data, 0, count)
                    count = isStream.read(data)
                }
                isStream.close()
                fos.close()

                val xml = XmlAnalyzer()
                return if (xml.checkUser(FileInputStream("$directory/user.xml"))                )
                    "User Found"
                else "User Not Found"
            } catch (e: MalformedURLException){
                return "Malformed URL"
            }
            catch (e: FileNotFoundException){
                return "File Not Found"
            }
            catch (e: IOException){
                return "IO Exception"
            }
        }
    }

}