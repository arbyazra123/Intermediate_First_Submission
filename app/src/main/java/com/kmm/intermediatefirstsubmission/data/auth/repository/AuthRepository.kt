package com.kmm.intermediatefirstsubmission.data.auth.repository

import com.kmm.intermediatefirstsubmission.data.auth.model.request.LoginRequest
import com.kmm.intermediatefirstsubmission.data.auth.model.request.RegisterRequest
import com.kmm.intermediatefirstsubmission.data.network.ApiService

class AuthRepository(private val api: ApiService) {
    fun login(body: LoginRequest) = api.login(body)
    fun register(body: RegisterRequest) =
            api.register(
                body,
            )
}