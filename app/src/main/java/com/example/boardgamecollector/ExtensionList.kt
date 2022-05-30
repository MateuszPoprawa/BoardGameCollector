package com.example.boardgamecollector

import android.os.Bundle
import android.widget.Spinner
import android.widget.TableLayout
import com.google.android.material.appbar.CollapsingToolbarLayout
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.snackbar.Snackbar
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.size
import com.example.boardgamecollector.databinding.ActivityExtensionListBinding

class ExtensionList : AppCompatActivity() {

    private lateinit var binding: ActivityExtensionListBinding
    private var spinner: Spinner? = null
    private var sortList = arrayOf (DBHandler.COLUMN_TITLE, DBHandler.COLUMN_RELEASED)
    private var order = DBHandler.COLUMN_TITLE
    private lateinit var tableGames: TableLayout
    private var extensions: MutableList<Extension> = mutableListOf()
    private var name = "List of extensions"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityExtensionListBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setSupportActionBar(findViewById(R.id.toolbar))
        binding.toolbarLayout.title = title

    }
}