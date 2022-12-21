package com.android.demoeditor.viewModelFactory.viewPager.textStickerFragment

import android.app.Application
import com.android.demoeditor.viewModel.viewPager.textStickerFragement.FontViewPagerViewModel
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider


class FontViewPagerViewModelFactory (private val application: Application  ) :  ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if(modelClass.isAssignableFrom(FontViewPagerViewModel::class.java)){
            return FontViewPagerViewModel(application ) as T
        }

        throw IllegalArgumentException(" FontViewPagerViewModel Not Found !!! ")
    }
}