package com.kmm.intermediatefirstsubmission.data.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.dicoding.myunlimitedquotes.database.RemoteKeysDao
import com.kmm.intermediatefirstsubmission.data.core.model.RemoteKeys
import com.kmm.intermediatefirstsubmission.data.story.model.ListStoryResponseItem

@Database(
    entities = [ListStoryResponseItem::class,RemoteKeys::class],
    version = 1,
    exportSchema = false
)
abstract class StoryDatabase : RoomDatabase() {

    abstract fun storyDao(): StoryDao
    abstract fun remoteKeysDao(): RemoteKeysDao

    companion object {
        @Volatile
        private var INSTANCE: StoryDatabase? = null

        fun getInstance(context: Context): StoryDatabase {
            return INSTANCE ?: synchronized(this) {
                INSTANCE ?: Room.databaseBuilder(
                    context,
                    StoryDatabase::class.java,
                    "curhatly_database"
                ).fallbackToDestructiveMigration().build().also { INSTANCE = it }
            }
        }


    }

}