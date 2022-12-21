package com.android.demoeditor.viewModelFactory

import android.app.Application
import com.android.demoeditor.screens.RotationFragmentArgs
import com.android.demoeditor.viewModel.RotationViewModel
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider

class RotationViewModelFactory(
    private val application: Application,
    private val navArg: RotationFragmentArgs,
    private val redirectToScreen: () -> Unit,
) : ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(RotationViewModel::class.java)) {
            return RotationViewModel(application, navArg, redirectToScreen) as T
        }

        throw IllegalArgumentException(" RotationViewModel Not Found !!! ")


    }
}