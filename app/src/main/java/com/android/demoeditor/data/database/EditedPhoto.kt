package com.android.demoeditor.data.database

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "recent_photos_details")
data class EditedPhoto(
    @PrimaryKey(autoGenerate = true)
    var id:Long=0L ,

    @ColumnInfo(name="uri")
    var uriString: String

)
