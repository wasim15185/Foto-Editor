package com.android.demoeditor.viewModelFactory

import android.app.Application
import com.android.demoeditor.viewModel.HomeViewModel
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider

class HomeViewModelFactory(private val application: Application): ViewModelProvider.Factory  {
     private val TAG = this::class.java.simpleName

    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if(modelClass.isAssignableFrom(HomeViewModel::class.java)){
            return HomeViewModel(application) as T
        }

        throw IllegalArgumentException(" HomeViewModel Not Found !!! ")
    }
}