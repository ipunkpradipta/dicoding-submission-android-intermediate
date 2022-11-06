package com.ipunkpradipta.submissionstoryapp.ui.auth

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import com.google.android.material.snackbar.Snackbar
import com.ipunkpradipta.submissionstoryapp.R
import com.ipunkpradipta.submissionstoryapp.network.RegisterRequest
import com.ipunkpradipta.submissionstoryapp.databinding.ActivityRegisterBinding

class RegisterActivity : AppCompatActivity() {

    private lateinit var binding: ActivityRegisterBinding
    private lateinit var authViewModel: AuthViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRegisterBinding.inflate(layoutInflater)
        setContentView(binding.root)
        supportActionBar?.title = resources.getString(R.string.title_register)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        authViewModel = AuthViewModel()
        authViewModel.isLoading.observe(this){
            showLoading(it)
        }
        authViewModel.snackbarText.observe(this){
            it.getContentIfNotHandled()?.let { message ->
                showNotification(message)
            }
        }

        authViewModel.isError.observe(this){
            if(!it){
                val intentAuth = Intent(this@RegisterActivity, LoginActivity::class.java)
                startActivity(intentAuth)
            }
        }

        binding.apply {
            registerButton.setOnClickListener{
                it.hideKeyboard()
                val name  = binding.edRegisterName.text
                val password = binding.edLoginPassword.text
                val email = binding.edLoginEmail.text
                if(!email.isNullOrEmpty() && !password.isNullOrEmpty() && !name.isNullOrEmpty()){
                    val request = RegisterRequest(name.toString(),email.toString(),password.toString())
                    authViewModel.register(request)
                }else{
                    Toast.makeText(this@RegisterActivity, resources.getString(R.string.error_input), Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    private fun showNotification(message:String){
        Snackbar.make(
            binding.coordinatorLayout,
            message,
            Snackbar.LENGTH_SHORT
        ).show()
    }

    private fun showLoading(isLoading:Boolean){
        if(isLoading){
            binding.progressBar.visibility = View.VISIBLE
        }else{
            binding.progressBar.visibility = View.GONE
        }
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