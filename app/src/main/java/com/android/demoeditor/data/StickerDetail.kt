package com.android.demoeditor.data



data class StickerDetail(
    val id:String,
    var text:String,
    var color: Int?=null,
    var typefaceName:String?=null ,
    var colorPositionInRecyclerView:Int?=null ,
    var fontPositionInRecyclerView:Int?=null
    )
