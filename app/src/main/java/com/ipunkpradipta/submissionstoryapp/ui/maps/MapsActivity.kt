package com.ipunkpradipta.submissionstoryapp.ui.maps

import android.Manifest
import android.content.pm.PackageManager
import android.location.Geocoder
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.core.content.ContextCompat

import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.LatLngBounds
import com.google.android.gms.maps.model.MarkerOptions
import com.ipunkpradipta.submissionstoryapp.R
import com.ipunkpradipta.submissionstoryapp.databinding.ActivityMapsBinding
import com.ipunkpradipta.submissionstoryapp.ui.ViewModelFactoryMaps
import java.io.IOException
import java.util.*

class MapsActivity : AppCompatActivity(), OnMapReadyCallback {

    private lateinit var mMap: GoogleMap
    private lateinit var binding: ActivityMapsBinding
    private val boundsBuilder = LatLngBounds.Builder()
    private val mapsViewModel: MapsViewModel by viewModels {
        ViewModelFactoryMaps(this)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        binding = ActivityMapsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        val mapFragment = supportFragmentManager
            .findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)
    }

    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap

        mMap.uiSettings.isZoomControlsEnabled = true
        mMap.uiSettings.isIndoorLevelPickerEnabled = true
        mMap.uiSettings.isCompassEnabled = true
        mMap.uiSettings.isMapToolbarEnabled = true

        // Add a marker in Sydney and move the camera
//        val sydney = LatLng(-34.0, 151.0)
//        mMap.addMarker(MarkerOptions().position(sydney).title("Marker in Sydney"))
//        mMap.moveCamera(CameraUpdateFactory.newLatLng(sydney))

        getMyLocation()
        addStoryMarker()
    }

    private val requestPermissionLauncher = registerForActivityResult(
            ActivityResultContracts.RequestPermission()
        ) { isGranted: Boolean ->
            if (isGranted) {
                getMyLocation()
            }
        }

    private fun getMyLocation() {
        if (ContextCompat.checkSelfPermission(
                this.applicationContext,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED
        ) {
            mMap.isMyLocationEnabled = true
        } else {
            requestPermissionLauncher.launch(Manifest.permission.ACCESS_FINE_LOCATION)
        }
    }

    private fun addStoryMarker(){
        mapsViewModel.getAllStories().observe(this){ stories ->
            stories.map {story->
                if(story.lat > -90.000000 && story.lat < 90.000000){
                    val latLng = LatLng(story.lat.toDouble(), story.lon.toDouble())
                    val addressName = getAddressName(story.lat.toDouble(), story.lon.toDouble())
                    mMap.addMarker(MarkerOptions().position(latLng).title(story.name).snippet(addressName))
                    boundsBuilder.include(latLng)
                }

            }
            val bounds: LatLngBounds = boundsBuilder.build()
            mMap.moveCamera(
                CameraUpdateFactory.newLatLngBounds(
                    bounds,
                    resources.displayMetrics.widthPixels,
                    resources.displayMetrics.heightPixels,
                    100
                )
            )
        }
    }

    private fun getAddressName(lat: Double, lon: Double): String? {
        Log.d("MapsActivity", "Lat: $lat")
        var addressName: String? = null
        if(lat < -90.000000|| lat > 90.000000){
            return addressName
        }else{
            val geocoder = Geocoder(this@MapsActivity, Locale.getDefault())
            try {
                val list = geocoder.getFromLocation(lat, lon, 1)
                Log.d("MapsActivity", "list: $list")
                if (list != null && list.size != 0) {
                    addressName = list[0].getAddressLine(0)
                    Log.d("MapsActivity", "getAddressName: $addressName")
                }
            } catch (e: IOException) {
                e.printStackTrace()
            }
            return addressName
        }

    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }

    override fun onContextItemSelected(item: MenuItem): Boolean {
        Log.d("MapsActivity",item.toString())
        when (item.itemId) {
            android.R.id.home -> {
                finish()
                return true
            }
        }
        return super.onContextItemSelected(item)
    }

}