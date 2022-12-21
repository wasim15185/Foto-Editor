package com.android.demoeditor.repository

import com.android.demoeditor.data.SliderVal

class AdjustRespository {



    val sliderBrightness=SliderVal(-1.0f,1.0f,0.0f)
    val sliderContrast=SliderVal(1.0f,2.0f,1.0f)
    val sliderSharpen=SliderVal(-4.0f,4.0f,0.0f)
    val sliderSaturation=SliderVal(0.0f,2.0f,1.0f)

    /**
     * Only For Slider-vignette
     * min :  -0.25f
     * max :   0.5f
     * default : 0.5f
     *
     * SliderVal( -0.25f, 0.5f, 0.5f)  <- Remember it
     */

    val sliderHue=SliderVal(0.5f,1.25f,0.5f)

}