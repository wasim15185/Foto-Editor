package com.android.demoeditor.screens

import android.annotation.SuppressLint
import android.app.Activity
import android.app.Application
import com.android.demoeditor.R
import com.android.demoeditor.customClass.BitmapHelper
import com.android.demoeditor.customView.stickerPackage.*
import com.android.demoeditor.data.ViewPagerData
import com.android.demoeditor.data.parcelData.CommonParcelData
import com.android.demoeditor.databinding.BottomViewTextStickerBinding
import com.android.demoeditor.databinding.FragmentTextStickerBinding
import android.os.Bundle
import androidx.fragment.app.Fragment
import com.android.demoeditor.enums.ActiveNavArgsData
import com.android.demoeditor.screens.dialog.EditTextDialog
import com.android.demoeditor.screens.viewPager.adapter.StickerViewPagerAdapter
import com.android.demoeditor.viewModel.TextStickerViewModel
import com.android.demoeditor.viewModelFactory.TextStickerViewModelFactory
import android.graphics.Bitmap
import android.graphics.drawable.Drawable
import android.net.Uri
import android.transition.TransitionInflater
import android.view.*
import androidx.core.app.SharedElementCallback
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.findNavController
import androidx.navigation.fragment.FragmentNavigatorExtras
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.google.android.material.tabs.TabLayoutMediator


class TextStickerFragment : Fragment(), EditTextDialog.Callback {

    companion object {
        private const val REQUEST_CODE = 11
        private val TAG = TextStickerFragment::class.java.simpleName
    }


    private lateinit var binding: FragmentTextStickerBinding
    private lateinit var textViewModel: TextStickerViewModel

    private val navArgs by navArgs<TextStickerFragmentArgs>()
//    private val mainViewModel: MainPaintViewModel by activityViewModels()

    private var isLargeLayout: Boolean = false

    private lateinit var includeLayoutBinding: BottomViewTextStickerBinding


    /**
     * This block of code only for animation
     */
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val animation =
            TransitionInflater.from(requireContext()).inflateTransition(android.R.transition.move)
        sharedElementEnterTransition = animation
        sharedElementReturnTransition = animation


    }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // This for Inflating menu inside Fragment Toolbar
        setHasOptionsMenu(true)


        binding = FragmentTextStickerBinding.inflate(layoutInflater, container, false)

        setupToolbar()

        /** this for shared animation delay  */
        postponeEnterTransition()
        binding.mainImg.post {
            startPostponedEnterTransition()

        }
        /** end for shared animation delay */


        includeLayoutBinding = binding.includeLayoutOfBottomViewText

        isLargeLayout = resources.getBoolean(R.bool.large_layout)

        setStickerViewIcon()

        val application = requireContext().applicationContext as Application
        val factory = TextStickerViewModelFactory(
            application,
            navArgs,
            binding
        ){
            // redirect to screen
            findNavController().navigate(R.id.textStickerFragment_to_homeFragment)

        }

        textViewModel = ViewModelProvider(this, factory).get(TextStickerViewModel::class.java)
        // Specify the current activity as the lifecycle owner.
        binding.lifecycleOwner = this
        binding.textViewModel = textViewModel


        /**
         * disable [ViewPager2] swift left right gesture
         */
        includeLayoutBinding.textToolViewPager.isUserInputEnabled = false


        /**
         * setting bitmap in ImageView
         */


        textViewModel.imgSrc.observe(viewLifecycleOwner, {


            binding.mainImg.setImageBitmap(it)

            binding.textStickerView.setMainImage(binding.mainImg)
            binding.textStickerView.setMainBitmap(it)


        })


        /** This callback is called when "Shared-Element" is END  */
        setEnterSharedElementCallback(
            object : SharedElementCallback() {
                override fun onSharedElementEnd(
                    sharedElementNames: List<String?>?,
                    sharedElements: List<View?>?,
                    sharedElementSnapshots: List<View?>?
                ) {
                    // transition has ended

                    binding.textStickerView.addSticker(textViewModel.createNewSticker("Double Tap To Edit"))


                }
            }
        )


        /**
         * setting [StickerViewPagerAdapter] adapter inside [binding.textToolViewPager.adapter]
         */
        textViewModel.viewPagerData.observe(viewLifecycleOwner, Observer {
            includeLayoutBinding.textToolViewPager.adapter =
                StickerViewPagerAdapter(childFragmentManager, lifecycle, it)
            configTabLayout(it)

        })


        /**
         * By Default when layout first appeare then by default one sticker view create automatically
         */

