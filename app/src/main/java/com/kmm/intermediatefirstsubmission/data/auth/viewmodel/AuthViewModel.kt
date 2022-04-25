package com.kmm.intermediatefirstsubmission.data.auth.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.kmm.intermediatefirstsubmission.data.core.StateHandler
import com.kmm.intermediatefirstsubmission.data.core.model.BaseResponse

abstract class AuthViewModel : ViewModel() {
    protected val loginEvent: MutableLiveData<StateHandler<BaseResponse>> = MutableLiveData()
    val loginState: LiveData<StateHandler<BaseResponse>> = loginEvent
    protected val registerEvent: MutableLiveData<StateHandler<BaseResponse>> = MutableLiveData()
    val registerState: LiveData<StateHandler<BaseResponse>> = registerEvent
    abstract fun login(email: String, password: String)
    abstract fun register(email: String, password: String, name: String)
}