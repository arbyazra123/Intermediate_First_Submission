package com.kmm.intermediatefirstsubmission.data.story.paging

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.kmm.intermediatefirstsubmission.data.story.model.ListStoryResponseItem
import com.kmm.intermediatefirstsubmission.data.story.repository.StoryRemoteRepository

class StoryPagingSource(private val storyRemoteRepository: StoryRemoteRepository) :
    PagingSource<Int, ListStoryResponseItem>() {

    private companion object {
        const val INITIAL_PAGE_INDEX = 1
    }

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, ListStoryResponseItem> {
        return try {
            val page = params.key ?: INITIAL_PAGE_INDEX
            val responseData =
                    storyRemoteRepository.getStoriesWithPaging("1", page, params.loadSize)

            LoadResult.Page(
                data = responseData.listStory ?: arrayListOf(),
                prevKey = if (page == 1) null else page - 1,
                nextKey = if (responseData.listStory.isNullOrEmpty()) null else page + 1
            )
        } catch (exception: Exception) {
            return LoadResult.Error(exception)
        }
    }

    override fun getRefreshKey(state: PagingState<Int, ListStoryResponseItem>): Int? {
        return state.anchorPosition?.let { anchorPosition ->
            val anchorPage = state.closestPageToPosition(anchorPosition)
            anchorPage?.prevKey?.plus(1) ?: anchorPage?.nextKey?.minus(1)
        }
    }
}