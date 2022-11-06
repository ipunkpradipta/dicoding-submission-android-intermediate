package com.ipunkpradipta.submissionstoryapp.ui

import android.Manifest
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.BitmapFactory
import android.location.Location
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.material.snackbar.Snackbar
import com.ipunkpradipta.submissionstoryapp.MainActivity
import com.ipunkpradipta.submissionstoryapp.R
import com.ipunkpradipta.submissionstoryapp.ui.auth.LoginActivity
import com.ipunkpradipta.submissionstoryapp.databinding.ActivityNewStoryBinding
import com.ipunkpradipta.submissionstoryapp.data.AuthPreferences
import com.ipunkpradipta.submissionstoryapp.MainViewModel
import com.ipunkpradipta.submissionstoryapp.utils.rotateBitmap
import com.ipunkpradipta.submissionstoryapp.utils.uriToFile
import java.io.File

private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "authentication")
class NewStory : AppCompatActivity() {

    private lateinit var binding:ActivityNewStoryBinding
    private val storiesViewModel: StoriesViewModel by viewModels {
        ViewModelFactory(this)
    }
    private lateinit var mainViewModel: MainViewModel
    private lateinit var token: String
    private lateinit var fusedLocationClient: FusedLocationProviderClient
    private var latitude:Double = 0.0
    private var longitude:Double = 0.0

    private var getFile: File? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityNewStoryBinding.inflate(layoutInflater)
        setContentView(binding.root)
        supportActionBar?.title = resources.getString(R.string.title_story_add)

        if (!allPermissionsGranted()) {
            ActivityCompat.requestPermissions(
                this,
                REQUIRED_PERMISSIONS,
                REQUEST_CODE_PERMISSIONS
            )
        }

        val authPref = AuthPreferences.getInstance(dataStore)
        mainViewModel = MainViewModel(authPref)
        mainViewModel.getTokenString().observe(this) { tokenString: String ->
            if (tokenString.isEmpty()) {
                val intentAuth = Intent(this@NewStory, LoginActivity::class.java)
                intentAuth.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK)
                startActivity(intentAuth)
            }else{
                token = tokenString
            }
        }

        storiesViewModel.isLoading.observe(this){
            showLoading(it)
        }

        storiesViewModel.snackbarText.observe(this){
            it.getContentIfNotHandled()?.let { message ->
                showNotification(message)
            }
        }

        storiesViewModel.isError.observe(this){
            if(!it){
                val intent = Intent(this@NewStory, MainActivity::class.java)
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK)
                startActivity(intent)
            }
        }

        binding.btnCamera.setOnClickListener{ startCameraX() }
        binding.btnGallery.setOnClickListener{ startGallery() }
        binding.btnUpload.setOnClickListener{ uploadImage() }

        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)

        getMyLocation()
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
            fusedLocationClient.lastLocation.addOnSuccessListener { location:Location? ->
                if(location != null){
                    latitude = location.latitude
                    longitude = location.longitude
                    binding.tvLocationValue.text = "Lat : ${location.latitude} Lon : ${location.longitude}"
                }else{
                    Toast.makeText(
                        this@NewStory,
                        "Location is not found. Try Again",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
        } else {
            requestPermissionLauncher.launch(Manifest.permission.ACCESS_FINE_LOCATION)
        }
    }

    private fun startCameraX() {
        val intent = Intent(this, CameraActivity::class.java)
        launcherIntentCameraX.launch(intent)
    }

    private val launcherIntentCameraX = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) {
        if (it.resultCode == CAMERA_X_RESULT) {
            val myFile = it.data?.getSerializableExtra("picture") as File
            val isBackCamera = it.data?.getBooleanExtra("isBackCamera", true) as Boolean

            getFile = myFile

            val result = rotateBitmap(
                BitmapFactory.decodeFile(myFile.path),
                isBackCamera
            )

            binding.previewImage.setImageBitmap(result)
        }
    }

    private  fun startGallery(){
        val intent = Intent()
        intent.action = Intent.ACTION_GET_CONTENT
        intent.type = "image/*"
        val chooser = Intent.createChooser(intent, "Choose a Picture")
        launcherIntentGallery.launch(chooser)
    }

    private val launcherIntentGallery = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) { result ->
        if (result.resultCode == RESULT_OK) {
            val selectedImg: Uri = result.data?.data as Uri

            val myFile = uriToFile(selectedImg, this@NewStory)

            getFile = myFile

            binding.previewImage.setImageURI(selectedImg)
        }
    }

    private fun uploadImage() {
        if (getFile != null) {
            Log.d("TOKEN",token)
            storiesViewModel.postStory(token, getFile!!,binding.edtDescription.text.toString(),latitude.toString(),longitude.toString())
        } else {
            Toast.makeText(this@NewStory, resources.getString(R.string.error_story_add), Toast.LENGTH_SHORT).show()
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == REQUEST_CODE_PERMISSIONS) {
            if (!allPermissionsGranted()) {
                Toast.makeText(
                    this,
                    resources.getString(R.string.error_permission),
                    Toast.LENGTH_SHORT
                ).show()
                finish()
            }
        }
    }

    private fun allPermissionsGranted() = REQUIRED_PERMISSIONS.all {
        ContextCompat.checkSelfPermission(baseContext, it) == PackageManager.PERMISSION_GRANTED
    }

    private fun showLoading(isLoading:Boolean){
        if(isLoading){
            binding.progressBar.visibility = View.VISIBLE
        }else{
            binding.progressBar.visibility = View.GONE
        }
    }

    private fun showNotification(message:String){
        Snackbar.make(
            binding.coordinatorLayout,
            message,
            Snackbar.LENGTH_SHORT
        ).show()
    }

    companion object {
        const val CAMERA_X_RESULT = 200

        private val REQUIRED_PERMISSIONS = arrayOf(Manifest.permission.CAMERA)
        private const val REQUEST_CODE_PERMISSIONS = 10
    }
}