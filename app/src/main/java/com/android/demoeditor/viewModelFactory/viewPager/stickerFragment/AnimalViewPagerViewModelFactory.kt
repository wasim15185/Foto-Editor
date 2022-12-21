package com.android.demoeditor.viewModelFactory.viewPager.stickerFragment

import android.app.Application
import android.content.res.TypedArray
import com.android.demoeditor.viewModel.viewPager.stickersFragment.AnimalViewPagerViewModel
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider

class AnimalViewPagerViewModelFactory(private val application: Application,private val stickerList: TypedArray):ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(AnimalViewPagerViewModel::class.java)){
            return AnimalViewPagerViewModel(application,stickerList) as T
        }

        throw IllegalArgumentException(" AnimalViewPagerViewModel Not Found !!! ")

    }
}