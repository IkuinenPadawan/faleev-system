package com.example.faleevsystem

import android.content.Context
import android.content.pm.ApplicationInfo
import android.content.pm.PackageManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.animation.AnimationUtils
import android.widget.ImageView
import android.widget.TextView
import androidx.core.app.ActivityCompat
import com.bumptech.glide.Glide
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import org.json.JSONObject
import java.net.URL
import kotlin.concurrent.thread

class MainActivity : AppCompatActivity() {
    lateinit var fusedLocationProviderClient: FusedLocationProviderClient
    var longitude: String = "0.0"
    var latitude: String = "0.0"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        title = "Futuristic Weather Dashboard"

        // Get secret API KEY
        val ai: ApplicationInfo = applicationContext.packageManager
            .getApplicationInfo(applicationContext.packageName, PackageManager.GET_META_DATA)
        val API_KEY = ai.metaData["keyValue"]

        // Find views
        val temperatureTextView: TextView = findViewById(R.id.temperature)
        val locationTextView: TextView = findViewById(R.id.location)
        val weatherIcon: ImageView = findViewById(R.id.weathericon)
        val outerCircle: ImageView = findViewById(R.id.outercircle)
        val inner: ImageView = findViewById(R.id.innercircle)

        // Initialize fusedLocationProviderClient
        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this)

        // Fetch location of the device
        fetchLocation() {
            // Fetch weather data
            downloadUrlAsync(
                this,
                "https://api.openweathermap.org/data/2.5/weather?lat=${latitude}&lon=${longitude}&units=metric&appid=${API_KEY.toString()}"
            ) {
                val result = JSONObject(it)
                val weather = result.getJSONObject("main")
                val currentTemp = weather.getDouble("temp")
                val location = result.getString("name")
                val icon = result.getJSONArray("weather").getJSONObject(0).getString("icon")
                // Update UI
                runOnUiThread {
                    temperatureTextView.text = currentTemp.toInt().toString() + "°C"
                    locationTextView.text = location
                    Glide.with(this).load("https://openweathermap.org/img/w/$icon.png").into(weatherIcon)
                }
            }
        }

        // Declare Animations
        val fadeIn = AnimationUtils.loadAnimation(this, R.anim.fade_in)
        val rotation = AnimationUtils.loadAnimation(this, R.anim.rotation)
        val counterRotation = AnimationUtils.loadAnimation(this, R.anim.rotation_counter)

        // Start animations
        temperatureTextView.startAnimation(fadeIn)
        weatherIcon.startAnimation(fadeIn)
        locationTextView.startAnimation(fadeIn)
        outerCircle.startAnimation(rotation)
        inner.startAnimation(counterRotation)
    }

    // Ask user permissions and fetch location of the device
    private fun fetchLocation(callback: () -> Unit) {
        val task = fusedLocationProviderClient.lastLocation
        if(ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION)
            != PackageManager.PERMISSION_GRANTED && ActivityCompat.
                checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED
        ){
            ActivityCompat.requestPermissions(this, arrayOf(android.Manifest.permission.ACCESS_FINE_LOCATION), 101)
            return
        }
        // If permissions given, get location
        // If not, use default location
        task.addOnSuccessListener {
            if(it != null) {
                latitude = it.latitude.toString()
                longitude = it.longitude.toString()
                callback()
            } else {
                callback()
            }
        }
    }

    // Fetch content of the URL as String
    private fun downloadUrlAsync(context: Context, url: String, cb: (String) -> Unit) {
        thread() {
            cb(URL(url).readText())
        }
    }
}
