package com.android.demoeditor.data

import android.graphics.drawable.Drawable
import java.util.*

data class StickerData(val stickerBitmapImg:Drawable, val id: String = UUID.randomUUID().toString())
