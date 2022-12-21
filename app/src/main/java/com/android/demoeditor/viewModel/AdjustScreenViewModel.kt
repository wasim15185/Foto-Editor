package com.android.demoeditor.viewModel

import android.annotation.SuppressLint
import android.app.Activity
import android.app.Application
import android.content.Context
import com.android.demoeditor.customClass.BitmapHelper
import com.android.demoeditor.customClass.FilterName
import com.android.demoeditor.customClass.ResizePhoto
import com.android.demoeditor.customClass.ResizePhoto.*
import com.android.demoeditor.customClass.UriBitmapUtils
import com.android.demoeditor.data.parcelData.CommonParcelData
import com.android.demoeditor.enums.ActiveNavArgsData
import com.android.demoeditor.filters.FiltersGroup
import com.android.demoeditor.repository.AdjustEditRepository
import com.android.demoeditor.screens.AdjustFragmentArgs
import android.graphics.Bitmap
import android.os.Build
import android.os.VibrationEffect
import android.os.Vibrator
import android.widget.ImageView
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import kotlinx.coroutines.*

class AdjustScreenViewModel(
    application: Application,
    private val navArgs: AdjustFragmentArgs,
    activity: Activity,
    private val redirectToScreen:()->Unit ,


) : AndroidViewModel(application) {

    private val TAG = AdjustScreenViewModel::class.java.simpleName

    private var repository: AdjustEditRepository = AdjustEditRepository()

    private val scope = CoroutineScope(Dispatchers.IO + Job())

    @SuppressLint("StaticFieldLeak")
    private val context = getApplication<Application>().applicationContext!!

    companion object {
        // We need two output images to avoid computing into an image currently on display.
        /**
         * Here No need of [NUMBER_OF_OUTPUT_IMAGES] because we are using script-group
         * that's way  currently we are not using [NUMBER_OF_OUTPUT_IMAGES] .
         */
        private const val NUMBER_OF_OUTPUT_IMAGES = 2
        private const val VIBRATION_TIME = 50L
    }

    // Input and outputs
    private lateinit var mInputImage: Bitmap
    private lateinit var mOutputImage: Bitmap

    private var mCurrentOutputImageIndex = 0

    private var mLatestThread: Thread? = null

    /**
     * [mLock] object for Thread lock
     */
    private val mLock = Any()


    /**
     * [isEnableHapticFeedback] is for enabling haptic feedback
     */

    var isEnableHapticFeedback = false


    /**
     * [_imgHeight] && [_imgWidth] are Height and Width of Bitmap-Image
     */
    private val _imgHeight = MutableLiveData<Int>()
    val imgHeight: LiveData<Int>
        get() = _imgHeight


    private val _imgWidth = MutableLiveData<Int>()
    val imgWidth: LiveData<Int>
        get() = _imgWidth





    /**
     * is Img Loading
     */
    private val _isImgLoading = MutableLiveData<Boolean>(false)
    val isImgLoading: LiveData<Boolean>
        get() = _isImgLoading



    /**
     * [_imgBitmap] is Data For Image Source
     */

    private val _imgBitmap = MutableLiveData<Bitmap>()
    val imgBitmap: LiveData<Bitmap>
        get() = _imgBitmap


    /**
     *[_recentTab] is Live-Data for Recent Tab And "Default-Value" of is [FilterName.BRIGHTNESS]
     */

    private val _recentTab = MutableLiveData(FilterName.BRIGHTNESS)


    /**
     * [_activity] for getting activity
     */

    private val _activity = MutableLiveData<Activity>()

    /**
     *   [filterGroup] is scriptGroup
     */

    private lateinit var filterGroup: FiltersGroup


    /**
     * [_brightness] ,
     * [_contrast] ,
     * [_sharpen] ,
     * [_saturation] ,
     * [_vignette] current slider value
     */

    private val _brightness = MutableLiveData(repository.sliderBrightness.defaultVal)
    private val _contrast = MutableLiveData(repository.sliderContrast.defaultVal)
    private val _sharpen = MutableLiveData(repository.sliderSharpen.defaultVal)
    private val _saturation = MutableLiveData(repository.sliderSaturation.defaultVal)
    private val _vignette = MutableLiveData(repository.sliderVignette.defaultVal)


    /**
     * [init] Block is only for "Initialization"
     */

    init {

        _isImgLoading.value=true

        setUriOrBitmap(navArgs) {

            mInputImage = it
            mOutputImage = it

            scope.launch {
                filterGroup = initFilterGroup()
                withContext(Dispatchers.Main) {
                    _isImgLoading.value = false
                }
            }

        }
        /** This set bitmap img inside [_imgBitmap] and as well as [_imgHeight] and [_imgWidth] */


//        _imgBitmap.value = imgSrc


        _activity.value = activity
//        mInputImage = _imgBitmap.value!!
//        mOutputImage = _imgBitmap.value!!
//
//        scope.launch {
//            filterGroup = initFilterGroup()
//        }


    }


    private fun setUriOrBitmap(navArgs: AdjustFragmentArgs, lateInitialization: (Bitmap) -> Unit) {
        when (navArgs.imgData.availableData) {
            ActiveNavArgsData.BITMAP -> {
                if (BitmapHelper.bitmap != null) {



                    if (BitmapHelper.isResized) {


                        // _imgBitmap.value = BitmapHelper.bitmap!!
                        //   _imgBitmap.value!!.config = Bitmap.Config.ARGB_8888 // this is important
                        _imgBitmap.value = BitmapHelper.bitmap!!.copy(Bitmap.Config.ARGB_8888, true)

                        lateInitialization(_imgBitmap.value!!)
                        set_Img_Height_Width(_imgBitmap.value!!)

                    } else {


                        val resizePhoto =
                            ResizePhoto(context, BitmapHelper.bitmap!!, true, RESOLUTION.HD_720P)



                        scope.launch {

                            val resizeBitmap = resizePhoto.execute()

                            withContext(Dispatchers.Main) {

                                 _imgBitmap.value = resizeBitmap
                                _imgBitmap.value!!.config =
                                    Bitmap.Config.ARGB_8888 // this is important

                                lateInitialization(_imgBitmap.value!!)
                                set_Img_Height_Width(_imgBitmap.value!!)
                            }
                        }


                    }


                } else {
                    // Navigate to some where

                    redirectToScreen()
                }

            }

            ActiveNavArgsData.URI -> {

                convertUriToBitmap(navArgs.imgData, lateInitialization)
            }

        }

    }


    private fun convertUriToBitmap(
        imgData: CommonParcelData,
        lateInitialization: (Bitmap) -> Unit
    ) {
        if (imgData.uri != null) {

            UriBitmapUtils(imgData.uri, context, scope) {
                val resizePhoto = ResizePhoto(context, it, true, RESOLUTION.HD_720P)

                scope.launch {
                    val resizeBitmap = resizePhoto.execute()
                    withContext(Dispatchers.Main) {

                        _imgBitmap.value = resizeBitmap
                        lateInitialization(_imgBitmap.value!!)
                    }
                }

            }

        } else {
            throw Exception("Uri not available")
        }
    }


    private fun set_Img_Height_Width(bitmap: Bitmap) {
        _imgHeight.value = bitmap.height
        _imgWidth.value = bitmap.width
    }


    /**
     * this fun change value of [_isImgLoading]
     */
    fun setLoadingState(value: Boolean) {
        _isImgLoading.value = value
    }


    /**
     * Brightness Filter
     */


    fun updateImage(value: Float, imgView: ImageView) {


        // Start a new thread to invoke RenderScript/Vulkan kernels without blocking the UI thread.
        mLatestThread = Thread(kotlinx.coroutines.Runnable {
            synchronized(mLock) {
                // If there is a new worker thread scheduled while this thread is waiting on the
                // lock, cancel this thread. This ensures that when worker threads are piled up
                // (typically in slow device with heavy kernel), only the latest (and already
                // started) thread invokes RenderScript/Vulkan operation.
                if (mLatestThread != Thread.currentThread()) {
                    return@Runnable
                }

                // Apply the filter and measure the latency.


                val bitmapOut: Bitmap = currentFilter(value)

                // this for capturing outputBitmap
                mOutputImage = bitmapOut

                // Update image and text on UI thread.
                _activity.value?.runOnUiThread {


                    imgView.setImageBitmap(bitmapOut)
                    imgView.invalidate()
                    changeSliderValue(value)

                }
                mCurrentOutputImageIndex =
                    (mCurrentOutputImageIndex + 1) % AdjustScreenViewModel.NUMBER_OF_OUTPUT_IMAGES
            }
        })
        mLatestThread?.start()

    }

    /**
     * [autoHapticFeedBack] is for HapticFeedback
     * @param value is for slider current value
     */

    private fun autoHapticFeedBack(value: Float) {
        val vibrator = context.getSystemService(Context.VIBRATOR_SERVICE) as Vibrator


        if (isEnableHapticFeedback) {

            if (value == getSliderDefaultValue()) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) vibrator.vibrate(
                    VibrationEffect.createOneShot(
                        VIBRATION_TIME,
                        VibrationEffect.EFFECT_TICK
                    )
                )
                else vibrator.vibrate(VIBRATION_TIME)
            }
        }
    }

    private fun currentFilter(values: Float): Bitmap {

        return when (_recentTab.value!!) {

            FilterName.BRIGHTNESS -> executeFilterGroup()
            FilterName.CONTRAST -> executeFilterGroup()
            FilterName.SATURATION -> executeFilterGroup()
            FilterName.SHARPEN -> executeFilterGroup()
            FilterName.VIGNETTE -> executeFilterGroup()

        }
    }

    /**
     * [executeFilterGroup] is execute Filter-Group kernel
     * @return [Bitmap]
     */

    private fun executeFilterGroup(): Bitmap = filterGroup.execute(
        _brightness.value!!,
        _contrast.value!!,
        _saturation.value!!,
        _sharpen.value!!,
        _vignette.value!!
    )


    fun changeRecentTab(filterName: FilterName, resetSliderValAndGetBitmap: () -> Unit) {
        _recentTab.value = filterName
        resetSliderValAndGetBitmap()
    }

    /**
     * [initFilterGroup] is initialize filter-group and this call inside [scope]
     * means it is running background [Dispatchers.IO]
     */

    private fun initFilterGroup(): FiltersGroup {
        val filter = FiltersGroup(context)
        filter.configureInputAndOutput(mInputImage, NUMBER_OF_OUTPUT_IMAGES)
        return filter
    }


    private fun changeSliderValue(value: Float) {

        when (_recentTab.value!!) {
            FilterName.BRIGHTNESS -> _brightness.value = value
            FilterName.CONTRAST -> _contrast.value = value
            FilterName.SATURATION -> _saturation.value = value
            FilterName.SHARPEN -> _sharpen.value = value
            FilterName.VIGNETTE -> _vignette.value = value
        }

        autoHapticFeedBack(value)

    }


    /**
     * [getMinVal] is for Slider Minimum Setting Value
     */

    fun getMinVal(): Float {

        return when (_recentTab.value!!) {
            FilterName.BRIGHTNESS -> repository.sliderBrightness.minVal
            FilterName.CONTRAST -> repository.sliderContrast.minVal
            FilterName.SATURATION -> repository.sliderSaturation.minVal
            FilterName.SHARPEN -> repository.sliderSharpen.minVal
            FilterName.VIGNETTE -> repository.sliderVignette.minVal
        }
    }

    /**
     * [getMaxVal] is for Slider Maximum Setting Value
     */

    fun getMaxVal(): Float {
        return when (_recentTab.value!!) {
            FilterName.BRIGHTNESS -> repository.sliderBrightness.maxVal
            FilterName.CONTRAST -> repository.sliderContrast.maxVal
            FilterName.SATURATION -> repository.sliderSaturation.maxVal
            FilterName.SHARPEN -> repository.sliderSharpen.maxVal
            FilterName.VIGNETTE -> repository.sliderVignette.maxVal
        }
    }

    /**
     * [getSliderInitVal] is for Slider Setting Slider Value
     * @return [Float]
     */

    fun getSliderInitVal(): Float {
        return when (_recentTab.value!!) {
            FilterName.BRIGHTNESS -> _brightness.value!!
            FilterName.CONTRAST -> _contrast.value!!
            FilterName.SATURATION -> _saturation.value!!
            FilterName.SHARPEN -> _sharpen.value!!
            FilterName.VIGNETTE -> _vignette.value!!
        }
    }

    /**
     * [getSliderStepVal] is for Slider Setting Slider StepSize
     */

    fun getSliderStepVal(): Float? {
        return when (_recentTab.value!!) {
            FilterName.BRIGHTNESS -> repository.sliderBrightness.stepSize
            FilterName.CONTRAST -> repository.sliderContrast.stepSize
            FilterName.SATURATION -> repository.sliderSaturation.stepSize
            FilterName.SHARPEN -> repository.sliderSharpen.stepSize
            FilterName.VIGNETTE -> repository.sliderSharpen.stepSize
        }
    }

    /**
     * [getSliderDefaultValue] is for get slider Default value
     */

    private fun getSliderDefaultValue(): Float? {
        return when (_recentTab.value!!) {
            FilterName.BRIGHTNESS -> repository.sliderBrightness.defaultVal
            FilterName.CONTRAST -> repository.sliderContrast.defaultVal
            FilterName.SATURATION -> repository.sliderSaturation.defaultVal
            FilterName.SHARPEN -> repository.sliderSharpen.defaultVal
            FilterName.VIGNETTE -> repository.sliderSharpen.defaultVal
        }

    }

    /**
     * [calculatePercentAge] is inline-method for calculate for slider value percent
     * @param value for slider recent value
     * @return String
     */

    fun calculatePercentAge(value: Float): String = "${(100f * value / getMaxVal()).toInt()}%"


    fun changeTheBitmapToMainScreen(
        navigateToMainScreen: (Bitmap) -> Unit
    ) {
        filterGroup.destroy()
//        mainViewModel.setImgSrc(mOutputImage)
        navigateToMainScreen(mOutputImage)


    }


}