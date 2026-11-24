
package com.example.mobiledevelopmentapp

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.activity.enableEdgeToEdge
import androidx.activity.ComponentActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class RideSelectionActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)

        enableEdgeToEdge()

        setContentView(R.layout.activity_ride_selelction)

        // Receive locations from MainActivity
        val pickupLocation =
            intent.getStringExtra("PICKUP_LOCATION") ?: ""

        val destination =
            intent.getStringExtra("DESTINATION") ?: ""

        val pickupLatitude =
            intent.getDoubleExtra("PICKUP_LATITUDE", 0.0)

        val pickupLongitude =
            intent.getDoubleExtra("PICKUP_LONGITUDE", 0.0)

        val continueButton =
            findViewById<Button>(R.id.continuebutton)

        continueButton.setOnClickListener {

            val intent = Intent(
                this,
                DriverActivity::class.java
            )

            // Pass locations to DriverActivity
            intent.putExtra("PICKUP_LOCATION", pickupLocation)
            intent.putExtra("DESTINATION", destination)

            intent.putExtra("PICKUP_LATITUDE", pickupLatitude)
            intent.putExtra("PICKUP_LONGITUDE", pickupLongitude)

            startActivity(intent)
        }

        ViewCompat.setOnApplyWindowInsetsListener(
            findViewById(R.id.main)
        ) { v, insets ->

            val systemBars = insets.getInsets(
                WindowInsetsCompat.Type.systemBars()
            )

            v.setPadding(
                systemBars.left,
                systemBars.top,
                systemBars.right,
                systemBars.bottom
            )

            insets
        }
    }
}
