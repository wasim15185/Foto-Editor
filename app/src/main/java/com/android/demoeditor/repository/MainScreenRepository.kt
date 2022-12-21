package com.android.demoeditor.repository

import android.content.Context
import com.android.demoeditor.R
import com.android.demoeditor.data.MainScreenNavScreenItemData
import com.android.demoeditor.enums.MainScreenItems
import com.android.demoeditor.enums.MainScreenItems.*
import androidx.core.content.ContextCompat

class MainScreenRepository(private val context: Context) {

    val dataForMainNavBar: MutableList<MainScreenNavScreenItemData>
        get() = mutableListOf<MainScreenNavScreenItemData>(
            getData(R.drawable.ic_crop, CROP, R.string.crop),
            getData(R.drawable.ic_rotate_left, ROTATE, R.string.rotate),
            getData(R.drawable.ic_adjust, ADJUST, R.string.adjust),
            getData(R.drawable.ic_brush, BRUSH, R.string.brush_text),
            getData(R.drawable.ic_filter, FILTER, R.string.filter),
            getData(R.drawable.ic_text, TEXT_STICKER, R.string.text),
            getData(R.drawable.ic_sticker, STICKER, R.string.sticker),

            )


    private fun getData(
        iconId: Int,
        iconType: MainScreenItems,
        nameId: Int
    ): MainScreenNavScreenItemData {
        val iconDrawable = ContextCompat.getDrawable(context, iconId)
        val iconText = context.getText(nameId).toString()
       return MainScreenNavScreenItemData(icon = iconDrawable!!, iconType = iconType, iconName = iconText)
    }


}