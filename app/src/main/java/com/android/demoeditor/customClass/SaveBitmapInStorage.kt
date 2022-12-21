package com.android.demoeditor.customClass

import android.content.Context
import com.android.demoeditor.R
import com.android.demoeditor.enums.ImgExtension
import android.graphics.Bitmap
import android.net.Uri
import android.util.Log
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import android.media.MediaScannerConnection


class SaveBitmapInStorage(private val context: Context) {

    companion object {
        private val TAG = SaveBitmapInStorage::class.java.simpleName

    }

    var compressionQuality = 100
        set(value) {
            if (value in 0..100) {
                field = value
            } else {
                throw Exception("compressionQuality value must be between 0 to 100")
            }
        }

    private var imgExtension = ImgExtension.PNG.name.lowercase()

    fun changeImgExtension(imgType: ImgExtension) {
        imgExtension = imgType.name.lowercase()
    }

    private val dirName = context.getString(R.string.app_name)

    private val appName = dirName

//    private val folder =    File(context.getExternalFilesDir(null)?.path + dirName)
//    private val folder =  File("${context.getExternalFilesDir(Environment.DIRECTORY_DCIM)?.path}${File.separator}$dirName" )
//    private val folder = File( Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM),dirName )

    private val folder =
        File(context.getExternalFilesDir(null)?.parentFile?.parentFile?.parentFile?.parentFile?.path + File.separator + appName)


    // It must be execute on background thread
    fun save(bitmap: Bitmap, isSaveInCacheDir: Boolean = false): Uri {
        if (!folder.isDirectory) {
            folder.mkdir()
            Log.d(TAG, " file created :-> $folder")
        }


        val imgFile = if (isSaveInCacheDir) File(context.cacheDir, getCacheImgFile)
        else File(folder, "$appName${System.currentTimeMillis()}.$imgExtension")


        try {

            val fileOutputStream = FileOutputStream(imgFile)

            checkImgFormat(bitmap, fileOutputStream)

            fileOutputStream.apply {
                flush();
                close()
            }


        } catch (e: IOException) {
            e.printStackTrace();

        } finally {
//            context.sendBroadcast( Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, Uri.fromFile(imgFile))); <-- this is deprecated

            // this is notify the gallery a file is added
            MediaScannerConnection.scanFile(context, arrayOf(imgFile.toString()), null, null)

        }

        return Uri.fromFile(imgFile)


    }


    private fun checkImgFormat(bitmap: Bitmap, fos: FileOutputStream) {
        when (imgExtension) {
            ImgExtension.PNG.name.lowercase() -> {
                bitmap.compress(Bitmap.CompressFormat.PNG, compressionQuality, fos)
            }

            ImgExtension.JPG.name.lowercase() -> {
                bitmap.compress(Bitmap.CompressFormat.JPEG, compressionQuality, fos)
            }


        }

    }


    private val getCacheImgFile = "${context.getString(R.string.cache_img_name)}.$imgExtension"

    // This should be in background thread ...
    fun savePhotoInCacheDir(bitmap: Bitmap): Uri {
        return save(bitmap, true)

    }


    private fun isUriExist(uri: Uri):Boolean{
        var bool = false
        try {
            val inputStream = context.contentResolver.openInputStream(uri)
            inputStream?.close()
            bool = true
        } catch (e: java.lang.Exception) {
            Log.w(TAG, "File corresponding to the uri does not exist $uri")
        }
        return bool
    }


}