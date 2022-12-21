package com.android.demoeditor.data.parcelData

import com.android.demoeditor.enums.ActiveNavArgsData
import android.net.Uri
import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class CommonParcelData(
                                val uri:Uri?=null,
                                val availableData: ActiveNavArgsData,
                                ): Parcelable
