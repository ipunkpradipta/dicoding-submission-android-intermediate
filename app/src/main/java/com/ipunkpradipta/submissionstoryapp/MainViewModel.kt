package com.ipunkpradipta.submissionstoryapp

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.ipunkpradipta.submissionstoryapp.data.AuthPreferences
import kotlinx.coroutines.launch

class MainViewModel(private val pref: AuthPreferences):ViewModel() {

    fun getTokenString() : LiveData<String>{
        return pref.getToken().asLiveData()
    }

    fun setTokenString(tokenString:String){
        viewModelScope.launch {
            pref.saveToken(tokenString)
        }
    }

    fun removeTokenString(){
        viewModelScope.launch {
            pref.removeToken()
        }
    }
}