package com.example.faleevsystem

import android.content.Context
import android.content.pm.ApplicationInfo
import android.content.pm.PackageManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.animation.AnimationUtils
import android.widget.ImageView
import android.widget.TextView
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import org.json.JSONObject
import java.net.URL
import java.util.jar.Manifest
import kotlin.concurrent.thread

class MainActivity : AppCompatActivity() {
    lateinit var fusedLocationProviderClient: FusedLocationProviderClient
    var longitude: String = "0.0"
    var latitude: String = "0.0"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        title = "Weather"

        // Get secret API KEY
        val ai: ApplicationInfo = applicationContext.packageManager
            .getApplicationInfo(applicationContext.packageName, PackageManager.GET_META_DATA)
        val API_KEY = ai.metaData["keyValue"]

        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this)

        fetchLocation() {
            // Fetch weather data and display
            downloadUrlAsync(
                this,
                "https://api.openweathermap.org/data/2.5/weather?lat=${latitude}&lon=${longitude}&units=metric&appid=${API_KEY.toString()}"
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
        Log.d("TAG", longitude)
        Log.d("TAG", latitude)

        // Find views
        val temperatureTextView: TextView = findViewById(R.id.temperature)
        val locationTextView: TextView = findViewById(R.id.location)
        val outerCircle: ImageView = findViewById(R.id.outercircle)
        val inner: ImageView = findViewById(R.id.innercircle)

        // Declare Animations
        val fadeIn = AnimationUtils.loadAnimation(this, R.anim.animation)
        val rotation = AnimationUtils.loadAnimation(this, R.anim.rotation)
        val counterRotation = AnimationUtils.loadAnimation(this, R.anim.rotation_counter)

        // Start animations
        temperatureTextView.startAnimation(fadeIn)
        locationTextView.startAnimation(fadeIn)
        outerCircle.startAnimation(rotation)
        inner.startAnimation(counterRotation)

    }

    private fun fetchLocation(cb: () -> Unit) {
        val task = fusedLocationProviderClient.lastLocation
        if(ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION)
            != PackageManager.PERMISSION_GRANTED && ActivityCompat.
                checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED
        ){
            ActivityCompat.requestPermissions(this, arrayOf(android.Manifest.permission.ACCESS_FINE_LOCATION), 101)
            return
        }
        task.addOnSuccessListener {
            if(it != null) {
                latitude = it.latitude.toString()
                longitude = it.longitude.toString()
                Log.d("TAG", it.toString())
                cb()
            }
        }

    }
    private fun downloadUrlAsync(context: Context, url: String, cb: (String) -> Unit) {
        thread() {
            cb(URL(url).readText())
        }
    }

}


