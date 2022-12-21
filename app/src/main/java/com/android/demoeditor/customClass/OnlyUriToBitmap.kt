package com.android.demoeditor.customClass

import android.content.Context
import android.graphics.Bitmap
import android.graphics.ImageDecoder
import android.net.Uri
import android.os.Build
import android.provider.MediaStore
import android.util.Log
import android.widget.Toast
import androidx.annotation.RequiresApi
import java.lang.Exception

class OnlyUriToBitmap(private val context: Context) {

    private val TAG = this::class.java.simpleName

    private val resolutionWidth = 1280

    fun convert(uri: Uri): Bitmap? {
        val newBitmap = if (Build.VERSION.SDK_INT >= 29) {
            newVersionImgDecoder(uri)
        } else {
            oldVersionImgDecoder(uri)
        }

        if (newBitmap != null) {
            return getScaleBitmap(newBitmap)
        } else {
            throw Exception("Bitmap is null")
        }

    }


    private fun getScaleBitmap(_bitmap: Bitmap): Bitmap {


        var newWidth = 0
        var newHeight = 0

        Log.d("Pictures", "Width and height are ${_bitmap.width}--${_bitmap.width}")

        if (_bitmap.width >= _bitmap.height) {
            val ratio: Float = _bitmap.width.toFloat() / _bitmap.height.toFloat()


            newWidth = resolutionWidth
            // Calculate the new height for the scaled _bitmap
            newHeight = Math.round(resolutionWidth / ratio)
        } else {
            val ratio: Float = _bitmap.height.toFloat() / _bitmap.width.toFloat()

            // Calculate the new width for the scaled _bitmap
            newWidth = Math.round(resolutionWidth / ratio)
            newHeight = resolutionWidth
        }

        Log.d("Pictures", "Width and height are $newWidth--$newHeight")
        return Bitmap.createScaledBitmap(
            _bitmap,
            newWidth,
            newHeight,
            false
        )


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