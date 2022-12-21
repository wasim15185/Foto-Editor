package com.android.demoeditor.screens

import android.content.ActivityNotFoundException
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.os.Handler
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.navigation.findNavController
import com.android.demoeditor.R
import com.android.demoeditor.databinding.FragmentDeveloperDetailsBinding


class DeveloperDetailsFragment : Fragment() {

    private val TAG = this::class.java.name

    private lateinit var binding: FragmentDeveloperDetailsBinding

    private val handler = Handler()

    private val nameStr = " Wasim Akram Biswas".toCharArray()

    private var animText = ""


    private var nameIdx = 0;


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding = DataBindingUtil.inflate(
            layoutInflater,
            R.layout.fragment_developer_details, container, false
        )

        setUpImageView()

        setupAnimationTextView()

        setupToolbar()


        binding.mail.setOnClickListener {
            emailShareIntent()
        }

        binding.linkedin.setOnClickListener {
            linkedinIntent()
        }

        handler.postDelayed(object : Runnable {
            override fun run() {
                //Call your function here


                if (nameIdx in 0..nameStr.size - 2) {
                    ++nameIdx
                    animText += nameStr[nameIdx]
                } else {
                    nameIdx = 0
                    animText = ""
                }
                animateText()

                handler.postDelayed(this, 550)//1 sec delay
            }
        }, 0)


        // Inflate the layout for this fragment
        return binding.root;
    }


    private fun setupAnimationTextView() {

        binding.developerName.apply {
            animateText(getText(R.string.developer_name))

        }

    }


    private fun alert(text: String) {
        Toast.makeText(context, text, Toast.LENGTH_SHORT)
    }

    private fun animateText() {
        binding.developerName.animateText(animText)
     }

    private fun setUpImageView() {
//        val radius = resources.getDimension(R.dimen.developer_details_for_all_corner_radius)
        val radius = 12f
        val shapeAppearanceModel = binding.developerImg.shapeAppearanceModel.toBuilder()
            .setAllCornerSizes(radius)
            .build()
        binding.developerImg.shapeAppearanceModel = shapeAppearanceModel


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



    /**Intents*/

  private  fun emailShareIntent() {


        val shareEmailIntent = Intent(Intent.ACTION_SEND )
            .apply {

                data = Uri.parse("mailto:"); // only email apps should handle this

                putExtra(
                    Intent.ACTION_SENDTO,
                    "wasimakram15185@gmail.com"
                );
                type = "text/plain";

                putExtra(Intent.EXTRA_SUBJECT, "Mail For Foto Editor ");


              }

        try {
            activity!!.startActivity(shareEmailIntent)
        } catch (ex: ActivityNotFoundException) {
            Toast.makeText(
                context,
                "Email have not been installed.",
                Toast.LENGTH_SHORT
            ).show()
        }


    }



    private fun linkedinIntent(){

        val profile_url = "https://www.linkedin.com/in/wasim-akram-biswas-753b77204"


        val shareLinkedinIntent = Intent(Intent.ACTION_VIEW, Uri.parse(profile_url))
            .apply {

                setPackage("com.linkedin.android")
                addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);


            }

        try {
            activity!!.startActivity(shareLinkedinIntent)
        } catch (ex: ActivityNotFoundException) {
            Toast.makeText(
                context,
                "Linkedin have not been installed.",
                Toast.LENGTH_SHORT
            ).show()
        }


    }












}