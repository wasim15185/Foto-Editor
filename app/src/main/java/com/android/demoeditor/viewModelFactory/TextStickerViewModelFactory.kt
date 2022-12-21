package com.android.demoeditor.viewModelFactory

import android.app.Application
import com.android.demoeditor.databinding.FragmentTextStickerBinding
import com.android.demoeditor.screens.TextStickerFragmentArgs
import com.android.demoeditor.viewModel.TextStickerViewModel
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider

class TextStickerViewModelFactory(
    private val application: Application,
    private val navArgs: TextStickerFragmentArgs,
    private val textStickerBind: FragmentTextStickerBinding,
    private val redirectToScreen: () -> Unit,

    ) : ViewModelProvider.Factory {

    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(TextStickerViewModel::class.java)) {
            return TextStickerViewModel(
                application,
                navArgs,
                textStickerBind,
                redirectToScreen
            ) as T
        }

        throw IllegalArgumentException(" TextStickerViewModel Not Found !!! ")

    }
}