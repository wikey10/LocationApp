package com.app.locationapp

import android.Manifest
import android.annotation.SuppressLint
import android.content.Context
import android.content.pm.PackageManager
import android.location.Address
import android.location.Geocoder
import android.os.Looper
import androidx.core.content.ContextCompat
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationResult
import com.google.android.gms.location.LocationServices
import com.google.android.gms.location.Priority
import com.google.android.gms.maps.model.LatLng
import java.util.Locale

class LocationUtils(val context: Context) {

    private val _fusedLocationClient : FusedLocationProviderClient
    = LocationServices.getFusedLocationProviderClient(context)

    @SuppressLint("MissingPermission")
    fun requestLocationUpdates(viewModel: LocationViewModel){
        val locationCallBack = object : LocationCallback(){
            override fun onLocationResult(locationResult: LocationResult) {
                super.onLocationResult(locationResult)
                locationResult.lastLocation?.let {
                    val location = LocationDetails(
                        latitude = it.latitude,
                        longitude = it.longitude
                    )
                    viewModel.updateLocation(location)
                }
            }
        }

        val locationRequest = LocationRequest.Builder(Priority.PRIORITY_HIGH_ACCURACY,
            1000).build()

        _fusedLocationClient.requestLocationUpdates(locationRequest,locationCallBack,
            Looper.getMainLooper()
            )
    }

    fun reverseGeoLocation(locationDetails: LocationDetails):String{
        val geocoder = Geocoder(context, Locale.getDefault())
        val coordinates = LatLng(locationDetails.latitude,
            locationDetails.longitude)
        val addresses :MutableList<Address>? = geocoder
            .getFromLocation(coordinates.latitude,coordinates.longitude,
                1)
        return if(addresses?.isNotEmpty()==true){
            println("address-${addresses[0].getAddressLine(0)}")
            addresses[0].getAddressLine(0)
        }
        else{
            println("not found")
            "Address not found!"
        }
    }


    fun hasLocationPermission(context: Context):Boolean{
        return ContextCompat.checkSelfPermission(
            context,Manifest.permission.ACCESS_FINE_LOCATION
        ) == PackageManager.PERMISSION_GRANTED
                && ContextCompat.checkSelfPermission(
            context,Manifest.permission.ACCESS_COARSE_LOCATION
        ) == PackageManager.PERMISSION_GRANTED
    }
}