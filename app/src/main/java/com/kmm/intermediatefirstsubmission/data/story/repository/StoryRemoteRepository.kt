package com.kmm.intermediatefirstsubmission.data.story.repository

import com.kmm.intermediatefirstsubmission.data.network.ApiService
import okhttp3.MultipartBody
import okhttp3.RequestBody

class StoryRemoteRepository(private val apiService: ApiService) {
    fun getStories(location: String?) = apiService.getStories(location)
    fun postStory(description: RequestBody, file: MultipartBody.Part) =
            apiService.postStory(description, file)
}