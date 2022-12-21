package com.android.demoeditor.screens.viewPager.homeFragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.android.demoeditor.R
import com.android.demoeditor.data.parcelData.CommonParcelData
import com.android.demoeditor.database.RecentPhotoDatabase
import com.android.demoeditor.databinding.FragmentHomeRecentEditViewPagerBinding
import com.android.demoeditor.enums.ActiveNavArgsData
import com.android.demoeditor.recyclerViews.RecentPhotosRecyclerViewAdapter
import com.android.demoeditor.recyclerViews.RecentPhotosSelectorClickListener
import com.android.demoeditor.repository.HomeRecentEditViewPagerRepository
import com.android.demoeditor.screens.HomeFragmentDirections
import com.android.demoeditor.viewModel.HomeViewModel
import com.android.demoeditor.viewModel.viewPager.homeFragment.HomeRecentEditViewPagerViewModel
import com.android.demoeditor.viewModelFactory.viewPager.homeFragment.HomeRecentEditViewPagerViewModelFactory
import android.graphics.Rect
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.grzegorzojdana.spacingitemdecoration.Spacing
import com.grzegorzojdana.spacingitemdecoration.SpacingItemDecoration


class HomeRecentEditViewPagerFragment : Fragment() {

    private val TAG = this::class.java.simpleName

    private lateinit var binding: FragmentHomeRecentEditViewPagerBinding

    private lateinit var viewModel: HomeRecentEditViewPagerViewModel

    private lateinit var recyclerViewAdapter: RecentPhotosRecyclerViewAdapter

    private lateinit var recyclerViewClickListener: RecentPhotosSelectorClickListener


    companion object{
       private var parentStaticViewModel :HomeViewModel?=null
        fun injectValue(viewModel:HomeViewModel):Fragment{
            parentStaticViewModel=viewModel
            return HomeRecentEditViewPagerFragment()
        }
    }

    private val parentViewModel by lazy {
        parentStaticViewModel
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = DataBindingUtil.inflate(
            layoutInflater,
            R.layout.fragment_home_recent_edit_view_pager,
            container,
            false
        )

        val db = RecentPhotoDatabase.getInstance(requireContext())

        val repository = HomeRecentEditViewPagerRepository(requireContext(), db)

        val factory = HomeRecentEditViewPagerViewModelFactory(repository)

        /**
         * initializing [viewModel]
         */
        viewModel = ViewModelProvider(
            this,
            factory
        ).get(HomeRecentEditViewPagerViewModel::class.java)

        // Specify the current activity as the lifecycle owner.
        binding.lifecycleOwner = this
        binding.viewModel=viewModel

        setupRecyclerView()





        return binding.root
    }

    private fun setupRecyclerView() {

        recyclerViewClickListener = RecentPhotosSelectorClickListener {
            val data = CommonParcelData(uri = it, availableData = ActiveNavArgsData.URI)
            navigateEditScreen(data)

        }

        val spacingItemDecoration = SpacingItemDecoration(
            Spacing(horizontal = 35, vertical = 35, edges = Rect(0, 25, 0, 0))
        )

        binding.recentEditRecyclerView.addItemDecoration(spacingItemDecoration)


        recyclerViewAdapter = RecentPhotosRecyclerViewAdapter(recyclerViewClickListener)

        binding.recentEditRecyclerView.adapter = recyclerViewAdapter

        viewModel.recyclerViewData.observe(viewLifecycleOwner, {


            recyclerViewAdapter.submitList(it)
        })

    }


    private fun navigateEditScreen(data: CommonParcelData) {
        val action =
            HomeFragmentDirections.homeFragmentToMainEditScreenFragment(data)
        findNavController().navigate(action)
    }


}