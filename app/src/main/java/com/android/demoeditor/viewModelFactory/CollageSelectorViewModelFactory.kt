package com.android.demoeditor.viewModelFactory

import android.app.Activity
import android.app.Application
import com.android.demoeditor.viewModel.CollageSelectorViewModel
import android.net.Uri
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider

class CollageSelectorViewModelFactory(
    private val application: Application,
    private val listOfUris: List<Uri>,
    private val activity: Activity,
 ) : ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(CollageSelectorViewModel::class.java)) {
            return CollageSelectorViewModel(
                application,
                listOfUris,
                activity,

            ) as T
        }

        throw  IllegalArgumentException(" CollageSelectorViewModel  Not Found !!! ")
    }
}