package com.kmm.intermediatefirstsubmission.data.story.view_model

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.kmm.intermediatefirstsubmission.data.core.StateHandler
import com.kmm.intermediatefirstsubmission.data.core.model.BaseResponse
import com.kmm.intermediatefirstsubmission.data.story.repository.StoryRemoteRepository
import java.io.File

abstract class StoryViewModel(
    val storyRepository: StoryRemoteRepository,
) : ViewModel() {
    protected val storyViewEvent = MutableLiveData<StateHandler<BaseResponse>>()
    val storyViewState: LiveData<StateHandler<BaseResponse>> = storyViewEvent
    protected val storyPostEvent = MutableLiveData<StateHandler<BaseResponse>>()
    val storyPostState: LiveData<StateHandler<BaseResponse>> = storyPostEvent

    protected val storyViewLastUpdateEvent = MutableLiveData<String>()
    val storyViewLastUpdateState: LiveData<String> = storyViewLastUpdateEvent

    abstract fun getStories(location: String?)
    abstract fun postStory(description: String, photo: File)
    abstract fun resetPostStory()
    abstract fun checkIfStoryHasUpdate() : Boolean

}