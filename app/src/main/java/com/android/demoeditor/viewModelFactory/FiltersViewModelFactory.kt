package com.android.demoeditor.viewModelFactory

import android.app.Application
import com.android.demoeditor.screens.FiltersFragmentArgs
import com.android.demoeditor.viewModel.FiltersViewModel
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider

class FiltersViewModelFactory(
    private val application: Application,
    val navArgs: FiltersFragmentArgs,
    private val redirectToScreen:()->Unit ,

    ): ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(FiltersViewModel::class.java)) {
            return FiltersViewModel(application,navArgs,redirectToScreen) as T
        }

        throw IllegalArgumentException(" FilterViewModel Not Found !!! ")

    }

}