package com.android.demoeditor.viewModelFactory.viewPager.stickerFragment

import android.app.Application
import com.android.demoeditor.viewModel.viewPager.stickersFragment.FoodViewPagerViewModel
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider

class FoodViewPagerViewModelFactory(private val application: Application) :
    ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(FoodViewPagerViewModel::class.java)) {

            return FoodViewPagerViewModel(application) as T
        }

        throw IllegalArgumentException(" FoodViewPagerViewModel Not Found !!! ")

    }
}