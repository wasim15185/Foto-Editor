package com.android.demoeditor.viewModelFactory.viewPager.stickerFragment

import android.app.Application
import com.android.demoeditor.viewModel.viewPager.stickersFragment.EmojiViewPagerViewModel
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider

class EmojiViewPagerViewModelFactory(private val application: Application): ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(EmojiViewPagerViewModel::class.java)){
            return EmojiViewPagerViewModel(application) as T
        }

        throw IllegalArgumentException(" EmojiViewPagerViewModel Not Found !!! ")

    }
}