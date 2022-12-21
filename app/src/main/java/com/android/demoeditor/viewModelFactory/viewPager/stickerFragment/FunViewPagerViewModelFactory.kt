package com.android.demoeditor.viewModelFactory.viewPager.stickerFragment

import android.app.Application
import com.android.demoeditor.viewModel.viewPager.stickersFragment.FunViewPagerViewModel
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider

class FunViewPagerViewModelFactory(private val application: Application):
    ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if(modelClass.isAssignableFrom(FunViewPagerViewModel::class.java)){
            return FunViewPagerViewModel(application) as T
        }

        throw IllegalArgumentException(" FunViewPagerViewModel Not Found !!! ")

    }

}