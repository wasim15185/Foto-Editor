package com.android.demoeditor.repository

import com.android.demoeditor.data.SliderVal

class AdjustEditRepository {
    val sliderBrightness= SliderVal(-100.0f,100.0f,0.0f,1.0f)
    val sliderContrast= SliderVal(0.0f,2.0f,1.0f,0.01f)
    val sliderSaturation= SliderVal(0.0f,2.0f,1.0f,0.01f)
    val sliderSharpen= SliderVal(0.0f,1.0f,0.0f,.01f)

    val sliderVignette= SliderVal(0.0f,1f,0.0f,.01f)


}