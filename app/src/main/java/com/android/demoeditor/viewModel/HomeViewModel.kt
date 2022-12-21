package com.android.demoeditor.viewModel

import android.annotation.SuppressLint
import android.app.Application
import androidx.lifecycle.AndroidViewModel

class HomeViewModel(application: Application) : AndroidViewModel(application) {
    private val TAG = FiltersViewModel::class.java.simpleName

    @SuppressLint("StaticFieldLeak")
    private val context = getApplication<Application>().applicationContext





}