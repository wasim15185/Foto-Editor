package com.android.demoeditor.database

import android.content.Context
 import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.android.demoeditor.data.database.EditedPhoto

@Database(entities = [EditedPhoto::class], version = 1, exportSchema = false)
abstract class RecentPhotoDatabase : RoomDatabase() {

    abstract val photoDatabaseDao: RecentPhotoDao

    companion object {
        @Volatile
        private var INSTANCE: RecentPhotoDatabase? = null

        fun getInstance(context: Context): RecentPhotoDatabase {
            synchronized(this) {
                var instance = INSTANCE

                if (instance == null) {
                    instance = Room.databaseBuilder(
                        context.applicationContext, RecentPhotoDatabase::class.java,
                        "recent_photos"
                    ).fallbackToDestructiveMigration()
                        .build()
                    INSTANCE = instance
                }

                return instance

            }
        }

    }


}