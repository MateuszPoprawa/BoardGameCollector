package com.example.boardgamecollector

import android.graphics.BitmapFactory
import android.graphics.Color
import android.os.Bundle
import android.util.TypedValue
import android.view.Gravity
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import com.example.boardgamecollector.databinding.ActivityGameListBinding
import java.net.URL


class GameList : AppCompatActivity(), AdapterView.OnItemSelectedListener {

    private lateinit var binding: ActivityGameListBinding
    private var spinner: Spinner? = null
    private var sortList = arrayOf (DBHandler.COLUMN_TITLE, DBHandler.COLUMN_RELEASED, DBHandler.COLUMN_RANK)
    private var order = DBHandler.COLUMN_TITLE
    private lateinit var tableGames: TableLayout
    private var games: MutableList<Game> = mutableListOf()
    private var name = "List of games"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityGameListBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setSupportActionBar(findViewById(R.id.toolbar))
        binding.toolbarLayout.title = title

        spinner = findViewById(R.id.spinner1)
        spinner!!.onItemSelectedListener = this

        val arrayAdapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, sortList)
        arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinner!!.adapter = arrayAdapter

        tableGames = findViewById(R.id.tableGames)

        findViewById<Button>(R.id.buttonSort1).setOnClickListener {
            showData()
        }

        showData()
    }

    override fun onItemSelected(arg0: AdapterView<*>, arg1: View, position: Int, id: Long)
    {
        if(arg0.id == R.id.spinner1) {
            order = sortList[position]
        }
    }

    override fun onNothingSelected(arg0: AdapterView<*>) {
        order = DBHandler.COLUMN_TITLE
    }

    private fun showData(){
        tableGames.removeAllViews()
        loadGames()
        showGames()
    }

    private fun loadGames(){
        val db = DBHandler(this, null, null, 1)
        games = mutableListOf()
        for (i in 0..DBHandler.GAMES_COUNT){
            db.findGame(i, order)?.let { games.add(it) }
        }
        db.close()
    }


    private fun showGames(){

        val leftRowMargin = 0
        val topRowMargin = 0
        val rightRowMargin = 0
        val bottomRowMargin = 0
        var textSize = 0
        var smallTextSize = 0
        var mediumTextSize = 0

        textSize = resources.getDimension(R.dimen.font_size_verysmall).toInt()
        smallTextSize = resources.getDimension(R.dimen.font_size_small).toInt()
        mediumTextSize = resources.getDimension(R.dimen.font_size_medium).toInt()
        val rows = DBHandler.GAMES_COUNT
        supportActionBar!!.title = "Games"
        var textSpacer: TextView? = null


        for (i in -1 until rows) {
            var row: Game? = null

            if (i < 0) {
                textSpacer = TextView(this)
                textSpacer.text = name
            } else {
                row = games[i]
            }


            val tv = TextView(this)
            tv.layoutParams = TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT,
                TableRow.LayoutParams.WRAP_CONTENT)
            tv.gravity = Gravity.START
            tv.setPadding(20, 15, 20, 15)

            if (i == -1) run {
                tv.text = " " //name
                tv.setBackgroundColor(Color.parseColor("#ffffff"))
                tv.setTextSize(TypedValue.COMPLEX_UNIT_PX, smallTextSize.toFloat())
            } else run {
                tv.setBackgroundColor(Color.parseColor("#ffffff"))
                tv.text = row?.i.toString()
                tv.setTextSize(TypedValue.COMPLEX_UNIT_PX, mediumTextSize.toFloat())
            }

            val tv2 = ImageButton(this)
            if (i == -1) {
                tv2.layoutParams = TableRow.LayoutParams()
            } else {
                tv2.layoutParams = TableRow.LayoutParams(200,200)
            }


            tv2.setPadding(20, 15, 20, 15)
            if (i == -1) {
                tv2.setBackgroundColor(Color.parseColor("#ffffff"))
            } else {
                tv2.setBackgroundColor(Color.parseColor("#ffffff"))
                Thread {
                    try {
                        val url = URL(row?.Img)
                        val bmp = BitmapFactory.decodeStream(url.openConnection().getInputStream())
                        runOnUiThread(java.lang.Runnable {
                            tv2.setImageBitmap(bmp)
                        })
                    } catch (ex: Exception) {
                        ex.printStackTrace()
                    }
                }.start()
            }

            val layCustomer = LinearLayout(this)
            layCustomer.orientation = LinearLayout.VERTICAL
            layCustomer.setPadding(20, 10, 20, 10)
            layCustomer.setBackgroundColor(Color.parseColor("#f8f8f8"))

            val tv3 = TextView(this)
            if (i == -1) {
                tv3.layoutParams = TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT,
                    TableRow.LayoutParams.MATCH_PARENT)
                tv3.setPadding(5, 5, 0, 5)
                tv3.setTextSize(TypedValue.COMPLEX_UNIT_PX, smallTextSize.toFloat())
            } else {
                tv3.layoutParams = TableRow.LayoutParams(450,
                    TableRow.LayoutParams.MATCH_PARENT)
                tv3.setPadding(5, 0, 0, 5)
                tv3.setTextSize(TypedValue.COMPLEX_UNIT_PX, textSize.toFloat())
            }

            tv3.gravity = Gravity.TOP

            if (i == -1) {
                tv3.text = DBHandler.COLUMN_TITLE
                tv3.setBackgroundColor(Color.parseColor("#f0f0f0"))
            } else {
                tv3.setBackgroundColor(Color.parseColor("#f8f8f8"))
                tv3.setTextSize(TypedValue.COMPLEX_UNIT_PX, mediumTextSize.toFloat())
                tv3.text = row?.Title
            }
            layCustomer.addView(tv3)


            if (i > -1) {
                val tv3b = TextView(this)
                tv3b.layoutParams = TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT,
                    TableRow.LayoutParams.WRAP_CONTENT)

                tv3b.gravity = Gravity.END
                tv3b.setTextSize(TypedValue.COMPLEX_UNIT_PX, mediumTextSize.toFloat())
                tv3b.setPadding(5, 1, 0, 5)
                tv3b.setTextColor(Color.parseColor("#aaaaaa"))
                tv3b.setBackgroundColor(Color.parseColor("#f8f8f8"))
                tv3b.text = "(" + row?.Year + ")"
                layCustomer.addView(tv3b)
            }

            val tv4 = TextView(this)
            tv4.layoutParams = TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT,
                TableRow.LayoutParams.WRAP_CONTENT)
            tv4.gravity = Gravity.START
            tv4.setPadding(20, 15, 20, 15)

            if (i == -1) run {
                tv4.text = DBHandler.COLUMN_RANK
                tv4.setBackgroundColor(Color.parseColor("#f0f0f0"))
                tv4.setTextSize(TypedValue.COMPLEX_UNIT_PX, smallTextSize.toFloat())
                tv4.setPadding(5, 5, 0,5)
            } else run {
                tv4.setBackgroundColor(Color.parseColor("#ffffff"))
                tv4.text = row?.Rank.toString()
                tv4.setTextSize(TypedValue.COMPLEX_UNIT_PX, mediumTextSize.toFloat())
            }

            // add table row
            val tr = TableRow(this)
            tr.id = i + 1
            val trParams = TableLayout.LayoutParams(TableLayout.LayoutParams.MATCH_PARENT,
                TableLayout.LayoutParams.MATCH_PARENT)
            trParams.setMargins(leftRowMargin, topRowMargin, rightRowMargin, bottomRowMargin)
            tr.setPadding(10, 0, 10, 0)
            tr.layoutParams = trParams

            tr.addView(tv)
            tr.addView(tv2)
            tr.addView(layCustomer)
            tr.addView(tv4)

            tableGames.addView(tr, trParams)

            if (i > -1) {

                val trSep = TableRow(this)
                val trParamsSep = TableLayout.LayoutParams(TableLayout.LayoutParams.MATCH_PARENT,
                    TableLayout.LayoutParams.WRAP_CONTENT)
                trParamsSep.setMargins(leftRowMargin, topRowMargin, rightRowMargin, bottomRowMargin)

                trSep.layoutParams = trParamsSep
                val tvSep = TextView(this)
                val tvSepLay = TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT,
                    TableRow.LayoutParams.WRAP_CONTENT)
                tvSepLay.span = 4
                tvSep.layoutParams = tvSepLay
                tvSep.setBackgroundColor(Color.parseColor("#d9d9d9"))
                tvSep.height = 1

                trSep.addView(tvSep)
                tableGames.addView(trSep, trParamsSep)
            }

        }
        val trDate = TableRow(this)
        val trParamsSep = TableLayout.LayoutParams(TableLayout.LayoutParams.MATCH_PARENT,
            TableLayout.LayoutParams.WRAP_CONTENT)
        trParamsSep.setMargins(leftRowMargin, topRowMargin, rightRowMargin, bottomRowMargin)

        trDate.layoutParams = trParamsSep
        val tvSep = TextView(this)
        val tvSepLay = TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT,
            TableRow.LayoutParams.MATCH_PARENT)
        tvSepLay.span = 4
        tvSep.layoutParams = tvSepLay
        tvSep.setBackgroundColor(Color.parseColor("#d9d9d9"))

        tvSep.setTextSize(TypedValue.COMPLEX_UNIT_PX, smallTextSize.toFloat())

        trDate.addView(tvSep)
        tableGames.addView(trDate, trParamsSep)

    }
}