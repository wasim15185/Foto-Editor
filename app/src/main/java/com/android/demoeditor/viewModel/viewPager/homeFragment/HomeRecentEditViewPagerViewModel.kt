package com.android.demoeditor.viewModel.viewPager.homeFragment

import com.android.demoeditor.recyclerViews.RecentPhotoItem
import com.android.demoeditor.repository.HomeRecentEditViewPagerRepository
import android.net.Uri
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel
import kotlinx.coroutines.*
import java.lang.Exception


class HomeRecentEditViewPagerViewModel(private val repository: HomeRecentEditViewPagerRepository) :
    ViewModel() {

    private val viewModelJob = Job()

    private val uiScope = CoroutineScope(Dispatchers.Main)

    private val TAG = this::class.java.simpleName


    private val _isRecyclerViewEmpty =MutableLiveData<Boolean>(true)
    val isRecyclerViewEmpty :LiveData<Boolean>
    get() = _isRecyclerViewEmpty


    private val allRecentData = repository.getAllRecentPhoto()

    val recyclerViewData = Transformations.map(allRecentData) {

        setEmptyIconVisibleOrNot(it.size)

        val list = mutableListOf<RecentPhotoItem>()

        it.forEach {
            val uri = Uri.parse(it.uriString)

            if (isUriExist(uri)) {
                list.add(RecentPhotoItem(uri = uri))
            }else{
                uiScope.launch {
                    deletePhotoFromDb(it.id)
                }

            }
        }

        return@map list
    }


    private fun isUriExist(uri: Uri): Boolean {
        var bool = false
        try {
            val inputStream =
                repository.context.contentResolver.openInputStream(uri)
            inputStream?.close()
            bool = true
        } catch (e: Exception) {
            Log.w(TAG, "File corresponding to the uri does not exist $uri")
        }
        return bool
    }


    private suspend fun deletePhotoFromDb(id:Long){
        Log.d(TAG, " Delete Photo From Database where id is $id")
        withContext(Dispatchers.IO){
            repository.database.photoDatabaseDao.deletePhoto(id)
        }
    }



    private fun setEmptyIconVisibleOrNot(arrLength:Int){
        Log.d(TAG, "  arr-length : $arrLength")

        _isRecyclerViewEmpty.value = arrLength < 1

    }



}