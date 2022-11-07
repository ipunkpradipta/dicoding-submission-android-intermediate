package com.ipunkpradipta.submissionstoryapp

import com.ipunkpradipta.submissionstoryapp.data.remote.response.DefaultResponse
import com.ipunkpradipta.submissionstoryapp.network.LoginRequest
import com.ipunkpradipta.submissionstoryapp.network.LoginResponse
import com.ipunkpradipta.submissionstoryapp.network.LoginResult
import com.ipunkpradipta.submissionstoryapp.network.RegisterRequest

object Dummy {
    fun generateResponseRegister(): DefaultResponse {
        return DefaultResponse(false,"User Created")
    }

    fun generateRequestRegister():RegisterRequest{
        return RegisterRequest("ipung","ipung.munandar@gmail.com","123456")
    }

    fun generateRequestLogin():LoginRequest{
        return LoginRequest("bala@gmail.com","123456")
    }

    fun generateResponseLogin():LoginResponse{
        val loginResult = LoginResult("ini_id_dummy","ipung","tokenNyaKuRAngPanjang")
        return LoginResponse(false,"Success", loginResult)
    }
}