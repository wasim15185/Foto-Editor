package com.android.demoeditor.viewModelFactory.viewPager.stickerFragment

import android.app.Application
import com.android.demoeditor.viewModel.viewPager.stickersFragment.WordsViewPagerViewModel
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider

class WordsViewPagerViewModelFactory(private val application: Application):
    ViewModelProvider.Factory  {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(WordsViewPagerViewModel::class.java)){
            return WordsViewPagerViewModel(application) as T
        }
        throw IllegalArgumentException(" WordsViewPagerViewModel Not Found !!! ")

    }
}