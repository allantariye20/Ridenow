
package com.example.mobiledevelopmentapp

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.text.font.FontWeight


class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            MaterialTheme {
                TaxiHomeScreen()
            }
        }
    }
}

@Composable
fun TaxiHomeScreen() {
val context= LocalContext.current
    var pickup by remember { mutableStateOf("") }
    var destination by remember { mutableStateOf("") }
    var message by remember { mutableStateOf("") }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        verticalArrangement = Arrangement.spacedBy(18.dp)
    ) {

        Text(
            text = "RideNow",
            style = MaterialTheme.typography.headlineLarge,
            fontWeight = FontWeight.Bold
        )

        Text(
            text = "Where would you like to go?",
            style = MaterialTheme.typography.titleMedium
        )

        OutlinedTextField(
            value = pickup,
            onValueChange = { pickup = it },
            label = { Text("Pickup location") },
            modifier = Modifier.fillMaxWidth()
        )

        OutlinedTextField(
            value = destination,
            onValueChange = { destination = it },
            label = { Text("Destination") },
            modifier = Modifier.fillMaxWidth()
        )

        Button(
            onClick = {

                    message = if (
                        pickup.isNotBlank() &&
                        destination.isNotBlank()
                    ) {

                        "Finding a taxi for your journey!"
                    } else {
                        "Please enter both locations."
                    }
                if (pickup.isNotBlank() && destination.isNotBlank()) {
                    val intent = Intent(context, RideSelectionActivity::class.java)
                    context.startActivity(intent)
                }

            },
            modifier = Modifier
                .fillMaxWidth()
                .height(55.dp),
            shape = RoundedCornerShape(12.dp)
        ) {
            Text("BOOK A TAXI")
        }

        Text(
            text = message,
            style = MaterialTheme.typography.bodyLarge
        )
    }
}
