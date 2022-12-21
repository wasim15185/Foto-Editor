package com.android.demoeditor.screens

import android.app.Application
import android.os.Bundle
import androidx.fragment.app.Fragment
import com.android.demoeditor.R
import com.android.demoeditor.customClass.BitmapHelper
import com.android.demoeditor.customView.rotatedAndCropLib.view.OverlayView.FREESTYLE_CROP_MODE_ENABLE
import com.android.demoeditor.data.parcelData.CommonParcelData

import com.android.demoeditor.databinding.FragmentRotationBinding
import com.android.demoeditor.enums.ActiveNavArgsData
import com.android.demoeditor.viewModel.RotationViewModel
import com.android.demoeditor.viewModelFactory.RotationViewModelFactory
import android.graphics.Bitmap
import android.net.Uri
import android.view.*
import androidx.core.content.ContextCompat
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.findNavController
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs

import java.lang.Exception
import java.util.*


class RotationFragment : Fragment() {

    private val ROTATE_WIDGET_SENSITIVITY_COEFFICIENT = 42

    private lateinit var binding: FragmentRotationBinding
    private lateinit var viewModel: RotationViewModel


    private val navArgs by navArgs<RotationFragmentArgs>()


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        // Inflate the layout for this fragment
        binding = FragmentRotationBinding.inflate(layoutInflater, container, false)

        setupToolbar()


        val application = requireContext().applicationContext as Application
        val factory = RotationViewModelFactory(application, navArgs){
            // redirect to screen
            findNavController().navigate(R.id.rotationFragment_to_homeFragment)
        }
        viewModel = ViewModelProvider(this, factory).get(RotationViewModel::class.java)

        // Specify the current activity as the lifecycle owner.
        binding.lifecycleOwner = this

        binding.viewModel = viewModel


        /**
         * when layout is first appear then by default set text [0f] inside textview
         */
        setAngelText(0f)



        binding.btn90Degree.setOnClickListener {
            rotateByAngle(90)

        }


        viewModel.imgSrc.observe(viewLifecycleOwner, Observer {
            binding.ucrop.cropImageView.setImageBitmap(it)
        })


        binding.ucrop.overlayView.freestyleCropMode = FREESTYLE_CROP_MODE_ENABLE





        binding.rulerView.apply {
            setMiddleLineColor(ContextCompat.getColor(context, android.R.color.transparent))

        }



        binding.ucrop.cropImageView.setTransformImageListener(object :
            com.android.demoeditor.customView.rotatedAndCropLib.view.TransformImageView.TransformImageListener {
            override fun onLoadComplete() {
            }

            override fun onLoadFailure(e: Exception) {
            }

            override fun onRotate(currentAngle: Float) {
                setAngelText(currentAngle)
            }

            override fun onScale(currentScale: Float) {
            }
        })






        binding.rulerView.setScrollingListener(object :
            com.android.demoeditor.customView.rotatedAndCropLib.view.widget.HorizontalProgressWheelView.ScrollingListener {

            override fun onScrollStart() {
                binding.ucrop.cropImageView.cancelAllAnimations();
            }

            override fun onScroll(delta: Float, totalDistance: Float) {
                binding.ucrop.cropImageView.postRotate(delta / ROTATE_WIDGET_SENSITIVITY_COEFFICIENT);
            }

            override fun onScrollEnd() {
                binding.ucrop.cropImageView.setImageToWrapCropBounds();
            }
        })




        return binding.root
    }


    /**
     * Inflating menu inside Fragment Toolbar
     */

    private fun setupToolbar() {
        // This for Inflating option-menu inside Fragment Toolbar
        setHasOptionsMenu(true)

        binding.toolbar.apply {
            setNavigationOnClickListener {
                it.findNavController().navigateUp()
            }

            setOnMenuItemClickListener {
                when (it.itemId) {
                    R.id.tick_id -> {

                        val bitmap = binding.ucrop.cropImageView.croppedBitmap

                        bitmap?.apply {
                            val data = getNavArgData(bitmap = this)
                            navigateToScreen(data)
                        }

                        true
                    }
                    else -> super.onOptionsItemSelected(it)
                }
            }

        }

    }


    /**
     * convert text with ° means '12' -> '12°'
     */
    private fun setAngelText(angel: Float) {
        binding.rotatedTextShower.text = String.format(Locale.getDefault(), "%.1f°", angel)
    }

    /**
     * rotated ucropview by angle
     */
    private fun rotateByAngle(angle: Int) {
        binding.ucrop.cropImageView.apply {
            postRotate(angle.toFloat());
            setImageToWrapCropBounds();
        }
    }


    private fun navigateToScreen(data: CommonParcelData) {
        val action = RotationFragmentDirections.rotationFragmentToMainEditScreenFragment(data)
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