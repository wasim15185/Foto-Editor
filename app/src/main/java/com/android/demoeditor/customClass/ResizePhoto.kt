package com.android.demoeditor.customClass

import android.content.Context
import com.android.demoeditor.filters.ResizeFilter
import android.graphics.Bitmap
import android.util.Log
import androidx.renderscript.*


class ResizePhoto(
    private val context: Context,
    private val _bitmap: Bitmap,
    private var isRenderscriptEnable: Boolean = false,
    private val resolution: RESOLUTION
) {

    private var rs: RenderScript = RenderScript.create(context)

    private lateinit var resizeFilter: ResizeFilter

    companion object {
        private val TAG = ResizePhoto::class.java.simpleName
    }

    private var resolutionWidth: Int = 0


    init {

        resolutionWidth = resolution.resolutionWidth

        resizeFilter=ResizeFilter(rs)
    }


    fun execute(): Bitmap {


        Log.d(TAG, "isRenderscriptEnable -$isRenderscriptEnable")


//        val rs = RenderScript.create(context)

        return if (isRenderscriptEnable) {

//            getRenderScriptBitmap(rs , _bitmap.copy(_bitmap.config,true))

            getRenderScriptBitmap()

        } else {

            getScaleBitmap()

        }

    }

    private fun getScaleBitmap(): Bitmap {


        var newWidth = 0
        var newHeight = 0

        Log.d("Pictures", "Width and height are ${_bitmap.width}--${_bitmap.width}")

        if (_bitmap.width >= _bitmap.height) {
            val ratio: Float = _bitmap.width.toFloat() / _bitmap.height.toFloat()

            newWidth = resolutionWidth
            // Calculate the new height for the scaled _bitmap
            newHeight = Math.round(resolutionWidth / ratio)
        } else {
            val ratio: Float = _bitmap.height.toFloat() / _bitmap.width.toFloat()

            // Calculate the new width for the scaled _bitmap
            newWidth = Math.round(resolutionWidth / ratio)
            newHeight = resolutionWidth
        }

        Log.d("Pictures", "Width and height are $newWidth--$newHeight")
        return Bitmap.createScaledBitmap(
            _bitmap,
            newWidth,
            newHeight,
            false
        )


    }

  /**  private fun getRenderScriptBitmap(rs: RenderScript,outBitmap:Bitmap): Bitmap {

        Log.d("Pictures", "Width and height are ${outBitmap.width}--${outBitmap.width}")

        val bitmapConfig: Bitmap.Config = outBitmap.config
        val bitmapWidth: Int = outBitmap.width
        val bitmapHeight: Int = outBitmap.height
        val bitmapAspectRatio = bitmapWidth.toFloat() / bitmapHeight
        val dstHeight = (resolutionWidth / bitmapAspectRatio)

        val resizeRatio: Float = bitmapWidth.toFloat() / resolutionWidth

        /* Calculate gaussian's radius */

        /* Calculate gaussian's radius */
        val sigma = resizeRatio / Math.PI.toFloat()
        // https://android.googlesource.com/platform/frameworks/rs/+/master/cpu_ref/rsCpuIntrinsicBlur.cpp
        // https://android.googlesource.com/platform/frameworks/rs/+/master/cpu_ref/rsCpuIntrinsicBlur.cpp
        var radius = 2.5f * sigma - 1.5f
        radius = min(25f, max(0.0001f, radius))

        /* Gaussian filter */

        /* Gaussian filter */
        val tmpIn = Allocation.createFromBitmap(rs, outBitmap)
        val tmpFiltered = Allocation.createTyped(rs, tmpIn.type)
        val blurInstrinsic = ScriptIntrinsicBlur.create(rs, tmpIn.element)

        blurInstrinsic.setRadius(radius)
        blurInstrinsic.setInput(tmpIn)
        blurInstrinsic.forEach(tmpFiltered)

        tmpIn.destroy()
        blurInstrinsic.destroy()



        /* Resize */
        val dst = Bitmap.createBitmap(resolutionWidth, dstHeight.toInt(), bitmapConfig)
        val t = Type.createXY(rs, tmpFiltered.element, resolutionWidth, dstHeight.toInt())
        val tmpOut = Allocation.createTyped(rs, t)
        val resizeIntrinsic = ScriptIntrinsicResize.create(rs)

        resizeIntrinsic.setInput(tmpFiltered)
        resizeIntrinsic.forEach_bicubic(tmpOut)
        tmpOut.copyTo(dst)

        tmpFiltered.destroy()
        tmpOut.destroy()
        resizeIntrinsic.destroy()

        Log.d("Pictures", "Width and height are ${dst.width}--${dst.width}")

        return dst

    } */




    private   fun getRenderScriptBitmap(): Bitmap {

//        val outBitmap=_bitmap.copy(_bitmap.config,true)
        val outBitmap=_bitmap

      val inAllocation= Allocation.createFromBitmap(rs,outBitmap)

      return  resizeFilter.executeFilter(inAllocation,outBitmap,resolutionWidth)

    }



    enum class RESOLUTION(val width: Int, val height: Int, val resolutionWidth: Int) {
        STANDARD_480P(720, 480, 720),
        HD_720P(1280, 720, 1280),
        FULL_HD_1080P(1920, 1080, 1920)
    }





}