package com.ipunkpradipta.submissionstoryapp.ui.auth

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.activity.viewModels
import androidx.lifecycle.lifecycleScope
import com.google.android.material.snackbar.Snackbar
import com.ipunkpradipta.submissionstoryapp.R
import com.ipunkpradipta.submissionstoryapp.data.Result
import com.ipunkpradipta.submissionstoryapp.data.remote.RegisterRequest
import com.ipunkpradipta.submissionstoryapp.databinding.ActivityRegisterBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class RegisterActivity : AppCompatActivity() {

    private lateinit var binding: ActivityRegisterBinding
    private val authViewModel: AuthViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRegisterBinding.inflate(layoutInflater)
        setContentView(binding.root)
        supportActionBar?.title = resources.getString(R.string.title_register)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        binding.apply {
            registerButton.setOnClickListener{
                it.hideKeyboard()
                handleRegisterButton()
            }
        }
    }

    private fun handleRegisterButton(){
        val name  = binding.edRegisterName.text
        val password = binding.edLoginPassword.text
        val email = binding.edLoginEmail.text
        if(!email.isNullOrEmpty() && !password.isNullOrEmpty() && !name.isNullOrEmpty()){
            val request = RegisterRequest(name.toString(),email.toString(),password.toString())
            lifecycleScope.launch {
                authViewModel.postRegister(request).observe(this@RegisterActivity){ result->
                    if(result != null){
                        Log.d("RegisterActivity","result: $result")
                        when(result){
                            is Result.Loading -> {
                                binding.progressBar.visibility = View.VISIBLE
                            }
                            is Result.Success -> {
                                binding.progressBar.visibility = View.GONE
                                val response = result.data
                                showNotification(response.message)
                                if(!response.error){
                                    startActivity(Intent(this@RegisterActivity,LoginActivity::class.java))
                                }
                                Log.d("RegisterActivity","response: $response")
                            }
                            is Result.Error -> {
                                binding.progressBar.visibility = View.GONE
                                showNotification(result.error)
                                Log.d("RegisterActivity","responseError: ${result.error}")
                            }
                        }
                    }
                }
            }
        }else{
            Toast.makeText(this@RegisterActivity, resources.getString(R.string.error_input), Toast.LENGTH_SHORT).show()
        }
    }

    private fun showNotification(message:String){
        Snackbar.make(
            binding.coordinatorLayout,
            message,
            Snackbar.LENGTH_SHORT
        ).show()
    }

    private fun View.hideKeyboard() {
        val inputManager = context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        inputManager.hideSoftInputFromWindow(windowToken, 0)
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }
}