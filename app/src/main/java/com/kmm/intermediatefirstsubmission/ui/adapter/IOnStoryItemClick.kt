package com.kmm.intermediatefirstsubmission.ui.adapter

import com.kmm.intermediatefirstsubmission.data.story.model.ListStoryResponseItem
import com.kmm.intermediatefirstsubmission.databinding.StoryItemBinding

interface IOnStoryItemClick {
    fun onClick(binding: StoryItemBinding, story: ListStoryResponseItem)
}