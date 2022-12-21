package com.android.demoeditor.viewModelFactory

import android.app.Application
import com.android.demoeditor.customClass.CustomPhotoEditor
import com.android.demoeditor.screens.PaintingFragmentArgs
import com.android.demoeditor.viewModel.PaintingScreenViewModel
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider

class PaintingScreenViewModelFactory(
    private val application: Application,
    private val photoEditorBuilder: CustomPhotoEditor,
    private val navArgs: PaintingFragmentArgs,
    private val redirectToScreen: () -> Unit,


    ) : ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(PaintingScreenViewModel::class.java)) {
            return PaintingScreenViewModel(
                application,
                photoEditorBuilder,
                navArgs,
                redirectToScreen
            ) as T
        }

        throw IllegalArgumentException(" PaintingScreenViewModel Not Found !!! ")

    }

}