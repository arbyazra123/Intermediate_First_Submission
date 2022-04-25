package com.kmm.intermediatefirstsubmission.data.core.local_data

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.map

class SessionPreference private constructor(private val dataStore: DataStore<Preferences>) {

    private val _token = stringPreferencesKey("token")

    suspend fun getToken(): String? {
        return dataStore.data.map { preferences ->
            preferences[_token] ?: ""
        }.firstOrNull()
    }

    fun getRealtimeToken(): Flow<String> {
        return dataStore.data.map { preferences ->
            preferences[_token] ?: ""
        }
    }

    suspend fun saveToken(token: String) {
        dataStore.edit { preferences ->
            preferences[_token] = token
        }
    }

    suspend fun removeToken() {
        dataStore.edit { preferences ->
            preferences.remove(_token)
        }
    }

    companion object {
        @Volatile
        private var INSTANCE: SessionPreference? = null

        fun getInstance(dataStore: DataStore<Preferences>): SessionPreference {
            return INSTANCE ?: synchronized(this) {
                val instance = SessionPreference(dataStore)
                INSTANCE = instance
                instance
            }
        }
    }

}