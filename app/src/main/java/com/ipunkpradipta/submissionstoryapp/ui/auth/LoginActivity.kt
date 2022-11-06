package com.ipunkpradipta.submissionstoryapp.ui.auth

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.core.app.ActivityOptionsCompat
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import com.google.android.material.snackbar.Snackbar
import com.ipunkpradipta.submissionstoryapp.data.AuthPreferences
import com.ipunkpradipta.submissionstoryapp.MainActivity
import com.ipunkpradipta.submissionstoryapp.R
import com.ipunkpradipta.submissionstoryapp.network.LoginRequest
import com.ipunkpradipta.submissionstoryapp.databinding.ActivityLoginBinding
import com.ipunkpradipta.submissionstoryapp.MainViewModel

private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "authentication")

class LoginActivity : AppCompatActivity() {

    private lateinit var binding: ActivityLoginBinding
    private lateinit var authViewModel: AuthViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)
        supportActionBar?.title = resources.getString(R.string.title_login)

        binding.apply {
            registerButton.setOnClickListener {
                handleClickButtonRegist()
            }
            loginButton.setOnClickListener {
                it.hideKeyboard()
                handleClickButtonLogin()
            }
        }

        authViewModel = AuthViewModel()

        authViewModel.isLoading.observe(this) {
            showLoading(it)
        }

        authViewModel.snackbarText.observe(this) {
            it.getContentIfNotHandled()?.let { message ->
                showNotification(message)
            }
        }

        authViewModel.loginToken.observe(this) {
            handleOnSaveToken(it)
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
            authViewModel.login(request)
        } else {
            Toast.makeText(this@LoginActivity,
                resources.getString(R.string.error_input),
                Toast.LENGTH_SHORT).show()
        }
    }

    private fun handleOnSaveToken(tokenString: String) {
        val authPref = AuthPreferences.getInstance(dataStore)
        val mainViewModel = MainViewModel(authPref)
//            ViewModelProvider(this, ViewModelFactory(authPref))[MainViewModel::class.java]
        if (tokenString.isEmpty()) {
            Log.d("TOKEN_KOSONG", tokenString)
        } else {
            Log.d("TOKEN_ISI", tokenString)
            mainViewModel.setTokenString(tokenString)
            val intent = Intent(this@LoginActivity, MainActivity::class.java)
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK)
            startActivity(intent,
                ActivityOptionsCompat.makeSceneTransitionAnimation(this@LoginActivity).toBundle())
        }
    }

    private fun showLoading(isLoading: Boolean) {
        if (isLoading) {
            binding.progressBar.visibility = View.VISIBLE
        } else {
            binding.progressBar.visibility = View.GONE
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