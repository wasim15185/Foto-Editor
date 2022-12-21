package com.android.demoeditor.viewModel

import android.annotation.SuppressLint
import android.app.Application
import com.android.demoeditor.customClass.BitmapHelper
import com.android.demoeditor.customClass.ResizePhoto
import com.android.demoeditor.customClass.ResizePhoto.*
import com.android.demoeditor.customClass.UriBitmapUtils
import com.android.demoeditor.data.parcelData.CommonParcelData
import com.android.demoeditor.enums.ActiveNavArgsData
import com.android.demoeditor.screens.RotationFragmentArgs
import android.graphics.Bitmap
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import kotlinx.coroutines.*

class RotationViewModel(
                        application: Application,
                        navArgs: RotationFragmentArgs,
                        private val redirectToScreen:()->Unit,
                        ) :
    AndroidViewModel(application) {

    companion object {
        private val TAG = RotationViewModel::class.java.simpleName
    }

    private val backgroundScope = CoroutineScope(Dispatchers.IO + Job())


    @SuppressLint("StaticFieldLeak")
    private val context = getApplication<Application>().applicationContext

    /**
     * [_imgSrc] Img Src
     */

    private val _imgSrc = MutableLiveData<Bitmap>()
    val imgSrc: LiveData<Bitmap>
        get() = _imgSrc


    /**
     * is Img Loading
     */
    private val _isImgLoading = MutableLiveData<Boolean>(false)
    val isImgLoading: LiveData<Boolean>
        get() = _isImgLoading


    init {

        setUriOrBitmap(navArgs)

    }

    private fun setUriOrBitmap(navArgs: RotationFragmentArgs) {
        when (navArgs.imgData.availableData) {
            ActiveNavArgsData.BITMAP -> {
                if (BitmapHelper.bitmap != null) {

                    _isImgLoading.value = true

                    /**
                     * if [BitmapHelper.isResized] is [true] don't resize
                     * else [BitmapHelper.isResized] is [false] do resize
                     */

                    if (BitmapHelper.isResized) {

                        _imgSrc.value = BitmapHelper.bitmap
                        _isImgLoading.value = false

                    } else {
                        val resizePhoto =
                            ResizePhoto(context, BitmapHelper.bitmap!!, true, RESOLUTION.HD_720P)
                        backgroundScope.launch {
                            val resizeBitmap = resizePhoto.execute()
                            withContext(Dispatchers.Main) {
                                _imgSrc.value = resizeBitmap
                                _isImgLoading.value = false
                            }
                        }
                    }


                } else {
                    // navigate screen
                    redirectToScreen()
                }

            }

            ActiveNavArgsData.URI -> {

                _isImgLoading.value = true

                convertUriToBitmap(navArgs.imgData)
            }

        }

    }


    private fun convertUriToBitmap(imgData: CommonParcelData) {
        if (imgData.uri != null) {
            UriBitmapUtils(imgData.uri, context, backgroundScope) {
                val resizePhoto = ResizePhoto(context, it, true, RESOLUTION.HD_720P)

                 backgroundScope.launch {

                    val resizeBitmap = resizePhoto.execute()
                    withContext(Dispatchers.Main) {
                        _isImgLoading.value = false
                        _imgSrc.value = resizeBitmap

                    }
                }

            }

        } else {
            throw Exception("Uri not available")
        }
    }


    /**
     * this fun change value of [_isImgLoading]
     */
    fun setLoadingState(value: Boolean) {
        _isImgLoading.value = value
    }


}