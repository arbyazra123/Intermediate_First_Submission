package com.kmm.intermediatefirstsubmission.data.network

import com.kmm.intermediatefirstsubmission.BuildConfig
import com.kmm.intermediatefirstsubmission.data.core.local_data.SessionPreference
import kotlinx.coroutines.runBlocking
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class ApiConfig {
    companion object {
        fun getService(sessionPreference: SessionPreference): ApiService {
            val loggingInterceptor =
                    HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY)
            val client = OkHttpClient.Builder()
                .addInterceptor { req ->
                    runBlocking {
                        val token = sessionPreference.getToken()
                        if (!token.isNullOrEmpty()) {
                            val newReq = req.request().newBuilder()
                                .addHeader("Authorization", "Bearer $token")
                                .build()
                            req.proceed(newReq)
                        } else {
                            req.proceed(req.request())
                        }
                    }


                }
                .addInterceptor(loggingInterceptor)
                .build()
            val retrofit = Retrofit.Builder()
                .baseUrl(BuildConfig.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .client(client)
                .build()
            return retrofit.create(ApiService::class.java)
        }
    }

}