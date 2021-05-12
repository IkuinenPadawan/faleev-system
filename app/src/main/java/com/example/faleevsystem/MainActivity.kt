package com.example.faleevsystem

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.*
import java.io.BufferedInputStream
import java.io.BufferedReader
import java.io.InputStreamReader
import java.net.HttpURLConnection
import java.net.URL
import kotlin.concurrent.thread

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        title = "Mars Weather"

        downloadUrlAsync(this, "https://api.nasa.gov/insight_weather/?api_key=DEMO_KEY&feedtype=json&ver=1.0") {
            println(it)
            Log.d("Tag", it)
        }

    }
}

private fun downloadUrlAsync(context: Context, url: String, cb: (String) -> Unit) {
    thread() {
        val myUrl = URL(url)
        val conn = myUrl.openConnection() as HttpURLConnection

        val allText = conn.inputStream.bufferedReader().use(BufferedReader::readText)

        cb(allText)
    }
}



