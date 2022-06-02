package com.example.boardgamecollector

import android.graphics.BitmapFactory
import android.graphics.Color
import android.os.Bundle
import android.util.TypedValue
import android.view.Gravity
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.core.widget.NestedScrollView
import com.example.boardgamecollector.databinding.ActivityExtensionListBinding
import java.net.URL

class ExtensionList : AppCompatActivity(), AdapterView.OnItemSelectedListener {

    private lateinit var binding: ActivityExtensionListBinding
    private var spinner: Spinner? = null
    private var sortList = arrayOf (DBHandler.COLUMN_TITLE, DBHandler.COLUMN_RELEASED)
    private var order = DBHandler.COLUMN_TITLE
    private lateinit var tableExtensions: TableLayout
    private var extensions: MutableList<Extension> = mutableListOf()
    private var name = "List of extensions"
    private val extensionsOnPage = 50
    private var pageCount: Int = DBHandler.EXTENSIONS_COUNT/extensionsOnPage
    private val onLastPage = DBHandler.EXTENSIONS_COUNT%extensionsOnPage
    private var pageNumber: Int = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityExtensionListBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setSupportActionBar(findViewById(R.id.toolbar))
        binding.toolbarLayout.title = title

        spinner = findViewById(R.id.spinner2)
        spinner!!.onItemSelectedListener = this

        val arrayAdapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, sortList)
        arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinner!!.adapter = arrayAdapter

        tableExtensions = findViewById(R.id.tableGames)

        findViewById<Button>(R.id.buttonSort2).setOnClickListener {
            pageNumber = 0
            showData()
        }

        showData()
    }

    override fun onItemSelected(arg0: AdapterView<*>, arg1: View, position: Int, id: Long) {
        if(arg0.id == R.id.spinner2) {
            order = sortList[position]
        }
    }

    override fun onNothingSelected(arg0: AdapterView<*>) {
        order = DBHandler.COLUMN_TITLE
    }

    private fun showData(){
        tableExtensions.removeAllViews()
        loadGames()
        showExtensions()
    }

    private fun loadGames(){
        val db = DBHandler(this, null, null, 1)
        extensions = mutableListOf()
        val n: Int = if (pageNumber == pageCount)
            onLastPage
        else
            extensionsOnPage
        for (i in 0..n){
            db.findExtension(i + (pageNumber*extensionsOnPage), order)?.let { extensions.add(it) }
        }
        db.close()
    }

    private fun showExtensions() {
        val leftRowMargin = 0
        val topRowMargin = 0
        val rightRowMargin = 0
        val bottomRowMargin = 0

        val textSize: Int = resources.getDimension(R.dimen.font_size_verysmall).toInt()
        val smallTextSize: Int = resources.getDimension(R.dimen.font_size_small).toInt()
        val mediumTextSize: Int = resources.getDimension(R.dimen.font_size_medium).toInt()
        val rows: Int = if (pageNumber != pageCount)
            extensionsOnPage
        else onLastPage
        supportActionBar!!.title = "Extensions"
        var textSpacer: TextView?


        for (i in -1 until rows) {
            var row: Extension? = null

            if (i < 0) {
                textSpacer = TextView(this)
                textSpacer.text = name
            } else {
                row = extensions[i]
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
            } else run{
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
                if (row?.Img != "") {
                    Thread {
                        try {
                            val url = URL(row?.Img)
                            val bmp =
                                BitmapFactory.decodeStream(url.openConnection().getInputStream())
                            runOnUiThread {
                                tv2.setImageBitmap(bmp)
                            }
                        } catch (ex: Exception) {
                            ex.printStackTrace()
                        }
                    }.start()
                }
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
                tv3.layoutParams = TableRow.LayoutParams(600,
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

            tableExtensions.addView(tr, trParams)

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
                tableExtensions.addView(trSep, trParamsSep)
            }


        }

        if (pageNumber != pageCount) {
            val nextButton = Button(this)
            nextButton.text = getString(R.string.next_page)
            nextButton.setBackgroundColor(Color.GREEN)
            nextButton.setOnClickListener {
                pageNumber += 1
                findViewById<NestedScrollView>(R.id.scrollView).fullScroll(View.FOCUS_UP)
                showData()
            }
            tableExtensions.addView(nextButton)
        }

        if (pageNumber != 0) {
            val prevButton = Button(this)
            prevButton.text = getString(R.string.prev_page)
            prevButton.setBackgroundColor(Color.CYAN)
            prevButton.setOnClickListener {
                pageNumber -= 1
                findViewById<NestedScrollView>(R.id.scrollView).fullScroll(View.FOCUS_UP)
                showData()
            }
            tableExtensions.addView(prevButton)
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
        tableExtensions.addView(trDate, trParamsSep)
    }
}