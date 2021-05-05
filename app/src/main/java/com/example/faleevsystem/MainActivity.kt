package com.example.faleevsystem

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.ListView

class MainActivity : AppCompatActivity() {

    lateinit var listView : ListView
    lateinit var adapter: ArrayAdapter<String>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        Log.d("TAG", "STARTING")
        listView = findViewById(R.id.listView)

        var arr: Array<String> = arrayOf("green", "red", "blue")

        adapter = ArrayAdapter(this, R.layout.row, R.id.day, arr)
        listView.adapter = adapter

        // list view item click listener
        listView.setOnItemClickListener { parent, view, position, id ->
            //val element = adapter.getItemAtPosition(position) // The item that was clicked
            val intent = Intent(this, WorkoutActivity::class.java)
            Log.d("TAG", "Click")
            startActivity(intent)
        }

        /*listView.onItemClickListener = AdapterView.OnItemClickListener { parent, view, position, id ->
            val selectedItemText = parent.getItemAtPosition(position)
        }*/

    }

    fun openWorkout(view: Context) {
        val intent = Intent(this, WorkoutActivity::class.java)
        startActivityForResult(intent, 12)
    }
}