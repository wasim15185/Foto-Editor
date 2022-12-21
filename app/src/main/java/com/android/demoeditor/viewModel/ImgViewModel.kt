package com.android.demoeditor.viewModel

import android.annotation.SuppressLint
import android.app.Activity
import android.app.Application
import com.android.demoeditor.customClass.ImgContentResolverData
import com.android.demoeditor.data.AlbumData
import com.android.demoeditor.data.ImgSelectorData
import com.android.demoeditor.recyclerViews.ImageShowerAdapter
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.*

class ImgViewModel(application: Application, adapter: ImageShowerAdapter, activity: Activity) :
    AndroidViewModel(application) {

    companion object {
        private val TAG = ImgViewModel::class.java.simpleName
    }

    private var mLatestThread: Thread? = null

    @SuppressLint("StaticFieldLeak")
    private val context = getApplication<Application>().applicationContext

    /**
     * [_activity] for getting activity
     */

    private val _activity = MutableLiveData<Activity>()

    private val job = CoroutineScope(Dispatchers.Default + Job())

//    private val _dataOfImgShowerRecyclerView = MutableLiveData(mutableListOf<ImgSelectorData>())
//
//    val dataOfImgShowerRecyclerView: LiveData<MutableList<ImgSelectorData>>
//        get() = _dataOfImgShowerRecyclerView


    private val _dataOfImgShowerRecyclerView = MutableLiveData<ArrayList<ImgSelectorData>>()

    val dataOfImgShowerRecyclerView: LiveData<ArrayList<ImgSelectorData>>
        get() = _dataOfImgShowerRecyclerView


    val _isListUpdated=MutableLiveData(false)

    private var mImageDataSource: ImgContentResolverData = ImgContentResolverData(context.contentResolver)
    var mAlbums = MutableLiveData<ArrayList<AlbumData>>()
    var mCurrentSelectedAlbum = 0

    private var mSelectedAlbum: AlbumData? = null
    var mPage = 0


    init {

        _activity.value = activity
        mSelectedAlbum= AlbumData("",true,"")

        // setDataInRecyclerView(adapter)

    }

    /**
     * Album
     */

    fun onAlbumChanged(item: AlbumData?, pos: Int) {
        mSelectedAlbum = item
//        mSelectedList.clear()
//        mCurrentSelection = 0
        mCurrentSelectedAlbum = pos
        loadImages()
    }


    internal fun loadAlbums() {
        if (!mAlbums.value.isNullOrEmpty()) {
            return
        }
        viewModelScope.launch {
            val albums = getAlbums()
            mAlbums.value = albums
            loadImages()
        }
    }







    private suspend fun getAlbums() = withContext(Dispatchers.Default) {
        mImageDataSource.loadAlbums()
    }






    /**
     * Image
     */

     fun loadMoreImages() {
        loadImages(true)
    }


    private fun loadImages(isLoadMore: Boolean = false) {
        if (isLoadMore) {
            mPage += 1
        } else {
            mPage = 0
        }
        viewModelScope.launch() {
            val images = getImages()
            Log.d(TAG, "loadImages: img-size : ${images.size}")
            if (mPage==1){
                _dataOfImgShowerRecyclerView.value=images
            }else{
                _dataOfImgShowerRecyclerView.value?.addAll(images)
                _isListUpdated.value=!_isListUpdated.value!!
            }


        }
    }

    private suspend fun getImages() = withContext(Dispatchers.Default) {
        mImageDataSource.loadAlbumImages(mSelectedAlbum, mPage)
    }








}