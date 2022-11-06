package com.ipunkpradipta.submissionstoryapp

import com.ipunkpradipta.submissionstoryapp.network.RegisterRequest

object RequestDummy {
    fun generateRequestRegister(): RegisterRequest {
        return RegisterRequest("ipung","ipung@gmail.com","123456")
    }
}