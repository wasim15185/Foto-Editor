package com.android.demoeditor.screens.viewPager.stickerFragment

import android.app.Application
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.android.demoeditor.databinding.FragmentFoodViewPagerBinding
import com.android.demoeditor.databinding.FragmentStickerBinding
import com.android.demoeditor.recyclerViews.StickerAdapter
import com.android.demoeditor.recyclerViews.StickerItemListener
import com.android.demoeditor.viewModel.viewPager.stickersFragment.FoodViewPagerViewModel
import com.android.demoeditor.viewModelFactory.viewPager.stickerFragment.FoodViewPagerViewModelFactory
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider


class FoodViewPagerFragment : Fragment() {

    private lateinit var binding: FragmentFoodViewPagerBinding
    private lateinit var foodViewModel: FoodViewPagerViewModel

    companion object {
        private var stickerBinding: FragmentStickerBinding? = null

        /**
         * [injectStickerBinding] for inject StickerFragment binding object inside [FoodViewPagerFragment]
         * @param bind [FragmentNewStickerBinding]
         * @return [Fragment]
         */
        fun injectStickerBinding(bind: FragmentStickerBinding): Fragment {
            stickerBinding = bind
            return FoodViewPagerFragment()
        }

    }


    private val stickerFragmentBind by lazy {
        stickerBinding
    }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentFoodViewPagerBinding.inflate(layoutInflater, container, false)


        val factory =
            FoodViewPagerViewModelFactory(requireContext().applicationContext as Application)
        foodViewModel = ViewModelProvider(this, factory).get(FoodViewPagerViewModel::class.java)

        val adapter = StickerAdapter(StickerItemListener {
            if (stickerBinding?.stickerViewModel?.isImgLoading?.value == false) {
                val drawableSticker = com.android.demoeditor.customView.stickerPackage.DrawableSticker(it)
                stickerFragmentBind?.stickerView?.addSticker(drawableSticker)
            }
        })

        binding.foodRecyclerView.adapter = adapter


        foodViewModel.recyclerViewData.observe(viewLifecycleOwner, Observer {
            it?.let {
                adapter.submitList(it)
            }
        })


        return binding.root
    }


    override fun onDestroy() {
        super.onDestroy()
       stickerBinding  = null
    }

}