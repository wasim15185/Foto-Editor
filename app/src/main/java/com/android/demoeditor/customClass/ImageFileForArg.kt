package com.android.demoeditor.customClass

import android.graphics.Bitmap
import android.os.Parcelable
import kotlinx.android.parcel.Parcelize


@Parcelize
data class ImageFileForArg(
     val bitMapImage:Bitmap
):Parcelable
