package com.kmm.intermediatefirstsubmission.data.story.view_model

import com.kmm.intermediatefirstsubmission.data.core.StateHandler
import com.kmm.intermediatefirstsubmission.data.core.model.BaseResponse
import com.kmm.intermediatefirstsubmission.data.story.repository.StoryRemoteRepository
import com.kmm.intermediatefirstsubmission.utility.ErrorParser
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.File

class StoryViewModelImpl(
    storyRepository: StoryRemoteRepository,
) : StoryViewModel(storyRepository) {

    override fun getStories(location: String?) {
        storyViewEvent.postValue(StateHandler.Initial())
        storyViewEvent.postValue(StateHandler.Loading())
        storyRepository.getStories(location).enqueue(object : Callback<BaseResponse> {
            override fun onResponse(call: Call<BaseResponse>, response: Response<BaseResponse>) {
                if (response.isSuccessful) {
                    val a = StateHandler.Success(response.body())
                    storyViewEvent.postValue(a)
                    storyViewLastUpdateEvent.postValue(a.data?.listStory?.first()?.createdAt)

                } else {
                    storyViewEvent.postValue(
                        StateHandler.Error(
                            ErrorParser.parse(response).message ?: "unknown error"
                        )
                    )
                }
            }

            override fun onFailure(call: Call<BaseResponse>, t: Throwable) {
                storyViewEvent.postValue(
                    StateHandler.Error(
                        t.message ?: "unknown error"
                    )
                )
            }

        })
    }

    override fun postStory(description: String, photo: File) {
        storyPostEvent.postValue(StateHandler.Loading())
        val descriptionRequestBody = description.toRequestBody("text/plain".toMediaType())
        val requestImageFile = photo.asRequestBody("image/jpeg".toMediaTypeOrNull())
        val imageMultipart: MultipartBody.Part =
                MultipartBody.Part.createFormData("photo", photo.name, requestImageFile)

        storyRepository.postStory(description = descriptionRequestBody, file = imageMultipart)
            .enqueue(object : Callback<BaseResponse> {
                override fun onResponse(
                    call: Call<BaseResponse>,
                    response: Response<BaseResponse>
                ) {
                    if (response.isSuccessful) {
                        storyPostEvent.postValue(StateHandler.Success(response.body()))

                    } else {
                        storyPostEvent.postValue(
                            StateHandler.Error(
                                ErrorParser.parse(response).message ?: "unknown error"
                            )
                        )
                    }
                }

                override fun onFailure(call: Call<BaseResponse>, t: Throwable) {
                    storyPostEvent.postValue(
                        StateHandler.Error(
                            t.message ?: "unknown error"
                        )
                    )
                }

            })
    }

    override fun resetPostStory() {
        storyPostEvent.postValue(StateHandler.Initial())
    }

    override fun checkIfStoryHasUpdate(): Boolean {
        val last = storyViewEvent.value?.data?.listStory?.first()
        last?.createdAt?.let {
            if (it != storyViewLastUpdateState.value?.toString()) {
                return true
            }
        }
        return false
    }
}