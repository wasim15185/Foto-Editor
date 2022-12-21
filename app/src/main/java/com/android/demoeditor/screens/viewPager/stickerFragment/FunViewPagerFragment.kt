package com.android.demoeditor.screens.viewPager.stickerFragment

import android.app.Application
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.android.demoeditor.databinding.FragmentFunViewPagerBinding
import com.android.demoeditor.databinding.FragmentStickerBinding
import com.android.demoeditor.recyclerViews.StickerAdapter
import com.android.demoeditor.recyclerViews.StickerItemListener
import com.android.demoeditor.viewModel.viewPager.stickersFragment.FunViewPagerViewModel
import com.android.demoeditor.viewModelFactory.viewPager.stickerFragment.FunViewPagerViewModelFactory
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider

class FunViewPagerFragment : Fragment() {

    private lateinit var binding: FragmentFunViewPagerBinding
    private lateinit var viewModel: FunViewPagerViewModel

    companion object {
        private var stickerBinding: FragmentStickerBinding? = null

        /**
         * [injectStickerBinding] for inject StickerFragment binding object inside [FunViewPagerFragment]
         * @param bind [FragmentNewStickerBinding]
         * @return [Fragment]
         */
        fun injectStickerBinding(bind: FragmentStickerBinding): Fragment {
            stickerBinding = bind
            return FunViewPagerFragment()
        }

    }

    private val stickerFragmentBind by lazy {
        stickerBinding
    }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentFunViewPagerBinding.inflate(inflater, container, false)

        val factory =
            FunViewPagerViewModelFactory(requireContext().applicationContext as Application)
        viewModel = ViewModelProvider(this, factory).get(FunViewPagerViewModel::class.java)

        val adapter = StickerAdapter(StickerItemListener {
            if (stickerBinding?.stickerViewModel?.isImgLoading?.value == false) {

                val drawableSticker = com.android.demoeditor.customView.stickerPackage.DrawableSticker(it)
                stickerFragmentBind?.stickerView?.addSticker(drawableSticker)

            }
        })

        binding.funRecyclerView.adapter = adapter

        viewModel.recyclerViewData.observe(viewLifecycleOwner, Observer {
            it?.let {
                adapter.submitList(it)
            }
        })

        return binding.root
    }


}