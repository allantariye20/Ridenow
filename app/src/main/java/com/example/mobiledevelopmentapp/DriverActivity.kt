
package com.example.mobiledevelopmentapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import android.content.Intent
import android.net.Uri
import android.widget.Button
import android.widget.Toast

class DriverActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)

        // Receive pickup and destination
        val pickupLocation =
            intent.getStringExtra("PICKUP_LOCATION") ?: ""

        val destination =
            intent.getStringExtra("DESTINATION") ?: ""

        // Receive actual GPS coordinates
        val pickupLatitude =
            intent.getDoubleExtra("PICKUP_LATITUDE", 0.0)

        val pickupLongitude =
            intent.getDoubleExtra("PICKUP_LONGITUDE", 0.0)

        setContentView(R.layout.activity_driver)

        // Cancel ride button
        val cancelButton =
            findViewById<Button>(R.id.button2)

        cancelButton.setOnClickListener {

            Toast.makeText(
                this,
                "Your ride has been cancelled.",
                Toast.LENGTH_SHORT
            ).show()
        }

        // Open pickup location in Google Maps
        val openMapButton =
            findViewById<Button>(R.id.openMapButton)

        openMapButton.setOnClickListener {

            if (pickupLatitude == 0.0 && pickupLongitude == 0.0) {

                Toast.makeText(
                    this,
                    "Pickup location unavailable.",
                    Toast.LENGTH_SHORT
                ).show()

            } else {

                val mapUri = Uri.parse(
                    "https://www.google.com/maps/search/?api=1&query=$pickupLatitude%2C$pickupLongitude"
                )

                val mapIntent = Intent(
                    Intent.ACTION_VIEW,
                    mapUri
                )

                startActivity(mapIntent)
            }
        }
    }
}
