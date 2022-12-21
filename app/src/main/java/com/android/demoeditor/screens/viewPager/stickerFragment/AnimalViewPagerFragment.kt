package com.android.demoeditor.screens.viewPager.stickerFragment

import android.app.Application
import com.android.demoeditor.R
import com.android.demoeditor.databinding.FragmentAnimalViewPagerBinding
import com.android.demoeditor.databinding.FragmentStickerBinding

import com.android.demoeditor.recyclerViews.StickerAdapter
import com.android.demoeditor.recyclerViews.StickerItemListener
 import com.android.demoeditor.viewModel.viewPager.stickersFragment.AnimalViewPagerViewModel
import com.android.demoeditor.viewModelFactory.viewPager.stickerFragment.AnimalViewPagerViewModelFactory
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider


class AnimalViewPagerFragment : Fragment() {
    private val TAG = AnimalViewPagerFragment::class.java.simpleName

    private lateinit var binding: FragmentAnimalViewPagerBinding

    private lateinit var animalViewModel: AnimalViewPagerViewModel


    companion object {
        private var stickerBinding: FragmentStickerBinding? = null

        /**
         * [injectStickerBinding] for inject StickerFragment binding object inside [AnimalViewPagerFragment]
         * @param bind [FragmentNewStickerBinding]
         * @return [Fragment]
         */
        fun injectStickerBinding(bind: FragmentStickerBinding): Fragment {
            stickerBinding = bind
            return AnimalViewPagerFragment()
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
        binding = FragmentAnimalViewPagerBinding.inflate(layoutInflater, container, false)

        Log.i(TAG, "called")

        val stickerList = resources.obtainTypedArray(R.array.animal_photo)


        val factory = AnimalViewPagerViewModelFactory(
            (requireContext().applicationContext as Application),
            stickerList
        )

        animalViewModel = ViewModelProvider(this, factory).get(AnimalViewPagerViewModel::class.java)


        val adapter = StickerAdapter(StickerItemListener {
            if (stickerBinding?.stickerViewModel?.isImgLoading?.value == false) {
                val drawableSticker = com.android.demoeditor.customView.stickerPackage.DrawableSticker(it)
                stickerFragmentBind?.stickerView?.addSticker(drawableSticker)
            }
        })



        binding.animalRecyclerView.adapter = adapter



//        binding.animalRecyclerView.addItemDecoration(StickerItemDecorator(8))


        animalViewModel.recyclerViewData.observe(viewLifecycleOwner, Observer {
            it?.let {
                adapter.submitList(it)
            }
        })



        return binding.root
    }


}