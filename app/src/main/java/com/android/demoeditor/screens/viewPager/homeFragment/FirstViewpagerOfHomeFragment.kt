package com.android.demoeditor.screens.viewPager.homeFragment

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.android.demoeditor.R
import com.android.demoeditor.databinding.FragmentFirstViewpagerOfHomeBinding
import com.android.demoeditor.navigationContainer.HomeFragmentNavigation
import com.android.demoeditor.viewModel.HomeViewModel
import com.android.demoeditor.navigationContainer.HomeFragmentNavigation.TedImageSelector
import android.net.Uri
import androidx.databinding.DataBindingUtil
import androidx.navigation.fragment.findNavController
import com.vmadalin.easypermissions.EasyPermissions
import com.vmadalin.easypermissions.dialogs.SettingsDialog
import gun0912.tedimagepicker.builder.TedImagePicker
import gun0912.tedimagepicker.builder.type.MediaType


class FirstViewpagerOfHomeFragment : Fragment(), EasyPermissions.PermissionCallbacks {

    companion object {
        // All request code requestCode should be >= 0
        private const val WRITE_STORAGE_PERMISSION_REQUEST_CODE_AND_READ_STORAGE_PERMISSION_REQUEST_CODE =
            201

        private const val REQUEST_CODE_FOR_MATISSE_PHOTO_PICKER = 51

        private var parentStaticViewModel: HomeViewModel? = null
        fun injectValue(viewModel: HomeViewModel): Fragment {
            parentStaticViewModel = viewModel
            return FirstViewpagerOfHomeFragment()
        }

    }

    private val parentViewModel by lazy {
        parentStaticViewModel
    }

    private lateinit var binding: FragmentFirstViewpagerOfHomeBinding


    private lateinit var navigate: HomeFragmentNavigation


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = DataBindingUtil.inflate(
            layoutInflater,
            R.layout.fragment_first_viewpager_of_home,
            container,
            false
        )

        binding.lifecycleOwner = this

        navigate = HomeFragmentNavigation(findNavController(), getImgPickerObj())

        binding.homeGridListener = object : HomeGridItemListener {
            override fun singlePhoto() {
                navigate.singlePhotoSelector()
            }

            override fun collagePhoto() {
                navigate.colleaguePhotosSelector()
            }

            override fun adjust() {
                navigate.gotoAdjustFragment()
            }

            override fun crop() {
                navigate.gotoCropFragment()

            }

            override fun filter() {

                navigate.gotoFilterFragment()
            }

            override fun sticker() {
                navigate.gotoStickerFragment()
            }

            override fun rotate() {

                navigate.gotoRotatedFragment()
            }
        }




        return binding.root
    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == REQUEST_CODE_FOR_MATISSE_PHOTO_PICKER && resultCode == Activity.RESULT_OK) {
        }
    }


    /**
     * Permission Code START point
     */
    private fun hasStorageWritePermissions(): Boolean {
        return EasyPermissions.hasPermissions(
            context,
            Manifest.permission.WRITE_EXTERNAL_STORAGE,
            Manifest.permission.READ_EXTERNAL_STORAGE
        )
    }


    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        EasyPermissions.onRequestPermissionsResult(requestCode, permissions, grantResults, this)
    }

    private fun requestReadAndWritePerMisson() {
        val deniedPermissionString = "Turn on storage permission to save the photo"
        EasyPermissions.requestPermissions(
            this,
            deniedPermissionString,
            WRITE_STORAGE_PERMISSION_REQUEST_CODE_AND_READ_STORAGE_PERMISSION_REQUEST_CODE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE
        )

    }

    override fun onPermissionsDenied(requestCode: Int, perms: List<String>) {
        if (EasyPermissions.somePermissionPermanentlyDenied(this, perms)) {
            SettingsDialog.Builder(requireActivity()).build().show()
        } else {
            requestReadAndWritePerMisson()
        }
    }

    override fun onPermissionsGranted(requestCode: Int, perms: List<String>) {

    }

    /**
     * End of permission code
     */


    private fun getImgPickerObj() = object : TedImageSelector {
        override fun singleImagePicker(result: (uri: Uri) -> Unit) {
            if (hasStorageWritePermissions()) {
                TedImagePicker
                    .with(requireContext())
                    .mediaType(MediaType.IMAGE)
                    .dropDownAlbum()
                    .zoomIndicator(false)
                    .start {
                        result(it)
                    }

            } else {
                requestReadAndWritePerMisson()
            }

        }

        override fun multiImagePicker(result: (uriList: List<Uri>) -> Unit) {
            if (hasStorageWritePermissions()) {

                val maxLimit = 9
                val minLimit = 1

                TedImagePicker
                    .with(requireContext())
                    .mediaType(MediaType.IMAGE)
                    .dropDownAlbum()
                    .max(maxLimit, "You can't select up-to $maxLimit ")
                    .min(minLimit, "You must be select at least $minLimit")
                    .zoomIndicator(false)
                    .startMultiImage {
                        result(it)
                    }

            } else {
                requestReadAndWritePerMisson()
            }

        }
    }


}


/**
 * [HomeGridItemListener] for Grid-Listener
 */
interface HomeGridItemListener {
    fun singlePhoto();
    fun collagePhoto();
    fun adjust()
    fun crop()
    fun filter()
    fun sticker()
    fun rotate()

}