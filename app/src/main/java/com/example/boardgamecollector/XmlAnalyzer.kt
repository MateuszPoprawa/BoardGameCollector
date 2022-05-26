package com.example.boardgamecollector

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
        var gameID = ""
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

    fun checkGame(input: InputStream): Boolean{

        xpp.setInput(input, null)
        var eventType: Int = xpp.eventType
        var tagName = ""
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
        return type == "boardgame"
    }

}