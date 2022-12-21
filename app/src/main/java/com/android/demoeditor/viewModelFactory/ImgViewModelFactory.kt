package com.android.demoeditor.viewModelFactory

import android.app.Activity
import android.app.Application
import com.android.demoeditor.recyclerViews.ImageShowerAdapter
import com.android.demoeditor.viewModel.ImgViewModel
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider

class ImgViewModelFactory(private val application: Application, val adapter:  ImageShowerAdapter,private val activity:Activity) : ViewModelProvider.Factory {

    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(ImgViewModel::class.java)) {
            return ImgViewModel(application,adapter,activity ) as T
        }

        throw IllegalArgumentException(" ImgViewModel Not Found !!! ")

    }
}