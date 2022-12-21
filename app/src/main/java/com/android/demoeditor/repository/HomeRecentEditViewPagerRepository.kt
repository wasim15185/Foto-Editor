package com.android.demoeditor.repository

import android.content.Context
import com.android.demoeditor.data.database.EditedPhoto
import com.android.demoeditor.database.RecentPhotoDatabase
import androidx.lifecycle.LiveData
import kotlinx.coroutines.*

class HomeRecentEditViewPagerRepository (val context: Context , val database: RecentPhotoDatabase ) {
    private val TAG = this::class.java.simpleName

    private val job = Job()

    private val uiScope=CoroutineScope(Dispatchers.Main+job)

    fun getAllRecentPhoto(): LiveData<MutableList<EditedPhoto>> {
        return database.photoDatabaseDao.getAllData()
    }



}