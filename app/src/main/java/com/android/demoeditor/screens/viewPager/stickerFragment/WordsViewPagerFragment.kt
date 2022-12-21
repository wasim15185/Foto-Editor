package com.android.demoeditor.screens.viewPager.stickerFragment

import android.app.Application
import com.android.demoeditor.databinding.FragmentStickerBinding
import com.android.demoeditor.databinding.FragmentWordsViewPagerBinding
import com.android.demoeditor.recyclerViews.StickerAdapter
import com.android.demoeditor.recyclerViews.StickerItemListener
import com.android.demoeditor.viewModel.viewPager.stickersFragment.WordsViewPagerViewModel
import com.android.demoeditor.viewModelFactory.viewPager.stickerFragment.WordsViewPagerViewModelFactory
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider

class WordsViewPagerFragment : Fragment() {
    private val TAG = WordsViewPagerFragment::class.java.simpleName
    private lateinit var binding: FragmentWordsViewPagerBinding
    private lateinit var wordsViewModel: WordsViewPagerViewModel

    companion object {
        private var stickerBinding: FragmentStickerBinding? = null

        /**
         * [injectStickerBinding] for inject StickerFragment binding object inside [WordsViewPagerFragment]
         * @param bind [FragmentNewStickerBinding]
         * @return [Fragment]
         */
        fun injectStickerBinding(bind: FragmentStickerBinding): Fragment {
            stickerBinding = bind
            return WordsViewPagerFragment()
        }

    }

    private val stickerFragmentBind by lazy {
        stickerBinding
    }


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentWordsViewPagerBinding.inflate(layoutInflater, container, false)

        val factory =
            WordsViewPagerViewModelFactory(requireContext().applicationContext as Application)
        wordsViewModel = ViewModelProvider(this, factory).get(WordsViewPagerViewModel::class.java)

        val adapter = StickerAdapter(StickerItemListener {
            if (stickerBinding?.stickerViewModel?.isImgLoading?.value == false) {
                val drawableSticker = com.android.demoeditor.customView.stickerPackage.DrawableSticker(it)
                stickerFragmentBind?.stickerView?.addSticker(drawableSticker)
            }
        })

        binding.wordsRecyclerView.adapter = adapter

        wordsViewModel.recyclerViewData.observe(viewLifecycleOwner, Observer {
            it?.let {
                adapter.submitList(it)
            }
        })



        return binding.root
    }
}