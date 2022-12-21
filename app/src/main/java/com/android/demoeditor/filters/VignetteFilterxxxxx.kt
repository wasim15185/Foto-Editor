package com.android.demoeditor.filters

import androidx.renderscript.RenderScript
import android.graphics.Bitmap
import androidx.renderscript.Allocation


class VignetteFilterxxxxx(private val rs:RenderScript,) {

     val filter= com.android.demoeditor.ScriptC_vignette(rs)

    private val relaxed = false
    private var center_x = 0.5f
    private var center_y = 0.5f
   // private var scale = 0.5f
    private var scale = 0.0f
    private var shade = 0.5f

    /**
     * This for Black For Opacity
     */
    private var slope = 7.0f


    fun x (inputImage:Bitmap):Bitmap{
        val outBitmap=Bitmap.createBitmap(inputImage)
        val tempIn= Allocation.createFromBitmap(rs,inputImage)
        val tempOut= Allocation.createFromBitmap(rs,outBitmap)


        filter.invoke_init_vignette(tempIn.type.x.toLong(),tempIn.type.y.toLong(),center_x,center_y,scale,shade,slope)
        filter.forEach_root(tempIn,tempOut)
        tempOut.copyTo(outBitmap)
        tempIn.destroy()
        tempOut.destroy()
        return outBitmap
    }


    fun setScale(scale:Float){
         this.scale=scale
        //this.shade=scale
    }







}