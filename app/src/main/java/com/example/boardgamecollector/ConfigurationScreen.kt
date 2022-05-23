package com.example.boardgamecollector

import android.os.AsyncTask
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.ProgressBar
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import java.io.File
import java.io.FileNotFoundException
import java.io.FileOutputStream
import java.io.IOException
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

    private fun searchUser() {
        val userName: EditText = findViewById(R.id.editTextUserName)
        DBHandler.setUserName(userName.text.toString())
        userName.setText("")
        val xml = XmlDownloader()
        xml.execute()
    }

    private fun checkResult(){
        if  (foundUser)
            super.finish()
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
            foundUser = result == "Success"
            checkResult()
        }

        @Deprecated("Deprecated in Java")
        override fun doInBackground(vararg p0: String?): String {
            try {
                val url = URL("https://boardgamegeek.com/xmlapi2/user?name=" + DBHandler.getUserName())
                //val url = URL("https://boardgamegeek.com/xmlapi2/user?name=loutre_on_fire")
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
                return if (total > 548)
                    "Success"
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