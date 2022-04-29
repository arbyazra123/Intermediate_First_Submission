package com.kmm.intermediatefirstsubmission.data.core.model

import com.google.gson.annotations.SerializedName
import com.kmm.intermediatefirstsubmission.data.auth.model.common.LoginResult
import com.kmm.intermediatefirstsubmission.data.story.model.ListStoryResponseItem


data class BaseResponse(
    @field:SerializedName("error")
    val error: Boolean? = null,

    @field:SerializedName("message")
    val message: String? = null,

    @field:SerializedName("loginResult")
    val loginResult: LoginResult? = null,

    @field:SerializedName("listStory")
    val listStory: List<ListStoryResponseItem>
)
