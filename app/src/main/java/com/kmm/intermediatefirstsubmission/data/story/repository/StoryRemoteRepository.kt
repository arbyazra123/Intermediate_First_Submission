package com.kmm.intermediatefirstsubmission.data.story.repository

import com.kmm.intermediatefirstsubmission.data.network.ApiService
import okhttp3.MultipartBody
import okhttp3.RequestBody

class StoryRemoteRepository(private val apiService: ApiService) {
    fun getStories(
        location: String?,
    ) = apiService.getStories(location)

    suspend fun getStoriesWithPaging(
        location: String?,
        page: Int?, size: Int?
    ) = apiService.getStoriesWithPaging(location, page, size)

    fun postStory(
        description: RequestBody,
        file: MultipartBody.Part,
        lat: RequestBody?,
        lon: RequestBody?
    ) =
            apiService.postStory(description, lat, lon, file)
}