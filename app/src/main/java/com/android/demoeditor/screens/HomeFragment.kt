package com.android.demoeditor.screens

import android.Manifest
import android.app.Activity.RESULT_OK
import android.app.Application
import android.content.Intent
import com.android.demoeditor.R
import com.android.demoeditor.data.parcelData.CollageSelectorData
import com.android.demoeditor.data.parcelData.CommonParcelData
import com.android.demoeditor.databinding.FragmentHomeBinding
import com.android.demoeditor.enums.ActiveNavArgsData
import com.android.demoeditor.screens.viewPager.adapter.HomeViewPagerAdapter
import com.android.demoeditor.viewModel.HomeViewModel
import com.android.demoeditor.viewModelFactory.HomeViewModelFactory
import android.net.Uri
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.navigation.fragment.findNavController
import com.vmadalin.easypermissions.EasyPermissions
import com.vmadalin.easypermissions.dialogs.SettingsDialog
//import com.zhihu.matisse.Matisse
import gun0912.tedimagepicker.builder.TedImagePicker
import gun0912.tedimagepicker.builder.type.MediaType


class HomeFragment : Fragment(), EasyPermissions.PermissionCallbacks {
    private lateinit var binding: FragmentHomeBinding

    private val TAG = HomeFragment::class.java.simpleName

    companion object {
        // All request code requestCode should be >= 0
        const val WRITE_STORAGE_PERMISSION_REQUEST_CODE_AND_READ_STORAGE_PERMISSION_REQUEST_CODE =
            201

        const val REQUEST_CODE_FOR_MATISSE_PHOTO_PICKER = 51

    }

    lateinit var mSelected: List<Uri>

    lateinit var toggle: ActionBarDrawerToggle

    private lateinit var viewPagerAdapter: HomeViewPagerAdapter

    private lateinit var viewModel:HomeViewModel


    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
//        binding.mainNavBar.onSaveInstanceState(outState);
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentHomeBinding.inflate(layoutInflater, container, false)

        val application = requireContext().applicationContext as Application
        val factory = HomeViewModelFactory(application)
       viewModel= factory.create(HomeViewModel::class.java)

//        binding.mainNavBar.initWithSaveInstanceState(savedInstanceState); // this important for navigation bar

        viewPagerAdapter = HomeViewPagerAdapter(this ,viewModel )
        binding.mainViewPager.adapter = viewPagerAdapter

        /**
         * disable [ViewPager2] swift left right gesture
         */
        binding.mainViewPager.isUserInputEnabled = false

        setupBottomNavigationBar()
        setupSliderDrawerNavigationView()

        setupBottomNavigationBar2()


//        binding.imgSelectorBtn.setOnClickListener {
//            singlePhotoSelector()
//        }
//
//
//        binding.Colleague.setOnClickListener {
//            colleaguePhotosSelector()
//        }
//
//
//
//
//
//
//        binding.customSelector.setOnClickListener {
//            navigateToScreen(R.id.homeFragment_to_imgSelectorFragment)
//        }


        return binding.root
    }


    private fun setupViewPager() {


    }


    private fun colleaguePhotosSelector() {
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
                    val action =
                        HomeFragmentDirections.homeFragmentToCollageSelectorFragment(
                            CollageSelectorData(it)
                        )
                    findNavController().navigate(action)

                }

        } else {
            requestReadAndWritePerMisson()
        }

    }


    private fun singlePhotoSelector() {
        if (hasStorageWritePermissions()) {
            TedImagePicker
                .with(requireContext())
                .mediaType(MediaType.IMAGE)
                .dropDownAlbum()
                .zoomIndicator(false)
                .start {

                    val data = CommonParcelData(
                        uri = it,
                        availableData = ActiveNavArgsData.URI,

                        )
                    navigateToNavArgument(
                        R.id.homeFragment_to_mainEditScreenFragment,
                        data
                    )

                }

        } else {
            requestReadAndWritePerMisson()
        }
    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)


        if (requestCode == REQUEST_CODE_FOR_MATISSE_PHOTO_PICKER && resultCode == RESULT_OK) {



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

    /**
     * Navigate Fun
     */
    private fun navigateToScreen(id: Int) {
        findNavController().navigate(id)
    }

//    private fun setImageSource(bitmap: Bitmap){
//        mainPaintViewModel.setImgSrc(bitmap)
//    }


    private fun setupBottomNavigationBar() {
/**
        binding.mainNavBar.apply {
            addSpaceItem(SpaceItem(null, R.drawable.ic_home_24))
            addSpaceItem(SpaceItem(null, R.drawable.ic_recent_24))
        }




        binding.mainNavBar.setSpaceOnClickListener(
            object : SpaceOnClickListener {
                override fun onCentreButtonClick() {
                        singlePhotoSelector()
                }

                override fun onItemClick(itemIndex: Int, itemName: String?) {

                    binding.mainViewPager.currentItem = itemIndex

                }

                override fun onItemReselected(itemIndex: Int, itemName: String?) {


                }
            }
        )
*/

    }




    private fun setupBottomNavigationBar2() {

        binding.bottomNavigationView.setOnItemSelectedListener{
            when(it.itemId) {
                R.id.home_start_icon -> {
                    binding.mainViewPager.currentItem=0
                    true
                 }
                R.id.home_recent -> {
                    binding.mainViewPager.currentItem = 1
                    true
                }
                else -> true
            }
        }


    }







    private fun setupSliderDrawerNavigationView() {

        toggle = ActionBarDrawerToggle(
            requireActivity(),
            binding.mainDrawerLayout,
            R.string.open,
            R.string.close
        )
        binding.mainDrawerLayout.addDrawerListener(toggle)
        toggle.syncState()

        binding.toolbar.setNavigationOnClickListener {
            binding.mainDrawerLayout.open()
        }
        binding.slideNavView.setNavigationItemSelectedListener { menuItem ->
            when (menuItem.itemId) {
//                R.id.setting -> {
//                    toast(" setting clicked ")
//                }
//
//                R.id.rate_us -> {
//                    toast(" rate us clicked ")
//
//                }

                R.id.about -> {
                    findNavController().navigate(R.id.homeFragment_to_aboutFragment)

                }

                R.id.developer_contact -> {
                    findNavController().navigate(R.id.action_homeFragment_to_developerDetailsFragment)


                }


            }


            // Handle menu item selected
            menuItem.isChecked = true



            binding.mainDrawerLayout.close()
            true
        }


    }


    private fun navigateToNavArgument(id: Int, data: CommonParcelData) {

        val action =
            HomeFragmentDirections.homeFragmentToMainEditScreenFragment(data)


        findNavController().navigate(action)
    }


    private fun toast(string: String = "clicked") {
        Toast.makeText(context, string, Toast.LENGTH_SHORT).show()
    }


}