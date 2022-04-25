package com.kmm.intermediatefirstsubmission.data.auth.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.kmm.intermediatefirstsubmission.data.core.local_data.SessionPreference
import kotlinx.coroutines.launch

class SessionViewModel(private val _sessionPreference: SessionPreference) : ViewModel() {
    fun getRealtimeToken() = _sessionPreference.getRealtimeToken().asLiveData()

    fun saveToken(token: String) {
        viewModelScope.launch {
            _sessionPreference.saveToken(token)
        }
    }

    fun removeToken() {
        viewModelScope.launch {
            _sessionPreference.removeToken()
        }
    }
}