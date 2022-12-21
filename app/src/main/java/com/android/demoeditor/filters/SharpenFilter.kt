package com.android.demoeditor.filters

import android.graphics.Bitmap
import androidx.renderscript.*

class SharpenFilter(
    private val rs: RenderScript
) : FilterConfig {

    private val sharpenConvolution = ScriptIntrinsicConvolve3x3.create(rs, Element.U8_4(rs))

    private  var isInputAllocated =false

    fun setInputAllocation(inputAllocation: Allocation) {
        sharpenConvolution.setInput(inputAllocation)
        isInputAllocated=true
    }

    override val kernelId: Script.KernelID
        get() = sharpenConvolution.kernelID

    override fun setInputOnlyForScriptGroup(value: Float, inputAllocationOfGroup: Allocation?) {
        val coefficientsMatrixArr = getCoefficientsMatrix(value)
        sharpenConvolution.setCoefficients(coefficientsMatrixArr)
    }

    override fun destroyFilter() {
        sharpenConvolution.destroy()
    }

    override fun executeFilter(
        inputAllocation: Allocation,
        outAllocation: Allocation,
        bitmap: Bitmap,
        filterValue: Float
    ): Bitmap {

        val coefficientsMatrixArr = getCoefficientsMatrix(filterValue)
        sharpenConvolution.apply {
           if (!this@SharpenFilter.isInputAllocated) setInput(inputAllocation)
            setCoefficients(coefficientsMatrixArr)
            forEach(outAllocation)
        }
        outAllocation.copyTo(bitmap)

        inputAllocation.destroy()
        outAllocation.destroy()
        return bitmap
    }


    private fun getCoefficientsMatrix(value: Float) = floatArrayOf(
        0f, -value, 0f,
        -value, 1 + 4 * value, -value,
        0f, -value, 0f
    )


}