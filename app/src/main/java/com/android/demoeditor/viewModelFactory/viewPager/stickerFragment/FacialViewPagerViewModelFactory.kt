package com.android.demoeditor.viewModelFactory.viewPager.stickerFragment

import android.app.Application
import android.content.res.TypedArray
import com.android.demoeditor.viewModel.viewPager.stickersFragment.FacialViewPagerViewModel
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider

class FacialViewPagerViewModelFactory(private val application: Application, private val stickerList: TypedArray): ViewModelProvider.Factory {
    private val TAG = this::class.java.simpleName

    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(FacialViewPagerViewModel::class.java)){
           return FacialViewPagerViewModel(application,stickerList) as T
        }

        throw IllegalArgumentException(" $TAG Not Found !!! ")
    }
}