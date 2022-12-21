package com.android.demoeditor.filters

import android.graphics.Bitmap
import androidx.renderscript.*

class BlurFilter (private val rs:RenderScript):FilterConfig {
    //Intrinsic Gaussian blur filter
    private val blurFilter= ScriptIntrinsicBlur.create(rs, Element.U8_4(rs))

     val blurRadius=20f



    override val kernelId: Script.KernelID
        get() = blurFilter.kernelID

    override fun destroyFilter() {
        rs.destroy()
        blurFilter.destroy()
    }

    override fun setInputOnlyForScriptGroup(value: Float, inputAllocationOfGroup: Allocation?) {
        //TODO("Not yet implemented")
    }

    override fun executeFilter(
        inputAllocation: Allocation,
        outAllocation: Allocation,
        bitmap: Bitmap,
        filterValue: Float
    ): Bitmap {

        blurFilter.apply {
            setRadius(filterValue)
            setInput(inputAllocation)
            forEach(outAllocation)
        }

        outAllocation.copyTo(bitmap)

        inputAllocation.destroy()
        outAllocation.destroy()

        return bitmap

    }
}