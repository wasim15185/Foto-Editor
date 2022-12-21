package com.android.demoeditor.viewModelFactory.viewPager.homeFragment

import com.android.demoeditor.repository.HomeRecentEditViewPagerRepository
import com.android.demoeditor.viewModel.viewPager.homeFragment.HomeRecentEditViewPagerViewModel
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider

class HomeRecentEditViewPagerViewModelFactory (private val repository:HomeRecentEditViewPagerRepository) : ViewModelProvider.Factory {
    private val TAG = this::class.java.simpleName

    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(HomeRecentEditViewPagerViewModel::class.java)) {
            return HomeRecentEditViewPagerViewModel(repository) as T
        }
        throw IllegalArgumentException(" $TAG Not Found !!! ")
    }
}