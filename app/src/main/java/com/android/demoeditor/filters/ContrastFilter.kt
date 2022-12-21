package com.android.demoeditor.filters

import android.graphics.Bitmap
import androidx.renderscript.Allocation
import androidx.renderscript.RenderScript
import androidx.renderscript.Script

class ContrastFilter(
    private val rs : RenderScript
) : FilterConfig {

    private val contrastScript = com.android.demoeditor.ScriptC_contrast(rs)

    override val kernelId: Script.KernelID
        get() = contrastScript.kernelID_contrastness

    override fun setInputOnlyForScriptGroup(value: Float, inputAllocationOfGroup: Allocation?) {
        contrastScript._contrast=value
    }

    override fun destroyFilter() {
        contrastScript.destroy()
    }

    override fun executeFilter(
        inputAllocation: Allocation,
        outAllocation: Allocation,
        bitmap: Bitmap,
        filterValue: Float
    ): Bitmap {
        contrastScript._contrast=filterValue
        contrastScript.forEach_contrastness(inputAllocation,outAllocation)
        outAllocation.copyTo(bitmap)

        inputAllocation.destroy()
        outAllocation.destroy()
        return bitmap
    }









}