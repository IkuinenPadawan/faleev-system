package com.example.faleevsystem

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.fasterxml.jackson.databind.ObjectMapper
import org.json.JSONArray
import org.json.JSONObject
import java.io.BufferedReader
import java.net.HttpURLConnection
import java.net.URL
import kotlin.concurrent.thread

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        title = "Mars Weather"

        downloadUrlAsync(this, "https://api.nasa.gov/insight_weather/?api_key=DEMO_KEY&feedtype=json&ver=1.0") {
            Log.d("Tag", it.toString())
            val result : JSONObject = it
            //Log.d("TAG", result.toString())
            val sols : JSONObject = result.getJSONObject("validity_checks")
            val solsChecked : JSONArray = sols.getJSONArray("sols_checked")
            Log.d("TAG", solsChecked.toString())
        }

    }
}

private fun downloadUrlAsync(context: Context, url: String, cb: (JSONObject) -> Unit) {
    thread() {
        val myUrl = URL(url)
        val conn = myUrl.openConnection() as HttpURLConnection

        val allText = conn.inputStream.bufferedReader().use(BufferedReader::readText)

        cb(JSONObject(URL(url).readText()))
    }
}
