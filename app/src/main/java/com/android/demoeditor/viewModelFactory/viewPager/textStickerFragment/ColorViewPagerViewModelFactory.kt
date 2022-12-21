package com.android.demoeditor.viewModelFactory.viewPager.textStickerFragment

import android.app.Application
import com.android.demoeditor.viewModel.viewPager.textStickerFragement.ColorViewPagerViewModel
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider

class ColorViewPagerViewModelFactory(private val application: Application) : ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(ColorViewPagerViewModel::class.java)){
           return ColorViewPagerViewModel(application) as T
        }

        throw IllegalArgumentException(" ColorViewPagerViewModel Not Found !!! ")
    }


}