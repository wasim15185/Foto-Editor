package com.android.demoeditor.screens.viewPager.collageSelectorFragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.android.demoeditor.R
import com.android.demoeditor.databinding.FragmentCollageEditViewPagerBinding
import com.android.demoeditor.databinding.FragmentCollageSelectorBinding
import com.android.demoeditor.recyclerViews.CollageEditNavbarAdapter
import com.android.demoeditor.recyclerViews.CollageEditScreenNavbarListener
import com.android.demoeditor.recyclerViews.CollageIconType.*
import com.android.demoeditor.viewModel.CollageSelectorViewModel
import androidx.databinding.DataBindingUtil


class CollageEditViewPagerFragment : Fragment() {

    private val TAG = this::class.java.simpleName

    private lateinit var binding: FragmentCollageEditViewPagerBinding

    companion object {
        private var staticValueOfCollageViewModel: CollageSelectorViewModel? = null
        private var staticValueOfCollageBinding: FragmentCollageSelectorBinding? = null
        fun injectParentViewModel(
            collageViewModel: CollageSelectorViewModel,
            collageBinding: FragmentCollageSelectorBinding? = null
        ): Fragment {
            staticValueOfCollageViewModel = collageViewModel
            staticValueOfCollageBinding = collageBinding

            return CollageEditViewPagerFragment()
        }
    }

    private val parentBinding by lazy { staticValueOfCollageBinding }

    private val parentViewModel by lazy { staticValueOfCollageViewModel }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = DataBindingUtil.inflate(
            layoutInflater,
            R.layout.fragment_collage_edit_view_pager,
            container,
            false
        )

        configRecyclerView()

        binding.also {
            if (parentViewModel?.isImgLoading?.value == true) {

                it.collageRotateBtn.setOnClickListener {
                    doRotate()
                }
                it.collageMirrorBtn.setOnClickListener {
                    doMirror()
                }

                it.collageFlipBtn.setOnClickListener {
                    doFlipping()
                }
            }
        }

        return binding.root
    }

        // this ReCycler-View is  'Invisible' so If u can delete this Function
    private fun configRecyclerView() {

        val adapter = CollageEditNavbarAdapter(CollageEditScreenNavbarListener {
            if (parentViewModel?.isImgLoading?.value == true) {
                when (it) {
                    ROTATE -> doRotate()
                    MIRROR -> doMirror()
                    FLIP -> doFlipping()

                }

            }
        })

        binding.collageEditNavbarRecyclerView.adapter = adapter

        parentViewModel?.collageEditScreenRecyclerViewData?.observe(viewLifecycleOwner, {
            adapter.submitList(it)
        })


    }

    private fun doRotate() {
        parentBinding?.puzzleView?.rotate(90f)
    }

    private fun doFlipping() {
        parentBinding?.puzzleView?.flipHorizontally()
    }

    private fun doMirror() {
        parentBinding?.puzzleView?.flipVertically()
    }


}