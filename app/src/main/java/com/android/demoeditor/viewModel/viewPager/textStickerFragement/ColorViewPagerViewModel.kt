package com.android.demoeditor.viewModel.viewPager.textStickerFragement

import android.annotation.SuppressLint
import android.app.Application
import androidx.lifecycle.AndroidViewModel

class ColorViewPagerViewModel(application: Application): AndroidViewModel(application) {

    @SuppressLint("StaticFieldLeak")
    private val context = getApplication<Application>().applicationContext

    /**
     * [_recyclerViewsData] is Mutable-Live-Data
     * [recyclerViewsData] is Live-Data
     */
//    private val _recyclerViewsData = MutableLiveData(getRecyclerViewData())
//
//    val recyclerViewsData: LiveData<MutableList<ColorPickerData>>
//        get() = _recyclerViewsData
//
//
//
//
//    private fun getRecyclerViewData(): MutableList<ColorPickerData> {
//        val colorList = context.resources.getStringArray(R.array.colors)
//
//        val list = mutableListOf<ColorPickerData>()
//
//        for ((i, item) in colorList.withIndex()) {
//            val colorAsInt = Color.parseColor(item)
//            val colorData = ColorPickerData(colorAsInt)
//            list.add(i, colorData)
//        }
//
//        return list
//    }


}