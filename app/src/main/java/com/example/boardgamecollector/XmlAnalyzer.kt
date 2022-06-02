package com.example.boardgamecollector

import android.content.Context
import android.widget.ProgressBar
import org.xmlpull.v1.XmlPullParser
import org.xmlpull.v1.XmlPullParserFactory
import java.io.InputStream


class XmlAnalyzer {
    private val xmlFactoryObject = XmlPullParserFactory.newInstance()
    private val xpp = xmlFactoryObject.newPullParser()

    fun checkUser(input: InputStream): Boolean{
        xpp.setInput(input, null)
        var name: String
        var attribute: String
        var eventType: Int = xpp.eventType
        while (eventType != XmlPullParser.END_DOCUMENT) {
            if (eventType == XmlPullParser.START_TAG) {
                name = xpp.name
                attribute = xpp.getAttributeValue(0)
                if(name == "user")
                {
                    return attribute != ""
                }
            }
            eventType = xpp.next()
        }
        input.close()
        return false
    }

    fun checkCollection(input: InputStream): ArrayList<String>{
        xpp.setInput(input, null)
        var tagName:String
        var gameID: String
        var eventType: Int = xpp.eventType
        val idList = ArrayList<String>()
        while (eventType != XmlPullParser.END_DOCUMENT) {
            if (eventType == XmlPullParser.START_TAG) {
                tagName = xpp.name
                if(tagName == "item"){
                    gameID = xpp.getAttributeValue(1)
                    idList.add(gameID)
                }
            }
            eventType = xpp.next()
        }
        input.close()
        return idList
    }



    fun addCollection(input: InputStream, c: Context, p: ProgressBar, game: Boolean){
        xpp.setInput(input, null)
        var eventType: Int = xpp.eventType
        var tagName: String
        var gameID: Long = 0
        var title = ""
        var date = 0
        var rank: Int
        var img = ""
        val db = DBHandler(c, null, null, 1)

        while (eventType != XmlPullParser.END_DOCUMENT) {
            if (eventType == XmlPullParser.START_TAG) {
                tagName = xpp.name

                if(tagName == "item"){
                    gameID = xpp.getAttributeValue(1).toLong()
                }
                if (tagName == "name") {
                    xpp.next()
                    title = xpp.text
                }
                if (tagName == "yearpublished") {
                    xpp.next()
                    date = xpp.text.toInt()
                }
                if (tagName == "thumbnail") {
                    xpp.next()
                    img = if(xpp.text != null)
                        xpp.text
                    else
                        ""
                    if (!game){
                        db.addExtension(title, date, img, gameID)
                        p.progress += 1
                        DBHandler.EXTENSIONS_COUNT += 1
                    }
                }
                if (game && tagName == "rank" && xpp.getAttributeValue(0) == "subtype") {
                    rank = if (xpp.getAttributeValue(4) == "Not Ranked")
                        0
                    else
                        xpp.getAttributeValue(4).toInt()
                    db.addGame(title, date, rank, img, gameID)
                    db.addRank(title, rank, Synchronizer().currentDate(false), Synchronizer().currentHour())
                    p.progress += 1
                    DBHandler.GAMES_COUNT += 1
                }
            }
            eventType = xpp.next()
        }
        db.close()
        input.close()
    }

    /*
    fun checkGame(input: InputStream, gameID: String, c: Context): Boolean{

        xpp.setInput(input, null)
        var eventType: Int = xpp.eventType
        var tagName: String
        var title = ""
        var date = 0
        var rank = 0
        var img = ""
        var type = ""

        while (eventType != XmlPullParser.END_DOCUMENT) {
            if (eventType == XmlPullParser.START_TAG) {
                tagName = xpp.name
                if (tagName == "name" && xpp.getAttributeValue(0) == "primary")
                    title = xpp.getAttributeValue(2)
                if (tagName == "yearpublished")
                    date =  xpp.getAttributeValue(0).toInt()
                if (tagName == "rank" && xpp.getAttributeValue(0) == "subtype") {
                    rank = if (xpp.getAttributeValue(4) == "Not Ranked")
                        0
                    else
                        xpp.getAttributeValue(4).toInt()
                }
                if (tagName == "thumbnail") {
                    xpp.next()
                    img = xpp.text
                }
                if (tagName == "item")
                    type = xpp.getAttributeValue(0)
            }
            eventType = xpp.next()
        }
        input.close()
        val db = DBHandler(c, null, null, 1)
        if (type == "boardgame") {
            db.addGame(title, date, rank, img, gameID.toLong())
            db.addRank(title, rank, Synchronizer().currentDate(false), Synchronizer().currentHour())
        }
        else
            db.addExtension(title, date, img, gameID.toLong())
        db.close()
        return type == "boardgame"
    }
     */
}