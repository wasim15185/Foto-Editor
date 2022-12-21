package com.android.demoeditor.viewModel

import android.annotation.SuppressLint
import android.app.Activity
import android.app.Application
import com.android.demoeditor.customClass.SaveBitmapInStorage
import com.android.demoeditor.customClass.backgroundThread.BitmapToUriConverter
import com.android.demoeditor.data.CollageEditItemData
import com.android.demoeditor.data.ColorDataInCollageRecyclerView
import com.android.demoeditor.data.ColorPickerData
import com.android.demoeditor.data.LayoutSelectorRecyclerViewItemData
import com.android.demoeditor.enums.ImgExtension
import com.android.demoeditor.repository.CollageSelectorRepository
import android.graphics.Bitmap
import android.graphics.drawable.Drawable
import android.net.Uri
import android.util.Log
import android.widget.Toast
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.bumptech.glide.Glide
import com.bumptech.glide.request.target.CustomTarget
import com.bumptech.glide.request.transition.Transition
import kotlinx.coroutines.*

class CollageSelectorViewModel(
    application: Application,
    val listOfUris: List<Uri>,
    activity: Activity,
) :
    AndroidViewModel(application) {

    private val TAG = this::class.java.simpleName


    private var repository: CollageSelectorRepository

    private val backgroundScope = CoroutineScope(Dispatchers.Default + Job())


    @SuppressLint("StaticFieldLeak")
    private val context = getApplication<Application>().applicationContext

    /**
     * [_recyclerViewLayoutSelectorData] is live data for LayoutSelectorData
     */
    private val _recyclerViewLayoutSelectorData =
        MutableLiveData<MutableList<LayoutSelectorRecyclerViewItemData>>()
    val recyclerViewLayoutSelectorData: LiveData<MutableList<LayoutSelectorRecyclerViewItemData>>
        get() = _recyclerViewLayoutSelectorData


    /**
     * [_listOfImages] is live data for puzzle-view
     */
    private val _listOfImages = MutableLiveData<MutableList<Bitmap>>()
    val listOfImages: LiveData<MutableList<Bitmap>>
        get() = _listOfImages

    /**
     * [_colorRecyclerViewData] is live data for Color-Recycler View
     */
    private val _colorRecyclerViewData =
        MutableLiveData<MutableList<ColorPickerData>>(mutableListOf())
    val colorRecyclerViewData: LiveData<MutableList<ColorPickerData>>
        get() = _colorRecyclerViewData


    /**
     * Data of  current-color recycler-view
     */
    private val _recentColorData = MutableLiveData<ColorDataInCollageRecyclerView>()
    val recentColorData: LiveData<ColorDataInCollageRecyclerView>
        get() = _recentColorData

    /**
     * Data for Recycler-View of [CollageEditViewPagerFragment]  <-- this should delete
     */
    private val _collageEditScreenRecyclerViewData =
        MutableLiveData<MutableList<CollageEditItemData>>(
            mutableListOf()
        )
    val collageEditScreenRecyclerViewData: LiveData<MutableList<CollageEditItemData>>
        get() = _collageEditScreenRecyclerViewData

    /**
     * progress
     */
    private val _progress = MutableLiveData(0f)
    val process: LiveData<Float>
        get() = _progress


    /**
     * [_isImgLoading] Live Data for is Image is Loading or not ???
     */

    private val _isImgLoading = MutableLiveData<Boolean>(false)
    val isImgLoading: LiveData<Boolean>
        get() = _isImgLoading


    private var thread: Thread? = null

    private val mLock = Any()

    init {

        repository = CollageSelectorRepository(context)

        _collageEditScreenRecyclerViewData.value = repository.dataForEditViewPagerRecyclerView()

        _recyclerViewLayoutSelectorData.value = mutableListOf()

        // setting data for LayoutSelector recycler-view
        setDataRecyclerViewForLayoutSelector(listOfUris)

        _colorRecyclerViewData.value = repository.dataForBackgroundColor()


        //  if thread is not null then set thread = null then initlized value inside thread
        if (thread != null) thread = null

        thread = BitmapToUriConverter(mLock, context, uris = listOfUris) { bitmapArr, progress ->
            activity.runOnUiThread {
                _progress.value = progress
                if (bitmapArr.size == listOfUris.size) {
                    _isImgLoading.value = true
                    _listOfImages.value = bitmapArr

                }
            }
        }

        // starting the thread
        // thread!!.start()

        getImages()
    }


    /**
     * [setDataRecyclerViewForLayoutSelector] is for setting data inside [CollageNavbarAdapter] recycler-view
     * @param listOfUris  [List] of uri
     */
    private fun setDataRecyclerViewForLayoutSelector(listOfUris: List<Uri>) {
        _recyclerViewLayoutSelectorData.value =
            repository.dataForRecyclerViewForLayoutSelector(listOfUris)
    }


    /**
     * [getInitLayoutForPuzzleLayout] is for getting default element data for puzzle_view
     * @return [LayoutSelectorRecyclerViewItemData]
     */
    fun getInitLayoutForPuzzleLayout(): LayoutSelectorRecyclerViewItemData? {
        return _recyclerViewLayoutSelectorData.value?.get(0)
    }


    /**
     *
     */

    fun updateRecentColor(color: Int, position: Int) {
        val recentColorInfo = ColorDataInCollageRecyclerView(color, position)
        _recentColorData.value = recentColorInfo
    }


    private fun getImages() {
        val list = mutableListOf<Bitmap>()

        val deviceWidth = context.resources.displayMetrics.widthPixels

        for (uri in listOfUris) {

            Glide
                .with(context)
                .asBitmap()
                .override(deviceWidth, deviceWidth)
                .load(uri)
                .into(object : CustomTarget<Bitmap>() {
                    override fun onResourceReady(
                        resource: Bitmap,
                        transition: Transition<in Bitmap>?
                    ) {

                        Log.d(TAG, "onResourceReady: called")
                        list.add(resource)
                        if (list.size == listOfUris.size) {
                            _isImgLoading.value = true
                            _listOfImages.value = list
                        }

                    }

                    override fun onLoadCleared(placeholder: Drawable?) {
                    }
                })

        }
    }

    /**
     * [savePhotoInFile] is store [bitmap] as Image File in Storage and also create [Uri]
     * @param bitmap ,
     * @param result is callback
     */
    fun savePhotoInFile(bitmap: Bitmap, result: (Uri) -> Unit) {

        backgroundScope.launch {


            val saveBitmapInStorage = SaveBitmapInStorage(context)
            saveBitmapInStorage.changeImgExtension(ImgExtension.JPG)

            val uri = saveBitmapInStorage.save(bitmap)

            delay(3000)
 
            withContext(Dispatchers.Main) {
                result(uri)
                Toast.makeText(context, "Photo is saved...", Toast.LENGTH_SHORT).show()
            }

        }


    }


}