package com.android.demoeditor.data

 import android.graphics.drawable.Drawable
 import java.util.*

data class AdjustNavItemData(val filterName:String, val filterPhoto: Drawable, val id: String = UUID.randomUUID().toString())
