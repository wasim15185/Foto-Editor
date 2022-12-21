package com.android.demoeditor.screens

import android.app.Application
import com.android.demoeditor.R
import com.android.demoeditor.customClass.BitmapHelper
import com.android.demoeditor.data.parcelData.CommonParcelData
import com.android.demoeditor.databinding.FragmentCropBinding
import com.android.demoeditor.enums.ActiveNavArgsData
import com.android.demoeditor.viewModel.CropScreenViewModel
import com.android.demoeditor.viewModelFactory.CropScreenViewModelFactory
import android.graphics.Bitmap
import android.net.Uri
import android.os.Bundle
import android.transition.TransitionInflater
import android.util.Log
import android.view.*
import androidx.fragment.app.Fragment
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.findNavController
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.isseiaoki.simplecropview.CropImageView
import com.isseiaoki.simplecropview.callback.CropCallback


class CropFragment : Fragment() {

    private val TAG = CropFragment::class.java.simpleName
    private lateinit var binding: FragmentCropBinding

    private lateinit var cropViewModel: CropScreenViewModel

    private val navArgs by navArgs<CropFragmentArgs>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Log.d(TAG, "onCreate: is created")

        val animation =
            TransitionInflater.from(requireContext()).inflateTransition(android.R.transition.move)
        sharedElementEnterTransition = animation
        sharedElementReturnTransition = animation
    }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentCropBinding.inflate(layoutInflater, container, false)

        // This for Inflating menu inside Fragment Toolbar
        setHasOptionsMenu(true)

        setupToolbar()


//        /**
//         * This is Actually Postpone the Enter animation Until
//         * PhotoEditorView is not Loaded fully
//         */
//
//        postponeEnterTransition()
//        binding.cropImageView.post{
//            startPostponedEnterTransition()
//        }
//        /**
//         * Don't touch upper 3 lines
//         */


        /**
         *  Creating object of  [CropScreenViewModelFactory]
         *  And passing Application , Img Src
         *  @param Application
         *  @param imgSrc
         */

        val cropViewModelFactory = CropScreenViewModelFactory(
            requireContext().applicationContext as Application,
            navArgs
        ) {
            // redirect to screen
            findNavController().navigate(R.id.cropFragment_to_homeFragment)

        }

        /**
         * Instantiate [CropScreenViewModel] inside [cropViewModel] variable
         */

        cropViewModel =
            ViewModelProvider(this, cropViewModelFactory).get(CropScreenViewModel::class.java)


        // Specify the current activity as the lifecycle owner.
        binding.lifecycleOwner = this

        binding.cropImageView.setInitialFrameScale(.80f)


        /**
         * Setting [cropViewModel] inside Fragment_Crop.xml Data Binding Variable
         */

        binding.cropViewModel = cropViewModel


        /**
         * Setting Bitmap inside CropImageView
         */

        cropViewModel.imgSrcBitmap.observe(viewLifecycleOwner, {

            binding.cropImageView.imageBitmap = it

        })


        /**
         * This is for Default Mode
         */

        convertCropViewAsAspectRatio(CropImageView.CropMode.FREE)


        /**
         * Crop-Navigation 'setOnNavigationItemSelectedListener'
         */

        binding.cropNavigationView.setOnItemSelectedListener {
            when (it.itemId) {
                R.id.free_crop_id -> {
                    convertCropViewAsAspectRatio(CropImageView.CropMode.FREE)
                    true
                }
                R.id.RATIO_4_3 -> {

                    convertCropViewAsAspectRatio(CropImageView.CropMode.RATIO_4_3)

                    true
                }
                R.id.RATIO_3_4 -> {
                    convertCropViewAsAspectRatio(CropImageView.CropMode.RATIO_3_4)
                    true
                }

                R.id.RATIO_16_9 -> {

                    convertCropViewAsAspectRatio(CropImageView.CropMode.RATIO_16_9)
                    true
                }

                else -> false
            }
        }

        return binding.root
    }


    private fun convertCropViewAsAspectRatio(cropMode: CropImageView.CropMode) {
        binding.cropImageView.setCropMode(cropMode, 300)
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

                        saveAsBitmap()

                        true
                    }
                    else -> super.onOptionsItemSelected(it)
                }
            }


        }


    }


    private fun toastFun(str: String) {
        Toast.makeText(context, str, Toast.LENGTH_SHORT).show()
    }


    /**
     * Cropped-Photo into save as Bitmap and cropped Bitmap set inside [mainViewModel]
     */

    private fun saveAsBitmap() {
        binding.cropImageView.crop(binding.cropImageView.sourceUri).execute(object : CropCallback {
            override fun onError(e: Throwable?) {
                Log.i(TAG, e?.message.toString())
                toastFun("Failed to save as Bitmap")
            }

            override fun onSuccess(croppedImg: Bitmap?) {

                val bitmap = binding.cropImageView.croppedBitmap
                val data = getNavArgData(bitmap = bitmap)

                val action = CropFragmentDirections.cropFragmentToMainEditScreenFragment(data)

                findNavController().navigate(action)

            }
        })
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