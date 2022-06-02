package com.example.boardgamecollector

import android.graphics.Color
import android.os.Bundle
import android.util.TypedValue
import android.view.Gravity
import android.widget.LinearLayout
import android.widget.TableLayout
import android.widget.TableRow
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.example.boardgamecollector.databinding.ActivityHistoryScreenBinding

class HistoryScreen : AppCompatActivity() {

    private lateinit var binding: ActivityHistoryScreenBinding
    private lateinit var history: MutableList<History>
    private lateinit var table: TableLayout
    private lateinit var name: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityHistoryScreenBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val extras = intent.extras ?: return
        name = extras.getString("name")!!

        setSupportActionBar(findViewById(R.id.toolbar))
        binding.toolbarLayout.title = name

        table = findViewById(R.id.tableGames)

        showData()
    }

    private fun showData(){
        table.removeAllViews()
        loadHistory()
        showHistory()
    }

    private fun loadHistory(){
        val db = DBHandler(this, null, null, 1)
        history = db.findHistory(name)
        db.close()
    }

    private fun showHistory() {
        val leftRowMargin = 0
        val topRowMargin = 0
        val rightRowMargin = 0
        val bottomRowMargin = 0
        //var textSize = 0

        //textSize = resources.getDimension(R.dimen.font_size_verysmall).toInt()
        val smallTextSize: Int = resources.getDimension(R.dimen.font_size_small).toInt()
        val mediumTextSize: Int = resources.getDimension(R.dimen.font_size_medium).toInt()
        val rows = history.count()
        supportActionBar!!.title = title
        var textSpacer: TextView?


        for (i in -1 until rows) {
            var row: History? = null

            if (i < 0) {
                textSpacer = TextView(this)
                textSpacer.text = ""
            } else {
                row = history[i]
            }



            val tv = TextView(this)
            tv.layoutParams = TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT,
                TableRow.LayoutParams.WRAP_CONTENT)
            tv.gravity = Gravity.START
            tv.setPadding(20, 15, 20, 15)

            if (i == -1) run {
                tv.text = ""
                tv.setBackgroundColor(Color.parseColor("#f0f0f0"))
                tv.setTextSize(TypedValue.COMPLEX_UNIT_PX, mediumTextSize.toFloat())
            } else run{
                tv.setBackgroundColor(Color.parseColor("#f8f8f8"))
                tv.text = row?.i.toString()
                tv.setTextSize(TypedValue.COMPLEX_UNIT_PX, mediumTextSize.toFloat())
            }

            val tv2 = TextView(this)
            if (i == -1) {
                tv2.layoutParams = TableRow.LayoutParams(300,
                    TableRow.LayoutParams.MATCH_PARENT)
                tv2.setTextSize(TypedValue.COMPLEX_UNIT_PX, mediumTextSize.toFloat())
            } else {
                tv2.layoutParams = TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT,
                    TableRow.LayoutParams.MATCH_PARENT)
                tv2.setTextSize(TypedValue.COMPLEX_UNIT_PX, mediumTextSize.toFloat())
            }

            tv2.gravity = Gravity.START

            tv2.setPadding(20, 15, 20, 15)
            if (i == -1) {
                tv2.text = DBHandler.COLUMN_RANK
                tv2.setBackgroundColor(Color.parseColor("#f7f7f7"))
            } else {
                tv2.setBackgroundColor(Color.parseColor("#ffffff"))
                tv2.setTextColor(Color.parseColor("#000000"))
                tv2.text = row?.Rank.toString()
            }

            val layCustomer = LinearLayout(this)
            layCustomer.orientation = LinearLayout.VERTICAL
            layCustomer.setPadding(20, 10, 20, 10)
            layCustomer.setBackgroundColor(Color.parseColor("#f8f8f8"))

            val tv3 = TextView(this)
            if (i == -1) {
                tv3.layoutParams = TableRow.LayoutParams(550,
                    TableRow.LayoutParams.MATCH_PARENT)
                tv3.setPadding(5, 5, 0, 5)
                tv3.setTextSize(TypedValue.COMPLEX_UNIT_PX, mediumTextSize.toFloat())
            } else {
                tv3.layoutParams = TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT,
                    TableRow.LayoutParams.MATCH_PARENT)
                tv3.setPadding(5, 0, 0, 5)
                tv3.setTextSize(TypedValue.COMPLEX_UNIT_PX, mediumTextSize.toFloat())
            }

            tv3.gravity = Gravity.TOP

            if (i == -1) {
                tv3.text = DBHandler.COLUMN_DATE
                tv3.setBackgroundColor(Color.parseColor("#f0f0f0"))
            } else {
                tv3.setBackgroundColor(Color.parseColor("#f8f8f8"))
                tv3.setTextSize(TypedValue.COMPLEX_UNIT_PX, mediumTextSize.toFloat())
                tv3.text = row?.Date
            }
            layCustomer.addView(tv3)


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

            table.addView(tr, trParams)

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
                table.addView(trSep, trParamsSep)
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
        table.addView(trDate, trParamsSep)
    }
}