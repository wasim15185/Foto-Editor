package com.android.demoeditor.repository

import jp.co.cyberagent.android.gpuimage.filter.*

class FiltersRepository {



    /**
     * Color Matrix Filters
     */


    private val retroFilter: GPUImageFilter = GPUImageColorMatrixFilter(
        1.0f,
        floatArrayOf(
            1.0f, 0.0f, 0.0f, 0.0f,
            0.0f, 1.0f, 0.2f, 0.0f,
            0.1f, 0.1f, 1.0f, 0.0f,
            1.0f, 0.0f, 0.0f, 1.0f
        )
    )

    private val justFilter = GPUImageColorMatrixFilter(
        0.9f,
        floatArrayOf(
            0.4f, 0.6f, 0.5f, 0.0f,
            0.0f, 0.4f, 1.0f, 0.0f,
            0.05f, 0.1f, 0.4f, 0.4f,
            1.0f, 1.0f, 1.0f, 1.0f
        )
    )

    private val humeFilter = GPUImageColorMatrixFilter(
        1.0f,
        floatArrayOf(
            1.25f, 0.0f, 0.2f, 0.0f,
            0.0f, 1.0f, 0.2f, 0.0f,
            0.0f, 0.3f, 1.0f, 0.3f,
            0.0f, 0.0f, 0.0f, 1.0f
        )
    )

    private val desertFilter = GPUImageColorMatrixFilter(
        1.0f,
        floatArrayOf(
            0.6f, 0.4f, 0.2f, 0.05f,
            0.0f, 0.8f, 0.3f, 0.05f,
            0.3f, 0.3f, 0.5f, 0.08f,
            0.0f, 0.0f, 0.0f, 1.0f
        )
    )

    private val oldTimesFilter =
        GPUImageColorMatrixFilter(
            1.0f,
            floatArrayOf(
                1.0f, 0.05f, 0.0f, 0.0f,
                -0.2f, 1.1f, -0.2f, 0.11f,
                0.2f, 0.0f, 1.0f, 0.0f,
                0.0f, 0.0f, 0.0f, 1.0f
            )
        )


    private val limoFilter =
        GPUImageColorMatrixFilter(
            1.0f,
            floatArrayOf(
                1.0f, 0.0f, 0.08f, 0.0f,
                0.4f, 1.0f, 0.0f, 0.0f,
                0.0f, 0.0f, 1.0f, 0.1f,
                0.0f, 0.0f, 0.0f, 1.0f
            )
        )

    private val solarFilter = GPUImageColorMatrixFilter(
        1.0f,
        floatArrayOf(
            1.5f, 0f, 0f, 0f,
            0f, 1f, 0f, 0f,
            0f, 0f, 1f, 0f,
            0f, 0f, 0f, 1f
        )
    )

    private val neutronFilter = GPUImageColorMatrixFilter(
        1.0f,
        floatArrayOf(
            0f, 1f, 0f, 0f,
            0f, 1f, 0f, 0f,
            0f, 0.6f, 1f, 0f,
            0f, 0f, 0f, 1f
        )
    )

    private val milkFilter = GPUImageColorMatrixFilter(
        1.0f,
        floatArrayOf(
            0.0f, 1.0f, 0.0f, 0.0f,
            0.0f, 1.0f, 0.0f, 0.0f,
            0.0f, 0.64f, 0.5f, 0.0f,
            0.0f, 0.0f, 0.0f, 1.0f
        )
    )

    /**
     * Black Filter
     */

    private val clueFilter = GPUImageColorMatrixFilter(
        1.0f,
        floatArrayOf(
            0.0f, 0.0f, 1.0f, 0.0f,
            0.0f, 0.0f, 1.0f, 0.0f,
            0.0f, 0.0f, 1.0f, 0.0f,
            0.0f, 0.0f, 0.0f, 1.0f
        )
    )

