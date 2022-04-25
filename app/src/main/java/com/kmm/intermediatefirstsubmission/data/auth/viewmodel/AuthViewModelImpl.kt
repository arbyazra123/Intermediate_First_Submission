package com.kmm.intermediatefirstsubmission.data.auth.viewmodel

import androidx.lifecycle.*
import com.kmm.intermediatefirstsubmission.data.auth.model.request.LoginRequest
import com.kmm.intermediatefirstsubmission.data.auth.model.request.RegisterRequest
import com.kmm.intermediatefirstsubmission.data.auth.repository.AuthRepository
import com.kmm.intermediatefirstsubmission.data.core.StateHandler
import com.kmm.intermediatefirstsubmission.data.core.model.BaseResponse
import com.kmm.intermediatefirstsubmission.utility.ErrorParser.Companion.parse
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class AuthViewModelImpl(
    private val authRepository: AuthRepository,
) : AuthViewModel() {

    override fun login(email: String, password: String) {
        loginEvent.postValue(StateHandler.Loading())
        viewModelScope.launch {
            val request = LoginRequest(email, password)

            authRepository.login(request).enqueue(object : Callback<BaseResponse> {
                override fun onResponse(
                    call: Call<BaseResponse>,
                    response: Response<BaseResponse>
                ) {

                    response.body().let { body: BaseResponse? ->
                        println(response.message())
                        println(response.body()?.error)
                        if (response.isSuccessful) {
                            loginEvent.postValue(StateHandler.Success(body))
                        } else {
                            loginEvent.postValue(
                                StateHandler.Error(
                                    parse(response).message

                                )
                            )
                        }
                    }


                }

                override fun onFailure(call: Call<BaseResponse>, t: Throwable) {
                    loginEvent.postValue(StateHandler.Error(t.message))
                }

            })
        }
    }

    override fun register(email: String, password: String, name: String) {
        registerEvent.postValue(StateHandler.Loading())
        viewModelScope.launch {
            val request = RegisterRequest(email, name, password)

            authRepository.register(request).enqueue(object : Callback<BaseResponse> {
                override fun onResponse(
                    call: Call<BaseResponse>,
                    response: Response<BaseResponse>
                ) {

                    response.body().let { body ->
                        println(response.message())
                        println(response.body()?.error)
                        if (response.isSuccessful) {
                            registerEvent.postValue(StateHandler.Success(body))
                        } else {
                            registerEvent.postValue(
                                StateHandler.Error(
                                    parse(response).message ?: "unknown error"
                                )
                            )
                        }
                    }


                }

                override fun onFailure(call: Call<BaseResponse>, t: Throwable) {
                    registerEvent.postValue(StateHandler.Error(t.message))
                }

            })
        }
    }
}