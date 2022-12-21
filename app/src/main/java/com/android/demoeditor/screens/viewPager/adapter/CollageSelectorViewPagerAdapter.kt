package com.android.demoeditor.screens.viewPager.adapter

import com.android.demoeditor.data.ViewPagerData
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Lifecycle

class CollageSelectorViewPagerAdapter(fragmentManager: FragmentManager,
                                      lifecycle: Lifecycle,
                                      private val dataArr: List<ViewPagerData>) :StickerViewPagerAdapter(fragmentManager, lifecycle, dataArr)