package com.android.demoeditor.filters

import android.graphics.Bitmap
import android.util.Log
import androidx.renderscript.*
import kotlin.math.max
import kotlin.math.min

class ResizeFilter(private val rs: RenderScript) {

    private val resizeIntrinsic = ScriptIntrinsicResize.create(rs)


    val kernelId: Script.KernelID
        get() = resizeIntrinsic.kernelID_bicubic

    fun destroyFilter() {
        resizeIntrinsic.destroy()
    }

    fun setInputOnlyForScriptGroup(value: Float, inputAllocationOfGroup: Allocation?) {

    }

    fun executeFilter(
        inputAllocation: Allocation,
        bitmap: Bitmap,
        resolutionWidth: Int
    ): Bitmap {

        Log.d("Pictures", "Width and height are ${bitmap.width}--${bitmap.width}")


        val bitmapConfig: Bitmap.Config = bitmap.config
        val bitmapWidth: Int = bitmap.width
        val bitmapHeight: Int = bitmap.height
        val bitmapAspectRatio = bitmapWidth.toFloat() / bitmapHeight
        val dstHeight = (resolutionWidth / bitmapAspectRatio)

        val resizeRatio: Float = bitmapWidth.toFloat() / resolutionWidth

        /* Calculate gaussian's radius */
        val sigma = resizeRatio / Math.PI.toFloat()

        var radius = 2.5f * sigma - 1.5f
        radius = min(25f, max(0.0001f, radius))

        /* Gaussian filter */

        val tmpFiltered = Allocation.createTyped(rs, inputAllocation.type)
        val blurInstrinsic = ScriptIntrinsicBlur.create(rs, inputAllocation.element)

        blurInstrinsic.apply {
            setRadius(radius)
            setInput(inputAllocation)
            forEach(tmpFiltered)
        }

        inputAllocation.destroy()
        blurInstrinsic.destroy()


        /* Resize */
        val dst = Bitmap.createBitmap(resolutionWidth, dstHeight.toInt(), bitmapConfig)
        val t = Type.createXY(rs, tmpFiltered.element, resolutionWidth, dstHeight.toInt())
        val tmpOut = Allocation.createTyped(rs, t)


        resizeIntrinsic.apply {
            setInput(tmpFiltered)
            forEach_bicubic(tmpOut)
        }
        tmpOut.copyTo(dst)

        tmpFiltered.destroy()
        tmpOut.destroy()
        resizeIntrinsic.destroy()

        Log.d("Pictures", "Width and height are ${dst.width}--${dst.width}")

        return dst


    }
}