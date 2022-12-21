package com.android.demoeditor.screens

import android.annotation.SuppressLint
import android.app.Application
import com.android.demoeditor.R
import com.android.demoeditor.customClass.BitmapHelper
import com.android.demoeditor.customClass.FilterName
import com.android.demoeditor.customView.RulerView
import com.android.demoeditor.data.parcelData.CommonParcelData
import com.android.demoeditor.databinding.FragmentAdjustBinding
import com.android.demoeditor.enums.ActiveNavArgsData
import com.android.demoeditor.viewModel.AdjustScreenViewModel
import com.android.demoeditor.viewModelFactory.AdjustScreenViewModelFactory
import android.graphics.*
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.*
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.findNavController
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.android.demoeditor.utils.resetSliderValAgain
import kotlinx.coroutines.*


class AdjustFragment : Fragment() {


    private val TAG = AdjustFragment::class.java.simpleName
    private lateinit var binding: FragmentAdjustBinding

    private lateinit var adjustViewModel: AdjustScreenViewModel

    private val navArgs by navArgs<AdjustFragmentArgs>()

    @SuppressLint("ClickableViewAccessibility")
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        // This for Inflating menu inside Fragment Toolbar <-- ## this must for toolbar
        setHasOptionsMenu(true)

        // Inflate the layout for this fragment
        binding = FragmentAdjustBinding.inflate(layoutInflater, container, false)

        /**
         * creating [adjustViewModelFactory] object with
         * [AdjustScreenViewModelFactory] class
         */

        val adjustViewModelFactory = AdjustScreenViewModelFactory(
            requireContext().applicationContext as Application,
            navArgs,
            requireActivity(),
        ){
            // redirect to screen
        findNavController().navigate(R.id.adjustEditFragment_to_homeFragment)

        }

        /**
         * initializing [adjustViewModel]
         */

        adjustViewModel = ViewModelProvider(
            this,
            adjustViewModelFactory
        ).get(AdjustScreenViewModel::class.java)

        // Specify the current activity as the lifecycle owner.
        binding.lifecycleOwner = this

        binding.adjustViewModel = adjustViewModel

        /** Setting bitmap in Image-View */
        adjustViewModel.imgBitmap.observe(viewLifecycleOwner, {
            binding.renderScriptImgId.setImageBitmap(it)
        })

        setupToolbar()

        binding.rulerView.setScrollingListener(object : RulerView.ScrollingListener {
            override fun onScrollStart() {
                binding.adjustPercentIndigator.visibility = View.VISIBLE

            }

            override fun onScroll(delta: Float, totalDistance: Float, currentVal: Float) {

                Log.d(TAG, "onScroll: ${adjustViewModel.isImgLoading.value}")
                
                if (adjustViewModel.isImgLoading.value == false) {
                    adjustViewModel.updateImage(currentVal, binding.renderScriptImgId)
                    binding.adjustPercentIndigator.text =
                        adjustViewModel.calculatePercentAge(currentVal)
                }

            }

            override fun onScrollEnd() {

                binding.adjustPercentIndigator.visibility = View.INVISIBLE
            }
        })


        // setonnavigationitemselectedlistener deprecated

        binding.bottomNavigation.setOnItemSelectedListener { item ->

            when (item.itemId) {
                R.id.brightnessId -> {
                    resetTab(FilterName.BRIGHTNESS)
                    true
                }
                R.id.contrastId -> {
                    resetTab(FilterName.CONTRAST)
                    true
                }
                R.id.sharpenId -> {
                    resetTab(FilterName.SHARPEN)
                    true
                }
                R.id.saturationId -> {
                    resetTab(FilterName.SATURATION)
                    true
                }
                R.id.vignetteId -> {
                    resetTab(FilterName.VIGNETTE)
                    true
                }

                else -> true
            }
        }


        return binding.root
    }

    private fun setupToolbar() {
        binding.toolbar.apply {
            setNavigationOnClickListener {
                it.findNavController().navigateUp()
            }

            setOnMenuItemClickListener {
                when (it.itemId) {
                    R.id.tick_id -> {

                        adjustViewModel.changeTheBitmapToMainScreen {

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


    private fun resetTab(filterName: FilterName) {
        adjustViewModel.changeRecentTab(filterName) {
            // binding.seek.resetSliderValAgain(adjustViewModel)
            binding.rulerView.resetSliderValAgain(adjustViewModel)

        }
    }





    /**
     * [navigateToMainScreen] for Navigate to Main-Screen
     */
    private fun navigateToMainScreen(data: CommonParcelData) {

        val action = AdjustFragmentDirections.adjustEditFragmentToMainEditScreenFragment(data)

        findNavController().navigate(action)
    }

    private fun toastFun(value: String) {
        Toast.makeText(context, value, Toast.LENGTH_SHORT).show()
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



