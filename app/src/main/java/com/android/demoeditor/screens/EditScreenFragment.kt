package com.android.demoeditor.screens


import android.app.Application
import com.android.demoeditor.R
import com.android.demoeditor.customClass.BitmapHelper
import com.android.demoeditor.data.parcelData.CommonParcelData
import com.android.demoeditor.databinding.FragmentEditScreenBinding
import com.android.demoeditor.enums.ActiveNavArgsData
import com.android.demoeditor.enums.MainScreenItems.*
import com.android.demoeditor.screens.dialog.LoadingDialog2
import com.android.demoeditor.viewModel.EditViewModel

import com.android.demoeditor.viewModelFactory.EditViewModelFactory
import android.graphics.Bitmap
import android.graphics.drawable.BitmapDrawable
import android.net.Uri
import android.os.Bundle
import android.view.*
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.findNavController
import androidx.navigation.fragment.FragmentNavigator
import androidx.navigation.fragment.FragmentNavigatorExtras
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs


class EditScreenFragment : Fragment() {


    private val TAG = EditScreenFragment::class.java.simpleName

    private lateinit var binding: FragmentEditScreenBinding

    private lateinit var viewModel: EditViewModel

    private val navArgs by navArgs<EditScreenFragmentArgs>()


    private val dialog by lazy { LoadingDialog2(requireContext()) }

    companion object {
        // All request code requestCode should be >= 0
        const val WRITE_STORAGE_PERSMISSION_REQUEST_CODE = 101
    }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding =
            DataBindingUtil.inflate(layoutInflater, R.layout.fragment_edit_screen, container, false)

        // This for Inflating option-menu inside Fragment Toolbar
        setHasOptionsMenu(true)
        setupToolbar()


        val application = requireContext().applicationContext as Application

        val factory = EditViewModelFactory(application, navArgs, requireActivity()) {
            // redirect to  screen
            findNavController().navigate(R.id.editScreenFragment_to_homeFragment)
        }

        viewModel = ViewModelProvider(this, factory).get(EditViewModel::class.java)




        viewModel.imgSrc.observe(viewLifecycleOwner, {
            binding.photoEditorView.setImageBitmap(it)
        })



        binding.lifecycleOwner = this

        binding.viewModel = viewModel


        binding.bottomNavigationListener = object : EditScreenBottomNavigationListener {
            override fun crop() {
                navigateToCropFragment()
            }

            override fun rotate() {
                navigateToRotateFragment()
            }

            override fun adjust() {
                navigateToAdjustFragment()
            }

            override fun paint() {
                navigateToPaintFragment()
            }

            override fun filter() {
                navigateToFilterFragment()
            }

            override fun textSticker() {
                navigateToTextStickerFragment()
            }

            override fun sticker() {
                navigateToStickerFragment()
            }
        }









        return binding.root
    }





    private fun navigateToScreen(navigationId: Int) {
        findNavController().navigate(navigationId)
    }


    private fun setupToolbar() {
        binding.mainEditToolbar.apply {
            setNavigationOnClickListener {
                it.findNavController().navigateUp()
            }

            setOnMenuItemClickListener {
                when (it.itemId) {
                    R.id.savePhoto -> {

                        dialog.show()

                        viewModel.savePhotoInStorage {
                            dialog.hide()
                            navigateToSaveImageFragment(it)
                        }

                        true
                    }
                    else -> super.onOptionsItemSelected(it)
                }
            }


        }
    }


    // Navigating From MainEditScreen to PaintFragment
    private fun navigateToPaintFragment() {
        val action = EditScreenFragmentDirections
            .editScreenFragmentToPaintingFragment(getNavArgData())
        findNavController().navigate(action, getSharedElementExtra())

    }


    // Navigating From MainEditScreen to CropFragment
    private fun navigateToCropFragment() {

        val action = EditScreenFragmentDirections
            .editScreenFragmentToCropFragment(getNavArgData())
        findNavController().navigate(action, getSharedElementExtra())

    }


    // Navigating From MainEditScreen to RotateFragment
    private fun navigateToRotateFragment() {

        val action = EditScreenFragmentDirections
            .editScreenFragmentToRotationFragment(getNavArgData())

        findNavController().navigate(action, getSharedElementExtra())

    }

    // Navigating From MainEditScreen to FiltersFragment
    private fun navigateToFilterFragment() {

        val action = EditScreenFragmentDirections
            .editScreenFragmentToFiltersFragment(getNavArgData())

        findNavController().navigate(action, getSharedElementExtra())

    }

    // Navigating From MainEditScreen to AdjustFragment
    private fun navigateToAdjustFragment() {

        val action = EditScreenFragmentDirections
            .editScreenFragmentToAdjustEditFragment(getNavArgData())

        findNavController().navigate(action)

    }

    // Navigation From MainEditScreen to StickerFragment
    private fun navigateToStickerFragment() {

        val action = EditScreenFragmentDirections
            .editScreenFragmentToStickerFragment(getNavArgData())

        findNavController().navigate(action, getSharedElementExtra())

    }

    // Navigation From MainEditScreen to Text-StickerFragment
    private fun navigateToTextStickerFragment() {

        val action = EditScreenFragmentDirections
            .editScreenFragmentToTextStickerFragment(getNavArgData())

        findNavController().navigate(action, getSharedElementExtra())


    }

    // Navigation From MainEditScreen to SaveImageFragment

    private fun navigateToSaveImageFragment(uri: Uri) {
        val action =
            EditScreenFragmentDirections.actionMainEditScreenFragmentIdToSaveImageFragment(uri)
        findNavController().navigate(action)
    }


    private fun toastFun(string: String = "clicked") {
        Toast.makeText(context, string, Toast.LENGTH_SHORT).show()

    }


    private fun getSharedElementExtra(): FragmentNavigator.Extras {
        val imgView = binding.photoEditorView
        val imgTransitionUniqueName = resources.getString(R.string.img_transition_unique_name)

        return FragmentNavigatorExtras(imgView to imgTransitionUniqueName)
    }


    /**
     * [getBitmap] for get the bitmap from image view
     */
    private fun getBitmap(): Bitmap {
        return (binding.photoEditorView.drawable as BitmapDrawable).bitmap
    }


    /**
     * [getNavArgData] for get the nav arg data
     */
    private fun getNavArgData(uri: Uri? = null): CommonParcelData {
        if (uri == null) BitmapHelper.setBitmap(getBitmap(), true)

        return CommonParcelData(
            uri = uri,
            availableData = if (uri != null) ActiveNavArgsData.URI else ActiveNavArgsData.BITMAP
        )
    }


}


interface EditScreenBottomNavigationListener {
    fun crop()
    fun rotate()
    fun adjust()
    fun paint()
    fun filter()
    fun textSticker()
    fun sticker()
}