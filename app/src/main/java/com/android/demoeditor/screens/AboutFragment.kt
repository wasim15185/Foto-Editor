package com.android.demoeditor.screens

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.android.demoeditor.R
import com.android.demoeditor.databinding.FragmentAboutBinding
import androidx.databinding.DataBindingUtil
import androidx.navigation.findNavController

class AboutFragment : Fragment() {

    private lateinit var binding: FragmentAboutBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = DataBindingUtil.inflate(layoutInflater, R.layout.fragment_about, container, false)
        // This for Inflating menu inside Fragment Toolbar
        setHasOptionsMenu(true)
        setupToolbar()

        binding.floatingActionButton.setOnClickListener {
            startActivity(
                Intent(
                    Intent.ACTION_VIEW,
                    Uri.parse("https://play.google.com/store/apps/details?id=inc.bina.pikify")
                )
            )

        }


        return binding.root
    }


    /**
     * Inflating menu inside Fragment Toolbar
     */
    private fun setupToolbar() {

        binding.toolbar.apply {
            setNavigationOnClickListener {
                it.findNavController().navigateUp()
            }
        }

    }


}