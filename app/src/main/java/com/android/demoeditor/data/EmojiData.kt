package com.android.demoeditor.data

import java.util.*

data class EmojiData(val emoji:String,  val id: String = UUID.randomUUID().toString())
//data class EmojiData(val emoji:Int,  val id: String = UUID.randomUUID().toString())
