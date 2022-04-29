package com.kmm.intermediatefirstsubmission.data.database

import androidx.paging.PagingSource
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.kmm.intermediatefirstsubmission.data.story.model.ListStoryResponseItem

@Dao
interface StoryDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAllStory(value: List<ListStoryResponseItem>)

    @Query("SELECT * FROM stories")
    fun getAllStory(): PagingSource<Int, ListStoryResponseItem>

    @Query("DELETE FROM stories")
    suspend fun deleteAll()
}