package com.android.demoeditor.viewModelFactory

import android.app.Activity
import android.app.Application
import com.android.demoeditor.database.RecentPhotoDatabase
import com.android.demoeditor.viewModel.SaveImageViewModel
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider

class SaveImageViewModelFactory(
    private val recentPhotoDatabase: RecentPhotoDatabase,
   private val application: Application,
    private val activity: Activity
): ViewModelProvider.Factory{
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if(modelClass.isAssignableFrom(SaveImageViewModel::class.java)){
            return SaveImageViewModel( recentPhotoDatabase , application ,activity ) as T
        }

        throw IllegalArgumentException("SaveImageViewModel is not found")
    }
}