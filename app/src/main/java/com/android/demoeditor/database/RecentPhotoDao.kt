package com.android.demoeditor.database

 import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.android.demoeditor.data.database.EditedPhoto


const val recentPhotoTableName="recent_photos_details"

@Dao
interface RecentPhotoDao{
    @Insert
    fun insert(photoDetail: EditedPhoto)
    @Query ("SELECT * FROM $recentPhotoTableName")
    fun getAllData():LiveData<MutableList<EditedPhoto>>
    @Query("DELETE FROM $recentPhotoTableName WHERE id = :id  ")
      fun deletePhoto(id:Long)

}