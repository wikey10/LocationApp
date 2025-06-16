package com.app.locationapp

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel

class LocationViewModel:ViewModel() {

    private val _locations = mutableStateOf<LocationDetails?>(null)

    val location : State<LocationDetails?> = _locations

    fun updateLocation(newLocationDetails: LocationDetails){
        _locations.value = newLocationDetails
    }


}