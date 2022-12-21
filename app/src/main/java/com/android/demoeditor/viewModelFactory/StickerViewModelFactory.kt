package com.android.demoeditor.viewModelFactory

import android.app.Application
import com.android.demoeditor.screens.StickerFragmentArgs
import com.android.demoeditor.viewModel.StickerViewModel
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider

class StickerViewModelFactory(
    private val application: Application,
    private val navArgs: StickerFragmentArgs,
    private val redirectToScreen: () -> Unit,
) : ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(StickerViewModel::class.java)) {
            return StickerViewModel(application, navArgs, redirectToScreen) as T
        }
        throw IllegalArgumentException(" StickerViewModel Not Found !!! ")

    }
}