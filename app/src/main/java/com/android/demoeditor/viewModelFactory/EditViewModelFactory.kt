package com.android.demoeditor.viewModelFactory

 import android.app.Activity
 import android.app.Application
 import com.android.demoeditor.screens.EditScreenFragmentArgs
 import com.android.demoeditor.viewModel.EditViewModel
 import androidx.lifecycle.ViewModel
 import androidx.lifecycle.ViewModelProvider

class EditViewModelFactory(private val application: Application,
                           private val navArgsData: EditScreenFragmentArgs,
                           private val activity: Activity,
                           private val redirectToScreen:()->Unit ,

                           ) : ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(EditViewModel::class.java)){
            return EditViewModel(application , navArgsData,activity,redirectToScreen) as T
        }

        throw IllegalArgumentException(" MainEditViewModel Not Found !!! ")

    }
}