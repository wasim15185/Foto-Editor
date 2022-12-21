package com.android.demoeditor.filters

import android.graphics.Bitmap
import androidx.renderscript.Allocation
import androidx.renderscript.RenderScript
import androidx.renderscript.Script

class VignetteFilter(private val rs: RenderScript) : FilterConfig {

    private val TAG =VignetteFilter::class.java.simpleName

    private val vignetteFilter = com.android.demoeditor.ScriptC_vignette(rs)

    override val kernelId: Script.KernelID
        get() = vignetteFilter.kernelID_root

    override fun setInputOnlyForScriptGroup(value: Float,  inputAllocationOfGroup: Allocation?) =
        if (inputAllocationOfGroup != null) {

            vignetteFilter.invoke_init_vignette(
                inputAllocationOfGroup.type.x.toLong(),
                inputAllocationOfGroup.type.y.toLong(),
                center_x,
                center_y,
                value,
                shade,
                slope
            )
        } else {
            throw IllegalArgumentException("Input-Allocation of ScriptGroup is NOT pass as an Argument in $TAG")
        }

    override fun destroyFilter() {
        vignetteFilter.destroy()
    }


    private var center_x = 0.5f
    private var center_y = 0.5f
    private var shade = 0.5f

    /**
     * This for Black For Opacity
     */
    private var slope = 7.0f


    override fun executeFilter(
        inputAllocation: Allocation,
        outAllocation: Allocation,
        bitmap: Bitmap,
        filterValue: Float
    ): Bitmap {


        vignetteFilter.apply {
            invoke_init_vignette(
                inputAllocation.type.x.toLong(),
                inputAllocation.type.y.toLong(),
                center_x,
                center_y,
                filterValue,
                shade,
                slope
            )
            forEach_root(inputAllocation, outAllocation)
        }

        outAllocation.copyTo(bitmap)

        inputAllocation.destroy()
        outAllocation.destroy()
        return bitmap
    }
}