package com.android.demoeditor.data

import android.graphics.Typeface
import java.util.*

data class FontItemData(val font : Typeface,val name:String,val id: String = UUID.randomUUID().toString())
