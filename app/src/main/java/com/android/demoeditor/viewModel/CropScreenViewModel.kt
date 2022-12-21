package com.android.demoeditor.viewModel

import android.annotation.SuppressLint
import android.app.Application
import com.android.demoeditor.customClass.BitmapHelper
import com.android.demoeditor.customClass.ResizePhoto
import com.android.demoeditor.customClass.ResizePhoto.*
import com.android.demoeditor.customClass.UriBitmapUtils
import com.android.demoeditor.data.parcelData.CommonParcelData
import com.android.demoeditor.enums.ActiveNavArgsData
import com.android.demoeditor.screens.CropFragmentArgs
import android.graphics.Bitmap
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import kotlinx.coroutines.*

class CropScreenViewModel(
    application: Application,
    navArgs: CropFragmentArgs,
    private val redirectToScreen:()->Unit
    ) :
    AndroidViewModel(application) {

    @SuppressLint("StaticFieldLeak")
    private val context = getApplication<Application>().applicationContext

    private val backgroundScope = CoroutineScope(Dispatchers.IO + Job())

    companion object {
        private val TAG = CropScreenViewModel::class.java.simpleName

    }

    /**
     * Img Source as Bitmap
     */

    private val _imgSrc = MutableLiveData<Bitmap>()
    val imgSrcBitmap: LiveData<Bitmap>
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


    private fun setUriOrBitmap(navArgs: CropFragmentArgs) {
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
                    // Navigate to screen
                    redirectToScreen()
                }

            }

            ActiveNavArgsData.URI -> {
                _isImgLoading.value = true
                convertUriToBitmap(navArgs.imgData)
            }

            ActiveNavArgsData.BYTE_ARRAY -> {

                Log.d(TAG, "BYTE_ARRAY Block is called")


//                backgroundScope.launch {
//                    val bitmap= BitmapByteArrayUtils.convertEncodedStringBitmap(navArgs.imgData.byteArrayStr!!)
//
//                    withContext(Dispatchers.Main){
//                        _imgSrc.value=bitmap
//                    }
//
//                }

            }


        }

    }


    private fun convertUriToBitmap(imgData: CommonParcelData) {
        if (imgData.uri != null) {
            UriBitmapUtils(imgData.uri, context, backgroundScope) {

                Log.d(TAG, "Uri :- bitmapHeight-${it.width} bitmapWidth-${it.height}  ")

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


}