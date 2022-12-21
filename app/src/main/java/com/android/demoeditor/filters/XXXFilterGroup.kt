package com.android.demoeditor.filters

import android.content.Context
import com.android.demoeditor.repository.AdjustEditRepository
import android.graphics.Bitmap
import android.util.Log
import androidx.renderscript.*

class XXXFilterGroup (private val context: Context, private val repository: AdjustEditRepository) {

    private val TAG = FiltersGroup::class.java.simpleName

    private var rs: RenderScript = RenderScript.create(context)

    private var brightnessScript: com.android.demoeditor.ScriptC_brightness =
        com.android.demoeditor.ScriptC_brightness(rs)

    private var contrastScript: com.android.demoeditor.ScriptC_contrast =
        com.android.demoeditor.ScriptC_contrast(rs)

    // private var contrastScript: ScriptC_contrast = ScriptC_contrast(rs)

    private var saturationFilter: com.android.demoeditor.ScriptC_saturation =
        com.android.demoeditor.ScriptC_saturation(rs)

    private var sharpenFilter: ScriptIntrinsicConvolve3x3 =
        ScriptIntrinsicConvolve3x3.create(rs, Element.U8_4(rs))

    private var vignetteFilterOriginal: com.android.demoeditor.ScriptC_vignette =
        com.android.demoeditor.ScriptC_vignette(rs)


    /**
     * Only For Filter-Groups
     */

    private var builder: ScriptGroup.Builder2 = ScriptGroup.Builder2(rs)

    private lateinit var scriptGroup: ScriptGroup

    private lateinit var brightClosure: ScriptGroup.Closure
    private lateinit var contrastClosure: ScriptGroup.Closure
    private lateinit var saturationClosure: ScriptGroup.Closure
    private lateinit var sharpenClosure: ScriptGroup.Closure
    private lateinit var vignetteClosure: ScriptGroup.Closure


    /**
     * Input-Allocation and Out-Allocation
     */

    private lateinit var inAllocation: Allocation

    private lateinit var outAllocation: Allocation


    /**
     * Input Bitmap
     */

    private lateinit var newInputBitmap: Bitmap

    private var brightness = repository.sliderBrightness.defaultVal
    private var contrast = repository.sliderContrast.defaultVal
    private var saturation = repository.sliderSaturation.defaultVal
    private var sharpen = repository.sliderSharpen.defaultVal
    private var vignette = repository.sliderVignette.defaultVal


    private val sharpenMatrix = floatArrayOf(
        0f, -sharpen, 0f,
        -sharpen, 1 + 4 * sharpen, -sharpen,
        0f, -sharpen, 0f
    )


    /**
     * Vignette
     */

    private val center_x = 0.5f
    private val center_y = 0.5f
    // private var scale = 0.5f
    /** 'scale' should 0.0f default*/
    private val scale = vignette
    private val shade = 0.5f

    /**
     * This for Black For Opacity
     */
    private val slope = 7.0f


    fun configureInputAndOutput(inputImage: Bitmap, numberOfOutputImages: Int) {

        if (numberOfOutputImages <= 0) {
            throw RuntimeException("Invalid number of output images: $numberOfOutputImages")
        }

        newInputBitmap = inputImage.copy(inputImage.config, true)

        // Input allocation
        inAllocation = Allocation.createFromBitmap(
            rs,
            newInputBitmap,
            Allocation.MipmapControl.MIPMAP_NONE,
            Allocation.USAGE_SCRIPT or Allocation.USAGE_SHARED
        )

        initFilterGroups()

    }


    private fun initFilterGroups() {



        sharpenFilter.setInput(inAllocation)

        val unbound: ScriptGroup.Input? = builder.addInput()

        val type = inAllocation.type

        sharpenClosure =
            builder.addKernel(
                sharpenFilter.kernelID,
                type,
                unbound
            )

        brightClosure =
            builder.addKernel(brightnessScript.kernelID_brightness, type, sharpenClosure.`return`)

        contrastClosure =
            builder.addKernel(
                contrastScript.kernelID_contrastness,
                type,
                brightClosure.`return`
            )
        /**   sharpenClosure =
        builder.addKernel(
        sharpenFilter.kernelID,
        type,
        contrastClosure.`return`
        )

        saturationClosure =
        builder.addKernel(
        saturationFilter.kernelID_saturation,
        type,
        sharpenClosure.`return`
        ) */

        // Nicher ta delete ta kore debe pore
        saturationClosure =
            builder.addKernel(
                saturationFilter.kernelID_saturation,
                type,
                contrastClosure.`return`
            )


        vignetteClosure =
            builder.addKernel(
                vignetteFilterOriginal.kernelID_root,
                type,
                saturationClosure.`return`
            )
        // builder.addKernel(vignetteFilterOriginal.filter.kernelID_root, inAllocation.type)



        // sharpenFilter.setCoefficients(sharpenMatrix)



        /** brightnessScript.invoke_setBright(brightness)
        contrastScript._contrast = contrast
        saturationFilter._saturationValue = saturation



        vignetteFilterOriginal.invoke_init_vignette(
        inAllocation.type.x.toLong(),
        inAllocation.type.y.toLong(),
        center_x,
        center_y,
        scale,
        shade,
        slope
        ) */


        scriptGroup = builder.create(
            "xyz",
            vignetteClosure.`return`
        )



        outAllocation = scriptGroup.execute(inAllocation)[0] as Allocation

        Log.i(TAG, "Everything is completed")

    }


    /**   fun setBrightness(newBrightness: Float): Bitmap {

    brightness = newBrightness

    brightnessScript.forEach_brightness(inAllocation, outAllocation)
    outAllocation.copyTo(newInputBitmap)


    //   (scriptGroup.execute(inAllocation)[0] as Allocation).copyTo(newInputBitmap)

    return newInputBitmap
    }

    fun setContrast(newContrast: Float): Bitmap {
    contrast = newContrast

    contrastScript.forEach_contrastness(inAllocation, outAllocation)
    outAllocation.copyTo(newInputBitmap)


    //  (scriptGroup.execute(inAllocation)[0] as Allocation).copyTo(newInputBitmap)


    return newInputBitmap
    }

    fun setSaturation(newSaturation: Float): Bitmap {
    saturation = newSaturation

    saturationFilter.forEach_saturation(inAllocation, outAllocation)
    outAllocation.copyTo(newInputBitmap)

    //   (scriptGroup.execute(inAllocation)[0] as Allocation).copyTo(newInputBitmap)

    return newInputBitmap
    }

     */


    fun executeAll(
        bright: Float,
        contrast: Float,
        saturation: Float,
        sharpen: Float,
        vignette: Float
    ): Bitmap {

        val newMatrix=floatArrayOf(
            0f, -sharpen, 0f,
            -sharpen, 1 + 4 * sharpen, -sharpen,
            0f, -sharpen, 0f
        )


        brightnessScript.invoke_setBright(bright)

        contrastScript._contrast = contrast

        saturationFilter._saturationValue = saturation


        /** this.sharpen = sharpen */
        sharpenFilter.setCoefficients(newMatrix)

        vignetteFilterOriginal.invoke_init_vignette(
            inAllocation.type.x.toLong(),
            inAllocation.type.y.toLong(),
            center_x,
            center_y,
            vignette,
            shade,
            slope
        )



        val xoutAllocation = scriptGroup.execute(inAllocation)[0] as Allocation
        xoutAllocation.copyTo(newInputBitmap)




        return newInputBitmap


    }


}