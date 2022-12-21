package com.android.demoeditor.data

import com.android.demoeditor.enums.ShareIconType
import android.graphics.drawable.Drawable
import java.util.*

data class ShareItemData(val id: String = UUID.randomUUID().toString(),val drawable:Drawable,val name :String,val type:ShareIconType)
