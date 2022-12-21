package com.android.demoeditor.screens.viewPager.stickerFragment

import android.annotation.SuppressLint
import android.app.Application
import com.android.demoeditor.R
import com.android.demoeditor.customView.stickerPackage.TextSticker
import com.android.demoeditor.databinding.FragmentEmojiViewPagerBinding
import com.android.demoeditor.databinding.FragmentStickerBinding
import com.android.demoeditor.recyclerViews.EmojiAdapter
import com.android.demoeditor.recyclerViews.EmojiItemListener
import com.android.demoeditor.viewModel.viewPager.stickersFragment.EmojiViewPagerViewModel
import com.android.demoeditor.viewModelFactory.viewPager.stickerFragment.EmojiViewPagerViewModelFactory
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider


class EmojiViewPagerFragment() : Fragment() {


    companion object {
        private val TAG = EmojiViewPagerFragment::class.java.simpleName

        private var stickerBinding: FragmentStickerBinding? = null

        fun injectStickerBinding(bind: FragmentStickerBinding): Fragment {
            stickerBinding = bind
            return EmojiViewPagerFragment()
        }

    }


    private lateinit var binding: FragmentEmojiViewPagerBinding

    private lateinit var emojiViewModel: EmojiViewPagerViewModel


    private val stickerFragmentBind by lazy {
        stickerBinding
    }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentEmojiViewPagerBinding.inflate(inflater, container, false)

        /**
         * [factory] is View Model factory
         */
        val factory =
            EmojiViewPagerViewModelFactory(requireContext().applicationContext as Application)

        emojiViewModel = ViewModelProvider(this, factory).get(EmojiViewPagerViewModel::class.java)


        /**
         * EmojiAdapter is Recycler View Adapter
         */
        val adapter = EmojiAdapter(EmojiItemListener {
            if (stickerBinding?.stickerViewModel?.isImgLoading?.value == false) {
                /**
                 * [TextSticker] creating object
                 */
                val textSticker = com.android.demoeditor.customView.stickerPackage.TextSticker(
                    requireContext()
                ).apply {
                    drawable = getDrawable(R.drawable.sticker_emoji_transparent_background)
                    text = it


                    setTextAlign(emojiViewModel.getStickerRandomPosition())
                    resizeText()
                }

                /** setting [textSticker] inside [StickerView] */
                stickerFragmentBind?.stickerView?.addSticker(textSticker)

            }

        })

        /**
         * Setting adapter inside Recycler-View Adapter
         */
        binding.emojiRecyclerView.adapter = adapter

        emojiViewModel.emojiRecyclerViewData.observe(viewLifecycleOwner,   {
            it?.let {
                adapter.submitList(it)
            }
        })


        return binding.root
    }

    @SuppressLint("UseCompatLoadingForDrawables")
    private fun getDrawable(id: Int): Drawable {
        return requireContext().getDrawable(id)!!
    }


}