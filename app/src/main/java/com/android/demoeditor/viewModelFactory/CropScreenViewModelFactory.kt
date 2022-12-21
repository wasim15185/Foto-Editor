package com.android.demoeditor.viewModelFactory

import android.app.Application
import com.android.demoeditor.screens.CropFragmentArgs
import com.android.demoeditor.viewModel.CropScreenViewModel
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider

class CropScreenViewModelFactory(
    private val application: Application,
    private val navArgs: CropFragmentArgs,
    private val redirectToScreen: () -> Unit,
) : ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(CropScreenViewModel::class.java)) {
            return CropScreenViewModel(application, navArgs, redirectToScreen) as T
        }

        throw IllegalArgumentException(" CropScreenViewModel Not Found !!! ")

    }
}