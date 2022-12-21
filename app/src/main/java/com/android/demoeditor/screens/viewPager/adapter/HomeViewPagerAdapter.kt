package com.android.demoeditor.screens.viewPager.adapter

import com.android.demoeditor.data.ViewPagerData
import com.android.demoeditor.screens.viewPager.homeFragment.FirstViewpagerOfHomeFragment
import com.android.demoeditor.screens.viewPager.homeFragment.HomeRecentEditViewPagerFragment
import com.android.demoeditor.viewModel.HomeViewModel
import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter


class HomeViewPagerAdapter(fragment: Fragment, viewModel: HomeViewModel,) : FragmentStateAdapter(fragment) {

    private var dataArr: List<ViewPagerData> = listOf(
        ViewPagerData (  FirstViewpagerOfHomeFragment.injectValue(viewModel),"First"),
        ViewPagerData (  HomeRecentEditViewPagerFragment.injectValue(viewModel),"Recent"),
    )


    override fun getItemCount(): Int {
        return dataArr.size
    }

    override fun createFragment(position: Int): Fragment {
        return dataArr[position].fragment
    }
}