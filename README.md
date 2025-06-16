📍 Android Location to Address App

This is a simple Android app built with Kotlin that fetches the user's current location using Google Play Services (FusedLocationProviderClient) and performs reverse geocoding to convert the latitude and longitude into a human-readable address using Geocoder.

✨ Features

Fetch current location using GMS

Convert coordinates to address via reverse geocoding

Display address in the UI

Built using Kotlin and MVVM (optional)

Minimal permissions handling

🛠️ Tech Stack

Kotlin

Google Play Services Location

Geocoder API

ViewModel

Jetpack Compose 

🔒 Permissions

Make sure the app has the following permissions in AndroidManifest.xml:

<uses-permission android:name="android.permission.ACCESS_FINE_LOCATION"/>
<uses-permission android:name="android.permission.ACCESS_COARSE_LOCATION"/>
