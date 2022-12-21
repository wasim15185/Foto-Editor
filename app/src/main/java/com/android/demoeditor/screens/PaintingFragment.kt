package com.android.demoeditor.screens

import android.app.Application
import com.android.demoeditor.R
import com.android.demoeditor.customClass.BitmapHelper
import com.android.demoeditor.customClass.CustomPhotoEditor
import com.android.demoeditor.data.parcelData.CommonParcelData
import com.android.demoeditor.databinding.FragmentPaintingBinding
import com.android.demoeditor.enums.ActiveNavArgsData
import com.android.demoeditor.recyclerViews.ColorPickerAdapter
import com.android.demoeditor.recyclerViews.ColorPickerItemListener
import android.os.Bundle
import androidx.fragment.app.Fragment
import com.android.demoeditor.viewModel.PaintingScreenViewModel
import com.android.demoeditor.viewModelFactory.PaintingScreenViewModelFactory
import android.graphics.Bitmap
import android.net.Uri
import android.view.*
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.findNavController
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.transition.TransitionInflater
import com.google.android.material.slider.Slider
import com.google.android.material.tabs.TabLayout


class PaintingFragment : Fragment() {

    private val TAG = PaintingFragment::class.java.simpleName

    private lateinit var binding: FragmentPaintingBinding
    private lateinit var paintViewModel: PaintingScreenViewModel
    private lateinit var colorPickerRecyclerAdapter: ColorPickerAdapter

