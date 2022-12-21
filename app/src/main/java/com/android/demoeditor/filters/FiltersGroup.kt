package com.android.demoeditor.filters

import android.content.Context
import android.graphics.Bitmap
import android.util.Log
import androidx.renderscript.*

class FiltersGroup(private val context: Context ) {

    private val TAG = FiltersGroup::class.java.simpleName

    private var rs: RenderScript = RenderScript.create(context)




    private val brightnessFilters = BrightnessFilter(rs)
    private val contrastFilters = ContrastFilter(rs)
    private val saturationFilters = SaturationFilter(rs)
    private val sharpenFilters = SharpenFilter(rs)
    private val vignetteFilters = VignetteFilter(rs)


    /**
     * Only For Filter-Groups
     */

    private var builder: ScriptGroup.Builder2 = ScriptGroup.Builder2(rs)

    private lateinit var scriptGroup: ScriptGroup


    /**
     * Input-Allocation and Out-Allocation
     */

    private lateinit var inAllocation: Allocation


    /**
     * Input Bitmap
     */

    private lateinit var inputBitmap: Bitmap


    fun configureInputAndOutput(bitmap: Bitmap, numberOfOutputImages: Int) {

        if (numberOfOutputImages <= 0) {
            throw RuntimeException("Invalid number of output images: $numberOfOutputImages")
        }

        inputBitmap = bitmap.copy(bitmap.config, true)

        // Input allocation
        inAllocation = Allocation.createFromBitmap(
            rs,
            inputBitmap,
            Allocation.MipmapControl.MIPMAP_NONE,
            Allocation.USAGE_SCRIPT or Allocation.USAGE_SHARED
        )

        initFilterGroups()

    }

    private fun initFilterGroups() {
        sharpenFilters.setInputAllocation(inAllocation)
        val unbound: ScriptGroup.Input? = builder.addInput()
        val type = inAllocation.type


        /**
         * Note about one thing :-
         *
         * 1. First of All :- [sharpenClosure] always at first on [scriptGroup]
         * 2. Then Second define [brightClosure] , [contrastClosure] ... rest of closure
         */

        val sharpenClosure = builder.addKernel(
            sharpenFilters.kernelId,
            type,
            unbound
        )

        val brightClosure =
            builder.addKernel(brightnessFilters.kernelId, type, sharpenClosure.`return`)

        val contrastClosure =
            builder.addKernel(contrastFilters.kernelId, type, brightClosure.`return`)


        val saturationClosure =
            builder.addKernel(
                saturationFilters.kernelId,
                type,
                contrastClosure.`return`
            )


        val vignetteClosure =
            builder.addKernel(
                vignetteFilters.kernelId,
                type,
                saturationClosure.`return`
            )

        /**
         * Initialize the [scriptGroup]
         */

        scriptGroup = builder.create(
            "xyz",
            vignetteClosure.`return`
        )

        Log.i(TAG," Script-Group initialization is completed... ")

    }

    /**
     * [execute] method is run  Script-Group which is [scriptGroup] at single time
     * @param bright is brightness value
     * @param contrast is contrast value
     * @param saturation is saturation value
     * @param sharpen is sharpen value
     * @param vignette is vignette value
     * @return [inputBitmap]
     *
     */

    fun execute(
        bright: Float,
        contrast: Float,
        saturation: Float,
        sharpen: Float,
        vignette: Float
    ): Bitmap {
        brightnessFilters.setInputOnlyForScriptGroup(bright)
        contrastFilters.setInputOnlyForScriptGroup(contrast)
        saturationFilters.setInputOnlyForScriptGroup(saturation)
        sharpenFilters.setInputOnlyForScriptGroup(sharpen)
        vignetteFilters.setInputOnlyForScriptGroup(vignette, inAllocation)



        val outAllocation = scriptGroup.execute(inAllocation)[0] as Allocation
        outAllocation.copyTo(inputBitmap)


        return inputBitmap

    }


    fun destroy(){
        rs.destroy() // i added
        inAllocation.destroy()
        brightnessFilters.destroyFilter()
        contrastFilters.destroyFilter()
        saturationFilters.destroyFilter()
        sharpenFilters.destroyFilter()
        vignetteFilters.destroyFilter()
        scriptGroup.destroy()


    }


}