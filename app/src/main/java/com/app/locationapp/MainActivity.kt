package com.app.locationapp

import android.content.Context
import android.os.Bundle
import android.Manifest
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContract
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.core.app.ActivityCompat
import androidx.lifecycle.viewmodel.compose.viewModel
import com.app.locationapp.ui.theme.LocationAppTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            val viewModel :LocationViewModel = viewModel()
            LocationAppTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    MyApp(modifier = Modifier.padding(innerPadding),viewModel)
                }
            }
        }
    }
}

@Composable
fun MyApp(modifier: Modifier,viewModel: LocationViewModel){
    val context = LocalContext.current
    val locationUtils = LocationUtils(context)
    LocationDisplay(
        context = context,
        location = locationUtils,
        viewModel = viewModel
    )
}


@Composable
fun LocationDisplay(location:LocationUtils,
                    context: Context,modifier: Modifier=Modifier,
                    viewModel: LocationViewModel
                    ){

    val locationsModel = viewModel.location.value

    val address = if (locationsModel != null) {
        location.reverseGeoLocation(locationsModel)
    } else {
        "Address not found!"
    }

    val requestPermissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestMultiplePermissions(),
        onResult ={
            permissions-> if(permissions[Manifest.permission.ACCESS_COARSE_LOCATION] == true
            && permissions[Manifest.permission.ACCESS_FINE_LOCATION] == true
                ){
                //i have access
                location.requestLocationUpdates(viewModel)
            }
            else{
                //ASK FOR Permission
                val rationalRequired =  ActivityCompat.shouldShowRequestPermissionRationale(
                    context as MainActivity,
                    Manifest.permission.ACCESS_FINE_LOCATION
                ) || ActivityCompat.shouldShowRequestPermissionRationale(
                    context as MainActivity,
                    Manifest.permission.ACCESS_COARSE_LOCATION
                )
            if (rationalRequired){
                Toast.makeText(context,"Location permission is required for this feature",Toast.LENGTH_LONG).show()
            }
            else{
                Toast.makeText(context,"Location permission is required pls enable in the Android settings",Toast.LENGTH_LONG).show()
            }
            }
        }
    )

 Column(modifier = Modifier.fillMaxSize(),
     horizontalAlignment = Alignment.CenterHorizontally,
     verticalArrangement = Arrangement.Center
     ) {
     if (locationsModel!=null){
         Text("Lat and Long : ${locationsModel.latitude}-${locationsModel.longitude}")
         Text("Address : ${address.toString()}")
     }
     else{
         Text("NO LOCATION ")
     }
     Button(
         onClick = {
             if (location.hasLocationPermission(context)){
                 //PERMISSION GRANTED
                 location.requestLocationUpdates(viewModel)
             }else{
                 //REQUEST Permission
                 requestPermissionLauncher.launch(
                     arrayOf(
                         Manifest.permission.ACCESS_COARSE_LOCATION,
                         Manifest.permission.ACCESS_FINE_LOCATION
                     )
                 )
             }
         }
     ) {
         Text("Get Location")
     }

 }
}