    private val navArgs by navArgs<PaintingFragmentArgs>()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        /**
         * This is only for Animation which is set
         * inside [sharedElementEnterTransition] and [sharedElementReturnTransition]
         */
        val animation =
            TransitionInflater.from(context).inflateTransition(android.R.transition.move)
        sharedElementEnterTransition = animation
        sharedElementReturnTransition = animation

    }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        // Inflate the layout for this fragment
        binding =
            DataBindingUtil.inflate(layoutInflater, R.layout.fragment_painting, container, false)

        // This for Inflating menu inside Fragment Toolbar
        setHasOptionsMenu(true)

        setUpToolBar()

        /**
         * This is Actually Postpone the Enter animation Until
         * PhotoEditorView is not Loaded fully
         **/

        postponeEnterTransition()
        binding.paintView.source.post {
            startPostponedEnterTransition()
        }
        /**
         * Don't touch upper 3 lines
         */


        val photoEditorBuilder = CustomPhotoEditor(requireContext(), binding.paintView)

        val factory =
            PaintingScreenViewModelFactory(
                requireContext().applicationContext as Application,
                photoEditorBuilder,
                navArgs
            ) {
                // redirect to screen
                findNavController().navigate(R.id.paintingFragment_to_homeFragment)

            }
        paintViewModel = ViewModelProvider(this, factory).get(PaintingScreenViewModel::class.java)

        // Specify the current activity as the lifecycle owner.
        binding.lifecycleOwner = this

        binding.paintViewModel = paintViewModel


        paintViewModel.imgSrcBitmap.observe(viewLifecycleOwner, Observer {
            binding.paintView.source.setImageBitmap(it)
        })


        paintViewModel.defaultDrawingMode()

        binding.paintTabLayout.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
            override fun onTabSelected(tab: TabLayout.Tab?) {

                paintViewModel.changeRecentTab(tab?.text.toString())
                setVisibility()

            }

            override fun onTabUnselected(tab: TabLayout.Tab?) {
                // TODO("Not yet implemented")
            }

            override fun onTabReselected(tab: TabLayout.Tab?) {
                // TODO("Not yet implemented")
            }
        })


        brushToolLayout()

        eraserToolLayout()


        return binding.root
    }


    /**
     * Inflating menu inside Fragment Toolbar
     */

    private fun setUpToolBar() {

        binding.toolbar.apply {
            setNavigationOnClickListener {
                it.findNavController().navigateUp()
            }

            setOnMenuItemClickListener {
                when (it.itemId) {
                    R.id.tick_id -> {

                        paintViewModel.savePhotoAsBitmap {
                            val data = getNavArgData(bitmap = it)
                            navigateToMainScreen(data)
                        }

                        true
                    }
                    else -> super.onOptionsItemSelected(it)
                }
            }

        }
    }


    private fun setVisibility() {
        if (paintViewModel.isBrushLayoutVisible()) {
            binding.brushIncludeId.brushContainerLayout.visibility = View.VISIBLE
            binding.eraserIncludeId.eraserContainerLayout.visibility = View.GONE
        } else {
            binding.eraserIncludeId.eraserContainerLayout.visibility = View.VISIBLE
            binding.brushIncludeId.brushContainerLayout.visibility = View.GONE

        }

    }

    private fun brushToolLayout() {

        colorPickerRecyclerAdapter = ColorPickerAdapter()
        val colorPickerItemListener = ColorPickerItemListener { colorValue, position ->
            paintViewModel.setBrushColor(colorValue)
            colorPickerRecyclerAdapter.setSingleSelection(position)
        }

        colorPickerRecyclerAdapter.clickListener = colorPickerItemListener

        binding.brushIncludeId.recyclerView.adapter = colorPickerRecyclerAdapter

        paintViewModel.recyclerViewsData.observe(viewLifecycleOwner, Observer {
            colorPickerRecyclerAdapter.submitList(it)
        })

        /**
         * Brush-Seekbar Touch-Listener
         */

        binding.brushIncludeId.brushSizeSeekbar.addOnSliderTouchListener(object :
            Slider.OnSliderTouchListener {
            override fun onStartTrackingTouch(slider: Slider) {
                showSliderTextIndigator()
            }

            override fun onStopTrackingTouch(slider: Slider) {
                // Responds to when slider's touch event is being stopped
                hideSliderTextIndigator()
            }
        })


        /**
         * Brush-Seekbar OnChange-Listener
         */


        binding.brushIncludeId.brushSizeSeekbar.addOnChangeListener { slider, value, fromUser ->
            paintViewModel.setBrushSize(value)
        }

    }

    private fun showSliderTextIndigator() {
        binding.paintBrushIndigator.visibility = View.VISIBLE
    }

    private fun hideSliderTextIndigator() {
        binding.paintBrushIndigator.visibility = View.GONE

    }


    private fun eraserToolLayout() {

        /**
         * Eraser-Seekbar Touch-Listener
         */

        binding.eraserIncludeId.eraserSizeSeekbar.addOnSliderTouchListener(object :
            Slider.OnSliderTouchListener {
            override fun onStartTrackingTouch(slider: Slider) {
                showSliderTextIndigator()
            }

            override fun onStopTrackingTouch(slider: Slider) {
                // Responds to when slider's touch event is being stopped
                hideSliderTextIndigator()
            }
        })

        /**
         * Eraser-Seekbar OnChange-Listener
         */

        binding.eraserIncludeId.eraserSizeSeekbar.addOnChangeListener { slider, value, fromUser ->
            paintViewModel.setEraserSize(value)
        }

    }


    private fun toastFun(value: String) {
        Toast.makeText(context, value, Toast.LENGTH_SHORT).show()
    }


    /**
     * Inflating menu inside Fragment Toolbar
     */
//    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
//        inflater.inflate(R.menu.paint_menu, menu)
//
//    }
//
//    override fun onOptionsItemSelected(item: MenuItem): Boolean {
//        return when (item.itemId) {
//            R.id.paint_tick_id -> {
//
//                paintViewModel.savePhotoAsBitmap {
//                    val data = getNavArgData(bitmap = it)
//                    navigateToMainScreen(data)
//                }
//
//                return true
//            }
//            else -> super.onOptionsItemSelected(item)
//        }
//    }


    private fun navigateToMainScreen(data: CommonParcelData) {
        val action = PaintingFragmentDirections.paintingFragmentToMainEditScreenFragment(data)

        findNavController().navigate(action)
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