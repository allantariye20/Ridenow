package com.example.mobiledevelopmentapp

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.activity.ComponentActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import android.content.Intent
import android.net.Uri
import android.widget.Button
import android.widget.Toast
class DriverActivity : ComponentActivity(){
    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_driver)
        val cancelButton =findViewById<Button>(R.id.button2)
        cancelButton.setOnClickListener{Toast.makeText(this, "Your ride has been cancelled.", Toast.LENGTH_SHORT).show()}
        val openMapButton =findViewById<Button>(R.id.openMapButton)
        openMapButton.setOnClickListener {
            val mapIntent = Intent(Intent.ACTION_VIEW, Uri.parse("geo:0,0?q=Cork+City"))
            this.startActivity(mapIntent)
        }

        }
    }
