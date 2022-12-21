package com.android.demoeditor.filters

import android.graphics.Bitmap
import androidx.renderscript.Allocation
import androidx.renderscript.Script.*

interface FilterConfig  {
    val kernelId : KernelID
    fun destroyFilter()
    fun setInputOnlyForScriptGroup(value:Float,inputAllocationOfGroup: Allocation? =null)
    fun executeFilter(inputAllocation: Allocation, outAllocation: Allocation, bitmap: Bitmap, filterValue:Float) : Bitmap

}