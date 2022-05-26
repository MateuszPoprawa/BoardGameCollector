package com.example.boardgamecollector

import android.os.AsyncTask
import java.io.*
import java.net.MalformedURLException
import java.net.URL
import java.util.*

class Synchronizer {
    private var query = ""
    var fileName = ""
    var filesDir = ""

    fun start(f: String): String{
        filesDir = f
        var link = "https://www.boardgamegeek.com/xmlapi2/collection?username=" + DBHandler.USER_NAME
        var file = "collection"
        var r = downloadFile(link, file)
        if(r != "Success")
            return "Synchronization error: $r"
        val xml = XmlAnalyzer()
        val idList: ArrayList<String> = xml.checkCollection(FileInputStream("$filesDir/XML/$file.xml"))
        for (item in idList){
            link = "https://boardgamegeek.com/xmlapi2/thing?id=$item&stats=1"
            file = "gameTemp"
            r = downloadFile(link, file)
            if(r != "Success")
                return "Synchronization error: $r"
            if (xml.checkGame(FileInputStream("$filesDir/XML/$file.xml")))
                DBHandler.GAMES_COUNT += 1
            else
                DBHandler.EXTENSIONS_COUNT += 1
        }
        DBHandler.SYNCHRONIZATION_DATE = currentDate(true)
        saveToFile()
        return "Synchronization complete"
    }

    private fun currentDate(hour:Boolean): String {
        val calendar = Calendar.getInstance()
        val intDay = calendar.get(Calendar.DATE)
        val day = if (intDay < 10)
            "0$intDay"
        else
            intDay.toString()
        val intMonth = (calendar.get(Calendar.MONTH) + 1)
        val month = if (intMonth < 10)
            "0$intMonth"
        else
            intMonth.toString()
        val year = calendar.get(Calendar.YEAR).toString()
        var result = "$year-$month-$day"
        if(hour)
        {
            val h = calendar.get(Calendar.HOUR_OF_DAY).toString()
            val min = calendar.get((Calendar.MINUTE)).toString()
            result += " $h:$min"
        }
        return result
    }

    private fun saveToFile(){
        val file = File("$filesDir/user.txt")
        val text = DBHandler.USER_NAME + "\n" + DBHandler.GAMES_COUNT.toString() + "\n" +
                DBHandler.EXTENSIONS_COUNT.toString() + "\n" + DBHandler.SYNCHRONIZATION_DATE
        file.writeText(text)
    }

    private fun downloadFile(q:String, f:String):String {
        query = q
        fileName = f
        val xml = XmlDownloader()
        return xml.execute().get()
    }


    @Suppress("DEPRECATION")
    private inner class XmlDownloader: AsyncTask<String, Int, String>() {
        @Deprecated("Deprecated in Java",
            ReplaceWith("super.onPreExecute()", "android.os.AsyncTask")
        )
        override fun onPreExecute(){
            super.onPreExecute()
        }

        @Deprecated("Deprecated in Java",
            ReplaceWith("super.onPostExecute(result)", "android.os.AsyncTask")
        )
        override fun onPostExecute(result: String?) {
            super.onPostExecute(result)
        }

        @Deprecated("Deprecated in Java")
        override fun doInBackground(vararg p0: String?): String {
            try {
                val url = URL(query)
                val connection = url.openConnection()
                connection.connect()
                val isStream = url.openStream()
                val directory = File("$filesDir/XML")
                if (!directory.exists()) directory.mkdir()
                val fos = FileOutputStream("$directory/$fileName.xml")
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
                return "Success"
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