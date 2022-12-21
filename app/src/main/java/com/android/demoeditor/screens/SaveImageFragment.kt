package com.android.demoeditor.screens

import android.annotation.SuppressLint
import android.app.Application
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.android.demoeditor.R
import com.android.demoeditor.database.RecentPhotoDatabase
import com.android.demoeditor.databinding.FragmentSaveImageBinding
import com.android.demoeditor.viewModel.SaveImageViewModel
import com.android.demoeditor.viewModelFactory.SaveImageViewModelFactory
import android.graphics.drawable.Drawable
import android.os.StrictMode
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.findNavController
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs


class SaveImageFragment : Fragment() {

    private val TAG = this::class.java.simpleName

    private lateinit var binding: FragmentSaveImageBinding

    private val navArgs by navArgs<SaveImageFragmentArgs>()

    private lateinit var viewModel: SaveImageViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding =
            DataBindingUtil.inflate(layoutInflater, R.layout.fragment_save_image, container, false)

        binding.saveMainImg.setImageURI(navArgs.uri)

        val recentPhotoDatabase = RecentPhotoDatabase.getInstance(requireContext())

        val factory = SaveImageViewModelFactory(
            recentPhotoDatabase,
            requireContext().applicationContext as Application,
            requireActivity()
        )
        viewModel = ViewModelProvider(this, factory).get(SaveImageViewModel::class.java)

        viewModel.insertUriIntoDB(navArgs.uri)


        /**
         * this 2 lines only for share image via Intent (eg:- Whatsapp, facebook,instagram)
         */
        val policyBuilder = StrictMode.VmPolicy.Builder()
        StrictMode.setVmPolicy(policyBuilder.build())


        binding.instagram.setOnClickListener {
            viewModel.instagramShareIntent(navArgs.uri)
        }

        binding.whatsapp.setOnClickListener {
            viewModel.whatsAppShareIntent(navArgs.uri)
        }

        binding.twitter.setOnClickListener {
            viewModel.twitterShareIntent(navArgs.uri)

        }



        setupToolBar()

        return binding.root
    }


    private fun setupToolBar() {
        binding.saveToolbar.apply {
            setNavigationOnClickListener {
                it.findNavController().navigateUp()
            }

            setOnMenuItemClickListener {
                when (it.itemId) {
                    R.id.save_home_id -> {
                        navigateToHomeScreen()
                        true
                    }
                    else -> super.onOptionsItemSelected(it)
                }
            }


        }
    }


    private fun navigateToHomeScreen(){
        findNavController().navigate(R.id.saveImageFragment_to_homeFragment)
    }


    @SuppressLint("UseCompatLoadingForDrawables")
    private fun getDrawable(drawableId: Int): Drawable {
        return requireContext().getDrawable(drawableId)!!

    }


}