//        binding.textStickerView.post {
//            binding.textStickerView.addSticker(textViewModel.createNewSticker("Double Tap To Edit"))
//
//        }


        includeLayoutBinding.addStickerBtn.setOnClickListener {
            showEditTextDialog(
                isTextEdit = false
            )
        }



        binding.textStickerView.onStickerOperationListener =
            object : com.android.demoeditor.customView.stickerPackage.StickerView.OnStickerOperationListener {
                override fun onStickerAdded(sticker: com.android.demoeditor.customView.stickerPackage.Sticker) {
                    textViewModel.updateCurrentSticker(sticker)
                }

                override fun onStickerClicked(sticker: com.android.demoeditor.customView.stickerPackage.Sticker) {
                    textViewModel.updateCurrentSticker(sticker)
                }

                override fun onStickerDeleted(sticker: com.android.demoeditor.customView.stickerPackage.Sticker) {

                    textViewModel.deleteOneStickerFromList(sticker as com.android.demoeditor.customView.stickerPackage.TextSticker)

                }

                override fun onStickerDragFinished(sticker: com.android.demoeditor.customView.stickerPackage.Sticker) {}

                override fun onStickerTouchedDown(sticker: com.android.demoeditor.customView.stickerPackage.Sticker) {}

                override fun onStickerZoomFinished(sticker: com.android.demoeditor.customView.stickerPackage.Sticker) {}

                override fun onStickerFlipped(sticker: com.android.demoeditor.customView.stickerPackage.Sticker) {}

                override fun onStickerDoubleTapped(sticker: com.android.demoeditor.customView.stickerPackage.Sticker) {
                    val textSticker = sticker as com.android.demoeditor.customView.stickerPackage.TextSticker

                    EditTextDialog.show(
                        this@TextStickerFragment,
                        REQUEST_CODE,
                        textSticker.text!!,
                        isTextIsAlReadyEdited = true
                    )


                }
            }







        return binding.root
    }


    private fun configTabLayout(viewpagerData: List<ViewPagerData>) {
        TabLayoutMediator(
            includeLayoutBinding.textStickerTabLayout,
            includeLayoutBinding.textToolViewPager
        ) { tab, position ->
            tab.text = viewpagerData[position].name
        }.attach()
    }


    private fun setStickerViewIcon() {

        val editIcon = com.android.demoeditor.customView.stickerPackage.BitmapStickerIcon(
            getDrawable(R.drawable.sticker_view_edit_icon_12),
            com.android.demoeditor.customView.stickerPackage.BitmapStickerIcon.LEFT_TOP
        )
        val deleteIcon = com.android.demoeditor.customView.stickerPackage.BitmapStickerIcon(
            getDrawable(R.drawable.sticker_view_close_icon_12),
            com.android.demoeditor.customView.stickerPackage.BitmapStickerIcon.RIGHT_TOP
        )
        val resizeIcon = com.android.demoeditor.customView.stickerPackage.BitmapStickerIcon(
            getDrawable(R.drawable.sticker_view_resize_12),
            BitmapStickerIcon.RIGHT_BOTOM
        )


        editIcon.iconEvent = EditIconEvent() {
            if (it != null) {
                val textSticker = it.currentSticker!! as com.android.demoeditor.customView.stickerPackage.TextSticker
                EditTextDialog.show(
                    this,
                    REQUEST_CODE,
                    textSticker.text!!,
                    isTextIsAlReadyEdited = true
                )
            }


        }
        deleteIcon.iconEvent = com.android.demoeditor.customView.stickerPackage.DeleteIconEvent()
        resizeIcon.iconEvent = com.android.demoeditor.customView.stickerPackage.ZoomIconEvent()

        binding.textStickerView.icons = listOf(editIcon, deleteIcon, resizeIcon)


    }


    private fun navigateToScreenWithExtra(data: CommonParcelData) {

        val imgView = binding.mainImg
        val imgTransitionUniqueName = resources.getString(R.string.img_transition_unique_name)

        val extra = FragmentNavigatorExtras(imgView to imgTransitionUniqueName)

        val action = TextStickerFragmentDirections.textStickerFragmentToMainEditScreenFragment(data)
        findNavController().navigate(action, extra)
    }


    @SuppressLint("UseCompatLoadingForDrawables")
    private fun getDrawable(id: Int): Drawable {
        return requireContext().getDrawable(id)!!
    }


    private fun showEditTextDialog(text: String = "", isTextEdit: Boolean) {

        EditTextDialog.show(this, REQUEST_CODE, text, isTextEdit)


    }


    override fun onEditTextDialogResult(
        requestCode: Int,
        resultCode: Int,
        text: String,
        isTextIsAlreadyEdited: Boolean
    ) {
        if (requestCode != REQUEST_CODE || resultCode != Activity.RESULT_OK) return


        if (!isTextIsAlreadyEdited) {
            binding.textStickerView.addSticker(textViewModel.createNewSticker(text))
        } else {

            val textSticker = textViewModel.currentSticker?.value as com.android.demoeditor.customView.stickerPackage.TextSticker
            textSticker.text = text
            textSticker.resizeText()

            textViewModel.updateCurrentSticker(textSticker)
            textViewModel.updateOneStickerInList(textSticker)

        }

    }


    /**
     * Inflating menu inside Fragment Toolbar
     */


    private fun setupToolbar() {

        binding.toolbar.apply {
            setNavigationOnClickListener {
                it.findNavController().navigateUp()
            }

            setOnMenuItemClickListener {
                when (it.itemId) {
                    R.id.tick_id -> {

                        val bitmap = binding.textStickerView.createBitmap()
                        val data = getNavArgData(bitmap = bitmap)
                        navigateToScreenWithExtra(data)

                        true
                    }
                    else -> super.onOptionsItemSelected(it)
                }
            }
        }

    }



    /**
     * [getNavArgData] for get the nav arg data
     */
    private fun getNavArgData(uri: Uri? = null, bitmap: Bitmap? = null): CommonParcelData {
        bitmap?.also {
            BitmapHelper.setBitmap(it, true)
        }
        return CommonParcelData(
            uri = uri,
            availableData = if (uri != null) ActiveNavArgsData.URI else ActiveNavArgsData.BITMAP
        )
    }

}



