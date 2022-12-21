package com.android.demoeditor.data

import com.android.demoeditor.recyclerViews.CollageIconType
import android.graphics.drawable.Drawable
import java.util.*

data class CollageEditItemData(val id:String = UUID.randomUUID().toString(), val iconType: CollageIconType, val icon: Drawable,val iconName:String)
