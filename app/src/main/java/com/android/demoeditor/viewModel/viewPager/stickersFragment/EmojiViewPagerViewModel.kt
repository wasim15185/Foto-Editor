package com.android.demoeditor.viewModel.viewPager.stickersFragment

import android.annotation.SuppressLint
import android.app.Application
import com.android.demoeditor.R
import com.android.demoeditor.data.EmojiData
import com.android.demoeditor.repository.viewPager.EmojiViewPagerRepository
import android.text.Layout
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData

class EmojiViewPagerViewModel(application: Application):AndroidViewModel(application)  {

private val emojiList: Array<String> =application.resources.getStringArray(R.array.photo_editor_emoji)

    private val repository=EmojiViewPagerRepository(emojiList)

    @SuppressLint("StaticFieldLeak")
    private val context = getApplication<Application>().applicationContext


    private val _emojiRecyclerViewData =MutableLiveData(repository.getEmojiData())
    val emojiRecyclerViewData :LiveData<MutableList<EmojiData>>
    get() = _emojiRecyclerViewData


    fun getStickerRandomPosition(): Layout.Alignment {

        return when((0..2).random()){
            0 -> Layout.Alignment.ALIGN_CENTER
            1->Layout.Alignment.ALIGN_NORMAL
           2->Layout.Alignment.ALIGN_OPPOSITE
           else -> Layout.Alignment.ALIGN_CENTER
       }

    }



}