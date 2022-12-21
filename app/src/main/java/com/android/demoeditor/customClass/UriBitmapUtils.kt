package com.android.demoeditor.customClass

import android.content.Context
import android.graphics.*
import android.net.Uri
import android.os.Build
import android.provider.MediaStore
import android.widget.Toast
import com.android.demoeditor.customClass.ResizePhoto.RESOLUTION.*
import android.util.Log

import androidx.annotation.RequiresApi
import kotlinx.coroutines.*
import java.lang.Exception
import kotlin.system.measureTimeMillis

class UriBitmapUtils(
    private val uri: Uri,
    private val context: Context,
    private val backgroundScope: CoroutineScope, // this is not using
    private val result: suspend (Bitmap) -> Unit
) {


    private val TAG = this::class.java.simpleName

    var isRenderScriptEnable = true


    init {
        uriToBitmap()
    }


    private fun uriToBitmap() {

        backgroundScope.launch(Dispatchers.IO) {

            if (Build.VERSION.SDK_INT >= 29) {
                newVersionImgDecoder(uri)
            } else {
                oldVersionImgDecoder(uri)
            }
        }

    }


    /**
     * [oldVersionImgDecoder] is old version which convert 'Uri to Bitmap'
     * @param uri [Uri]
     */
    private suspend fun oldVersionImgDecoder(uri: Uri) {
        try {
            val bitmap =
                MediaStore.Images.Media.getBitmap(context.contentResolver, uri)



//            backgroundScope.launch {
            val resizeBitmap = doResizeBitmap(bitmap)
            result(resizeBitmap)
//            }


        } catch (e: Exception) {
            Toast.makeText(context, "${e.message}", Toast.LENGTH_SHORT).show()

        }
    }


    /**
     * [newVersionImgDecoder] is new version which convert 'Uri to Bitmap'
     * @param uri [Uri]
     */
    @RequiresApi(Build.VERSION_CODES.P)
    private suspend fun newVersionImgDecoder(uri: Uri) {


        try {
            val imgDecoderSrc =
                ImageDecoder.createSource(context.contentResolver, uri)

            val bitmap =
                ImageDecoder.decodeBitmap(imgDecoderSrc).copy(Bitmap.Config.ARGB_8888, true)

//            backgroundScope.launch {
            val resizeBitmap = doResizeBitmap(bitmap)
            result(resizeBitmap)
//            }

        } catch (e: Exception) {
            Toast.makeText(context, "${e.message}", Toast.LENGTH_SHORT).show()

        }


    }

    /**
     * [doResizeBitmap] which convert 'bitmap into resize bitmap'
     * @param bitmap [Bitmap]
     * @return [Bitmap]
     */
    private fun doResizeBitmap(bitmap: Bitmap): Bitmap {
        return if (bitmap.width >= HD_720P.width && bitmap.height >= HD_720P.height) {
            var resizedBitmap: Bitmap

            val time = measureTimeMillis {
                val resizePhoto = ResizePhoto(context, bitmap, isRenderScriptEnable, HD_720P)
                Log.d(TAG, "Bitmap Converted in $TAG-Class using Renderscript ")
                resizedBitmap = resizePhoto.execute()
            }

            Log.d(TAG, "Time taken $time using Filter")
            resizedBitmap

        } else {
            bitmap
        }

    }


}