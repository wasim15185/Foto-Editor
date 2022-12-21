package com.android.demoeditor.customClass.backgroundThread

import android.content.Context
import com.android.demoeditor.customClass.ResizePhoto
import android.graphics.Bitmap
import android.graphics.ImageDecoder
import android.net.Uri
import android.os.Build
import android.provider.MediaStore
import android.util.Log
import android.widget.Toast
import androidx.annotation.RequiresApi
import java.lang.Exception
import kotlin.system.measureTimeMillis

class BitmapToUriConverter(
    private val mLock: Any,
    private val context: Context,
    private val uris: List<Uri>,
    private val result: (MutableList<Bitmap>,Float) -> Unit
) : Thread() {


    private val TAG = this::class.java.simpleName

    private val listOfBitmap = mutableListOf<Bitmap>()


    var isRenderscriptEnable = false



    override fun run() {
        synchronized(mLock) {


            val time = measureTimeMillis {

                var initProcess=0f
//
                for ( uri in uris) {
                    val bitmap = uriToBitmap(uri)


                    bitmap?.let {
                        listOfBitmap.add(it)
                        val process =getCurrentProgress(++initProcess)
                            result(listOfBitmap,process)
                    }


                }

            }

            Log.d(TAG, " class time take is $time")

        }

    }


    private fun uriToBitmap(uri: Uri): Bitmap? {
        val bitmap = if (Build.VERSION.SDK_INT >= 29) {
            newVersionImgDecoder(uri)
        } else {
            oldVersionImgDecoder(uri)
        }


        if (bitmap != null) {
            return if (bitmap.width >= ResizePhoto.RESOLUTION.HD_720P.width && bitmap.height >= ResizePhoto.RESOLUTION.HD_720P.height) {

                val resizePhoto = ResizePhoto(
                    context,
                    bitmap,
                    isRenderscriptEnable,
                    ResizePhoto.RESOLUTION.HD_720P
                )

                Log.d(TAG, "Bitmap Converted in $TAG-Class using Renderscript ")

                resizePhoto.execute() // return

            } else {

                Log.d(TAG, " Bitmap is Already Reside!!! ")

                bitmap // return

            }
        } else {
            throw NullPointerException("Bitmap is null")
        }


    }


    private fun getCurrentProgress(p:Float): Float {
        val totalProcess= uris.size.toFloat()

       return (p/totalProcess)*100
    }

    private fun oldVersionImgDecoder(uri: Uri): Bitmap? {

        var bitmap: Bitmap? = null

        try {
            bitmap = MediaStore.Images.Media.getBitmap(context.contentResolver, uri)
                .copy(Bitmap.Config.ARGB_8888, true)
        } catch (e: Exception) {
            Toast.makeText(context, "${e.message}", Toast.LENGTH_SHORT).show()
        }

        return bitmap

    }


    @RequiresApi(Build.VERSION_CODES.P)
    private fun newVersionImgDecoder(uri: Uri): Bitmap? {


        val imgDecoderSrc =
            ImageDecoder.createSource(context.contentResolver, uri)

        return ImageDecoder.decodeBitmap(imgDecoderSrc).copy(Bitmap.Config.ARGB_8888, true)

    }


}