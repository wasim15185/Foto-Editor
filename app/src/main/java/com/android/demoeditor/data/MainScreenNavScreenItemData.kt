package com.android.demoeditor.data

import com.android.demoeditor.enums.MainScreenItems
import android.graphics.drawable.Drawable
import java.util.*

data class MainScreenNavScreenItemData(
    val id: String = UUID.randomUUID().toString(),
    val iconType:MainScreenItems,
    val icon: Drawable,
    val iconName:String
)
