
package com.example.mobiledevelopmentapp

import android.location.Geocoder
import java.util.Locale
import android.os.Handler
import android.os.Looper
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
import android.Manifest
import android.content.pm.PackageManager
import com.google.android.gms.location.LocationServices
import com.google.android.gms.location.Priority
import com.google.android.gms.tasks.CancellationTokenSource
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat

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

    val context = LocalContext.current

    var pickup by remember { mutableStateOf("") }
    var destination by remember { mutableStateOf("") }
    var message by remember { mutableStateOf("") }
    var isLoading by remember { mutableStateOf(false) }

    // Store the actual GPS coordinates
    var pickupLatitude by remember { mutableStateOf(0.0) }
    var pickupLongitude by remember { mutableStateOf(0.0) }

    val fusedLocationClient = remember {
        LocationServices.getFusedLocationProviderClient(context)
    }

    fun getCurrentLocation() {

        isLoading = true
        message = "Detecting your location..."

        try {
            fusedLocationClient.getCurrentLocation(
                Priority.PRIORITY_HIGH_ACCURACY,
                CancellationTokenSource().token
            ).addOnSuccessListener { location ->

                isLoading = false

                if (location != null) {

                    val latitude = location.latitude
                    val longitude = location.longitude

                    // Save GPS coordinates
                    pickupLatitude = latitude
                    pickupLongitude = longitude

                    message = "Finding your street address..."

                    Thread {
                        try {

                            val geocoder = Geocoder(
                                context,
                                Locale.getDefault()
                            )

                            val addresses = geocoder.getFromLocation(
                                latitude,
                                longitude,
                                1
                            )

                            val address = addresses?.firstOrNull()
                            val detectedAddress = address?.getAddressLine(0)

                            Handler(Looper.getMainLooper()).post {

                                if (!detectedAddress.isNullOrBlank()) {

                                    pickup = detectedAddress
                                    message = "Pickup location detected successfully!"

                                } else {

                                    pickup = "Lat: $latitude, Lon: $longitude"
                                    message = "Address unavailable. GPS coordinates saved."
                                }
                            }

                        } catch (e: Exception) {

                            Handler(Looper.getMainLooper()).post {

                                pickup = "Lat: $latitude, Lon: $longitude"
                                message = "Could not find address. GPS coordinates saved."
                            }
                        }

                    }.start()

                } else {
                    message = "Unable to detect location. Try again."
                }

            }.addOnFailureListener {

                isLoading = false
                message = "Location failed. Please try again."
            }

        } catch (e: SecurityException) {

            isLoading = false
            message = "Location permission is required."
        }
    }

    val locationPermissionLauncher =
        rememberLauncherForActivityResult(
            ActivityResultContracts.RequestMultiplePermissions()
        ) { permissions ->

            val granted =
                permissions[Manifest.permission.ACCESS_FINE_LOCATION] == true ||
                        permissions[Manifest.permission.ACCESS_COARSE_LOCATION] == true

            if (granted) {
                getCurrentLocation()
            } else {
                message = "Please allow location access."
            }
        }

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

        Button(
            onClick = {

                val fineGranted = ContextCompat.checkSelfPermission(
                    context,
                    Manifest.permission.ACCESS_FINE_LOCATION
                ) == PackageManager.PERMISSION_GRANTED

                val coarseGranted = ContextCompat.checkSelfPermission(
                    context,
                    Manifest.permission.ACCESS_COARSE_LOCATION
                ) == PackageManager.PERMISSION_GRANTED

                if (fineGranted || coarseGranted) {
                    getCurrentLocation()
                } else {
                    locationPermissionLauncher.launch(
                        arrayOf(
                            Manifest.permission.ACCESS_FINE_LOCATION,
                            Manifest.permission.ACCESS_COARSE_LOCATION
                        )
                    )
                }
            },
            modifier = Modifier.fillMaxWidth(),
            enabled = !isLoading
        ) {
            Text("USE MY CURRENT LOCATION")
        }

        OutlinedTextField(
            value = pickup,
            onValueChange = { pickup = it },
            label = { Text("Pickup location") },
            modifier = Modifier.fillMaxWidth(),
            readOnly = true
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

                    val intent = Intent(
                        context,
                        RideSelectionActivity::class.java
                    )

                    // Send pickup and destination
                    intent.putExtra("PICKUP_LOCATION", pickup)
                    intent.putExtra("DESTINATION", destination)

                    // Send actual GPS coordinates
                    intent.putExtra("PICKUP_LATITUDE", pickupLatitude)
                    intent.putExtra("PICKUP_LONGITUDE", pickupLongitude)

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
