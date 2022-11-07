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
import androidx.core.app.ActivityOptionsCompat
import androidx.lifecycle.lifecycleScope
import com.google.android.material.snackbar.Snackbar
import com.ipunkpradipta.submissionstoryapp.MainActivity
import com.ipunkpradipta.submissionstoryapp.R
import com.ipunkpradipta.submissionstoryapp.network.LoginRequest
import com.ipunkpradipta.submissionstoryapp.databinding.ActivityLoginBinding
import com.ipunkpradipta.submissionstoryapp.data.Result
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class LoginActivity : AppCompatActivity() {

    private lateinit var binding: ActivityLoginBinding
    private val authViewModel: AuthViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)
        supportActionBar?.title = resources.getString(R.string.title_login)

        authViewModel.getTokenAuth().observe(this@LoginActivity){result->
            Log.d("LoginActivity","tokenResult:$result")
            if(result.isNotEmpty()){
                val intentAuth = Intent(this@LoginActivity, MainActivity::class.java)
                intentAuth.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK)
                startActivity(intentAuth,ActivityOptionsCompat.makeSceneTransitionAnimation(this@LoginActivity).toBundle())
            }
        }

        binding.apply {
            registerButton.setOnClickListener {
                handleClickButtonRegist()
            }
            loginButton.setOnClickListener {
                it.hideKeyboard()
                handleClickButtonLogin()
            }
        }
    }

    private fun handleClickButtonRegist() {
        val intent = Intent(this@LoginActivity, RegisterActivity::class.java)
        startActivity(intent,ActivityOptionsCompat.makeSceneTransitionAnimation(this@LoginActivity).toBundle())
    }

    private fun handleClickButtonLogin() {
        val email = binding.edLoginEmail.text
        val password = binding.edLoginPassword.text
        if (!email.isNullOrEmpty() && !password.isNullOrEmpty()) {
            val request = LoginRequest(email.toString(), password.toString())
            lifecycleScope.launch{
                authViewModel.postLogin(request).observe(this@LoginActivity){result->
                    Log.d("LoginActivity","result:$result")
                    if(result != null){
                        when(result){
                            is Result.Loading -> {
                                binding.progressBar.visibility = View.VISIBLE
                            }
                            is Result.Error -> {
                                binding.progressBar.visibility = View.GONE
                                showNotification(result.error)
                            }
                            is Result.Success -> {
                                binding.progressBar.visibility = View.GONE
                                showNotification(result.data.message)
                                handleOnSaveToken(result.data.loginResult.token)
                            }
                        }
                    }
                }
            }
        } else {
            Toast.makeText(this@LoginActivity,
                resources.getString(R.string.error_input),
                Toast.LENGTH_SHORT).show()
        }
    }

    private fun handleOnSaveToken(tokenString: String) {
        lifecycleScope.launch{
            authViewModel.saveTokenAuth(tokenString)
        }
    }

    private fun showNotification(message: String) {
        Snackbar.make(
            binding.coordinatorLayout,
            message,
            Snackbar.LENGTH_SHORT
        ).show()
    }

    private fun View.hideKeyboard() {
        val inputManager =
            context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        inputManager.hideSoftInputFromWindow(windowToken, 0)
    }
}