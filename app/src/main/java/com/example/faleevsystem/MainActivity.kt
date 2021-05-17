package com.example.faleevsystem

import android.content.Context
import android.content.pm.ApplicationInfo
import android.content.pm.PackageManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import android.widget.ImageView
import android.widget.TextView
import org.json.JSONObject
import org.w3c.dom.Text
import java.net.URL
import kotlin.concurrent.thread

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        title = "Weather"

        // Get secret API KEY
        val ai: ApplicationInfo = applicationContext.packageManager
            .getApplicationInfo(applicationContext.packageName, PackageManager.GET_META_DATA)
        val API_KEY = ai.metaData["keyValue"]

        // Declare Animation
        val joku = AnimationUtils.loadAnimation(this, R.anim.animation)
        val temp: TextView = findViewById(R.id.temperature)
        val temp1: TextView = findViewById(R.id.location)
        // Set animation
        temp.startAnimation(joku)
        temp1.startAnimation(joku)

        val rotation = AnimationUtils.loadAnimation(this, R.anim.rotation)
        val outerCircle: ImageView = findViewById(R.id.outercircle)
        outerCircle.startAnimation(rotation)

        val counterRotation = AnimationUtils.loadAnimation(this, R.anim.rotation_counter)
        val inner: ImageView = findViewById(R.id.innercircle)
        inner.startAnimation(counterRotation)

       downloadUrlAsync(
           this,
           "https://api.openweathermap.org/data/2.5/weather?q=Tampere&units=metric&appid=${API_KEY.toString()}"
       ) {
            Log.d("Tag", it)
            val result : JSONObject = JSONObject(it)
            val weather = result.getJSONObject("main")
            val currentTemp = weather.getDouble("temp")
            val location = result.getString("name")
            Log.d("TAG", currentTemp.toString())
            runOnUiThread {
                findViewById<TextView>(R.id.temperature).text = currentTemp.toInt().toString() + "°C"
                findViewById<TextView>(R.id.location).text = location
            }
        }

    }
}

private fun downloadUrlAsync(context: Context, url: String, cb: (String) -> Unit) {
    thread() {
        cb(URL(url).readText())
    }
}
