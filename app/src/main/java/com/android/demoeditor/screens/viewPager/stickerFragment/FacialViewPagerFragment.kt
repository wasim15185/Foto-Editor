package com.android.demoeditor.screens.viewPager.stickerFragment

import android.app.Application
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.android.demoeditor.R
import com.android.demoeditor.databinding.FragmentFacialViewPagerBinding
import com.android.demoeditor.databinding.FragmentStickerBinding
import com.android.demoeditor.recyclerViews.StickerAdapter
import com.android.demoeditor.recyclerViews.StickerItemListener
import com.android.demoeditor.screens.viewPager.stickerFragment.FoodViewPagerFragment.Companion.injectStickerBinding
import com.android.demoeditor.viewModel.viewPager.stickersFragment.FacialViewPagerViewModel
import com.android.demoeditor.viewModelFactory.viewPager.stickerFragment.FacialViewPagerViewModelFactory
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider


class FacialViewPagerFragment : Fragment() {

    private lateinit var binding:FragmentFacialViewPagerBinding
    private lateinit var viewModel:FacialViewPagerViewModel

    companion object{
        private var stickerBinding: FragmentStickerBinding? = null

        /**
         * [injectStickerBinding] for inject StickerFragment binding object inside [FoodViewPagerFragment]
         * @param bind [FragmentNewStickerBinding]
         * @return [Fragment]
         */
        fun injectStickerBinding(bind: FragmentStickerBinding): Fragment {
             stickerBinding = bind
            return FacialViewPagerFragment()
        }

    }

    private val stickerFragmentBind by lazy {
        stickerBinding
    }


    override fun onDestroy() {
        super.onDestroy()
        stickerBinding =null
    }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = DataBindingUtil.inflate(layoutInflater,R.layout.fragment_facial_view_pager, container, false)

        val stickerList = resources.obtainTypedArray(R.array.facial_photo)

        val factory = FacialViewPagerViewModelFactory(requireContext().applicationContext as Application,stickerList)

        viewModel= ViewModelProvider(this, factory).get(FacialViewPagerViewModel::class.java)


        setupRecyclerView()


        return binding.root
    }

    private fun setupRecyclerView(){

        val adapter = StickerAdapter(StickerItemListener{
            if (stickerBinding?.stickerViewModel?.isImgLoading?.value == false) {
                val drawableSticker = com.android.demoeditor.customView.stickerPackage.DrawableSticker(it)
                stickerFragmentBind?.stickerView?.addSticker(drawableSticker)
            }
        })

        binding.facialRecyclerView.adapter=adapter

        viewModel.recyclerViewData.observe(viewLifecycleOwner,  {
            it?.let {
                adapter.submitList(it)
            }
        })


    }










}