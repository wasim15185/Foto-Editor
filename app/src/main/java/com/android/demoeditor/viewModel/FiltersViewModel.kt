package com.android.demoeditor.viewModel

import android.annotation.SuppressLint
import android.app.Application
import com.android.demoeditor.customClass.BitmapHelper
import com.android.demoeditor.customClass.ResizePhoto
import com.android.demoeditor.customClass.ResizePhoto.*
import com.android.demoeditor.customClass.UriBitmapUtils
import com.android.demoeditor.data.FiltersNavItemData
import com.android.demoeditor.data.parcelData.CommonParcelData
import com.android.demoeditor.enums.ActiveNavArgsData
import com.android.demoeditor.repository.FiltersRepository
import com.android.demoeditor.screens.FiltersFragmentArgs
import android.graphics.Bitmap
import android.graphics.drawable.Drawable
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.bumptech.glide.Glide
import com.bumptech.glide.request.target.CustomTarget
import com.bumptech.glide.request.transition.Transition
import jp.co.cyberagent.android.gpuimage.GPUImage
import kotlinx.coroutines.*

class FiltersViewModel(
    application: Application,
                       private val navArgs: FiltersFragmentArgs,
    private val redirectToScreen:()->Unit
    ) :
    AndroidViewModel(application) {

    private val TAG = FiltersViewModel::class.java.simpleName

    private val backgroundScope = CoroutineScope(Dispatchers.IO + Job())


    private val repository = FiltersRepository()

    private val _listOfFilter: MutableLiveData<MutableList<FiltersNavItemData>> =
        MutableLiveData(mutableListOf())

    val listOfFilters: LiveData<MutableList<FiltersNavItemData>>
        get() = _listOfFilter

    @SuppressLint("StaticFieldLeak")
    private val context = getApplication<Application>().applicationContext

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


    private fun setUriOrBitmap(navArgs: FiltersFragmentArgs) {
        _isImgLoading.value = true

        when (navArgs.imgData.availableData) {
            ActiveNavArgsData.BITMAP -> {
                if (BitmapHelper.bitmap != null) {


                    /**
                     * if [BitmapHelper.isResized] is [true] don't resize
                     * else [BitmapHelper.isResized] is [false] do resize
                     */

                    if (BitmapHelper.isResized) {

                        updateListOfFilters(BitmapHelper.bitmap!!)
                        _imgSrc.value = BitmapHelper.bitmap


                    } else {
                        val resizePhoto =
                            ResizePhoto(context, BitmapHelper.bitmap!!, true, RESOLUTION.HD_720P)
                        backgroundScope.launch {
                            val resizeBitmap = resizePhoto.execute()
                            withContext(Dispatchers.Main) {

                                updateListOfFilters(resizeBitmap)
                                _imgSrc.value = resizeBitmap


                            }
                        }

                    }


                } else {
                    // navigate to some where
                    redirectToScreen()
                }

            }

            ActiveNavArgsData.URI -> {

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
                        updateListOfFilters(resizeBitmap)
                        _imgSrc.value = resizeBitmap


                    }
                }

            }

        } else {
            throw Exception("Uri not available")
        }
    }


//    private fun getListOfFilters(): MutableList<FiltersNavItemData> {
//
//        val list = mutableListOf<FiltersNavItemData>()
//
//        for ((i, filter) in repository.filters.withIndex()) {
//            list.add(i, FiltersNavItemData(filter))
//        }
//
//        return list
//
//    }


    private fun updateListOfFilters(
        bitmap: Bitmap
    ) {

        val deviceWidth = context.resources.displayMetrics.widthPixels

        Glide
            .with(context)
            .asBitmap()
            .override(deviceWidth, deviceWidth)
            .load(bitmap)
            .into(object : CustomTarget<Bitmap>() {
                override fun onResourceReady(
                    resource: Bitmap,
                    transition: Transition<in Bitmap>?
                ) {
                    dataUpdateForRecyclerView(resource)

                }

                override fun onLoadCleared(placeholder: Drawable?) {

                }
            })


    }





private fun dataUpdateForRecyclerView(bitmap: Bitmap) {

    backgroundScope.launch {

        val list = mutableListOf<FiltersNavItemData>()

        val newBitmap: Bitmap? = bitmap.copy(bitmap.config, true)
        var gpuImage: GPUImage? = GPUImage(context)


        repository.filters.forEach { filter ->
            gpuImage?.apply {
                setFilter(filter)
                val b = getBitmapWithFilterApplied(newBitmap)
                list.add(FiltersNavItemData(bitmap = b, filter = filter))

            }
        }

        gpuImage = null

        withContext(Dispatchers.Main) {

            _listOfFilter.value = list
            _isImgLoading.value = false
        }

    }
}


}