package com.kmm.intermediatefirstsubmission.data.network

import com.kmm.intermediatefirstsubmission.data.auth.model.request.LoginRequest
import com.kmm.intermediatefirstsubmission.data.auth.model.request.RegisterRequest
import com.kmm.intermediatefirstsubmission.data.core.model.BaseResponse
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.http.*

interface ApiService {

    @POST("login")
    fun login(
        @Body body: LoginRequest,
    ): Call<BaseResponse>

    @POST("register")
    fun register(
        @Body body: RegisterRequest,
    ): Call<BaseResponse>

    @GET("stories")
    fun getStories(@Query("location") location: String?): Call<BaseResponse>

    @Multipart
    @POST("stories")
    fun postStory(
        @Part("description") description: RequestBody,
        @Part file : MultipartBody.Part,
    ): Call<BaseResponse>
}