    /**
     * Black Filter
     */
    private val muliFilter = GPUImageColorMatrixFilter(
        1.0f,
        floatArrayOf(
            1.0f, 0.0f, 0.0f, 0.0f,
            1.0f, 0.0f, 0.0f, 0.0f,
            1.0f, 0.0f, 0.0f, 0.0f,
            0.0f, 0.0f, 0.0f, 1.0f
        )
    )

    private val aeroFilter = GPUImageColorMatrixFilter(
        1.0f,
        floatArrayOf(
            0f, 0f, 1f, 0f,
            1f, 0f, 0f, 0f,
            0f, 1f, 0f, 0f,
            0f, 0f, 0f, 1f
        )
    )

    private val classicFilters = GPUImageColorMatrixFilter(
        1.0f,
        floatArrayOf(
            0.763f, 0.0f, 0.2062f, 0f,
            0.0f, 0.9416f, 0.0f, 0f,
            0.1623f, 0.2614f, 0.8052f, 0f,
            0f, 0f, 0f, 1f
        )
    )

    private val atomFilter = GPUImageColorMatrixFilter(
        1.0f,
        floatArrayOf(
            0.5162f, 0.3799f, 0.3247f, 0f,
            0.039f, 1.0f, 0f, 0f,
            -0.4773f, 0.461f, 1.0f, 0f,
            0f, 0f, 0f, 1f
        )
    )

    val marsFilter = GPUImageColorMatrixFilter(
        1.0f,
        floatArrayOf(
            0.0f, 0.0f, 0.5183f, 0.3183f,
            0.0f, 0.5497f, 0.5416f, 0f,
            0.5237f, 0.5269f, 0.0f, 0f,
            0f, 0f, 0f, 1f
        )
    )

    private val yeliFilter = GPUImageColorMatrixFilter(
        1.0f,
        floatArrayOf(
            1.0f, -0.3831f, 0.3883f, 0.0f,
            0.0f, 1.0f, 0.2f, 0f,
            -0.1961f, 0.0f, 1.0f, 0f,
            0f, 0f, 0f, 1f
        )
    )


    /**
     * [brightnessFilter] is Brightness Filter
     */

    private  val brightnessFilter = GPUImageBrightnessFilter(.15f)

    private  val contrastFilter = GPUImageContrastFilter(1.6f)

    private  val saturationFilter = GPUImageSaturationFilter(1.8f)

    private   val exposureFilter = GPUImageExposureFilter(.30f)

    private  val rgbFilterFilter = GPUImageRGBFilter(1.1f, 1.3f, 1.6f)

    private  val hueFilter = GPUImageHueFilter(46f)

    private  val sharpenFilter = GPUImageSharpenFilter(2.0f)


    private  val embossFilter = GPUImageEmbossFilter()

    private  val toonFilter = GPUImageToonFilter()

    private   val posterizeFilter = GPUImagePosterizeFilter()

    /**
     * Blur
     */
    private  val gaussianBlurFilter = GPUImageGaussianBlurFilter(5.0f)

    private   val monochromeFilter = GPUImageMonochromeFilter()



    private  val imageColorInvertFilter = GPUImageColorInvertFilter()

    /**
     * Black Filters
     */

    private   val luminanceFilter = GPUImageLuminanceFilter()

    private  val luminanceThresholdFilter = GPUImageLuminanceThresholdFilter()


    val filters = listOf<GPUImageFilter>(
        brightnessFilter,
        contrastFilter,
        saturationFilter,
        exposureFilter,
        retroFilter,
        rgbFilterFilter,
        hueFilter,
        posterizeFilter,
        justFilter,
        humeFilter,
        limoFilter,
        solarFilter,
        neutronFilter,
        milkFilter,
        aeroFilter,
        classicFilters,
        atomFilter,
        yeliFilter,
        marsFilter,
        desertFilter,
        oldTimesFilter,
        monochromeFilter,


        sharpenFilter,
        embossFilter,
        toonFilter,

        imageColorInvertFilter,

        /**
         * Black Filters
         */

        luminanceThresholdFilter,
        clueFilter,
        luminanceFilter,
        muliFilter

    )


}