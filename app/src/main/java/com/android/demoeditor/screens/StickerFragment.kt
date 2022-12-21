package com.android.demoeditor.screens

import android.annotation.SuppressLint
import android.app.Application
import com.android.demoeditor.R
import com.android.demoeditor.customClass.BitmapHelper
import com.android.demoeditor.data.ViewPagerData
import com.android.demoeditor.data.parcelData.CommonParcelData
import com.android.demoeditor.databinding.FragmentStickerBinding
import com.android.demoeditor.enums.ActiveNavArgsData
import com.android.demoeditor.screens.viewPager.adapter.StickerViewPagerAdapter
import com.android.demoeditor.screens.viewPager.stickerFragment.*
import android.os.Bundle
import androidx.fragment.app.Fragment
import com.android.demoeditor.viewModel.StickerViewModel
import com.android.demoeditor.viewModelFactory.StickerViewModelFactory
import android.graphics.Bitmap
import android.graphics.drawable.Drawable
import android.net.Uri
import android.transition.TransitionInflater
import android.view.*
import androidx.core.app.SharedElementCallback
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.findNavController
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.google.android.material.tabs.TabLayoutMediator


class StickerFragment : Fragment() {

    private val TAG = StickerFragment::class.java.simpleName
    private lateinit var binding: FragmentStickerBinding

    //    private val mainViewModel: MainPaintViewModel by activityViewModels()
    private val navArgs by navArgs<StickerFragmentArgs>()

    private lateinit var stickerViewModel: StickerViewModel


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val animation =
            TransitionInflater.from(requireContext()).inflateTransition(android.R.transition.move)
        sharedElementEnterTransition = animation
        sharedElementReturnTransition = animation
    }


    @SuppressLint("UseCompatLoadingForDrawables")
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        // Inflate the layout for this fragment
        binding=DataBindingUtil.inflate(layoutInflater,R.layout.fragment_sticker,container,false)


        setupToolbar()


        val application = requireContext().applicationContext as Application
        val factory = StickerViewModelFactory(
            application,
            navArgs
        ){
            // redirect to screen
            findNavController().navigate(R.id.newStickerFragment_to_homeFragment)

        }

        stickerViewModel = ViewModelProvider(this, factory).get(StickerViewModel::class.java)

        // Specify the current activity as the lifecycle owner.
        binding.lifecycleOwner = this

        binding.stickerViewModel = stickerViewModel


        stickerViewModel.imgSrc.observe(viewLifecycleOwner,   {

            binding.stickerView.setMainImage(binding.mainImg)
            binding.stickerView.setMainBitmap(it)
            binding.mainImg.setImageBitmap(it)


        })


        /** This callback is called when "Shared-Element" is END  */
        setEnterSharedElementCallback(
            object : SharedElementCallback() {
                override fun onSharedElementEnd(
                    sharedElementNames: List<String?>?,
                    sharedElements: List<View?>?,
                    sharedElementSnapshots: List<View?>?
                ) {
                    // transition has ended

                    /** if you are get result bitmap u have to then MUST be View.VISIBLE the  [binding.resultImageview]  */
//                    binding.resultImageview.visibility=View.VISIBLE


                }
            }
        )


        val viewpagerData: List<ViewPagerData> = mutableListOf(

            ViewPagerData(
                AnimalViewPagerFragment.injectStickerBinding(binding),
                getString(R.string.animal)
            ),
            ViewPagerData(
                FacialViewPagerFragment.injectStickerBinding(binding),
                getString(R.string.facial)
            )
            ,
            ViewPagerData(
                FunViewPagerFragment.injectStickerBinding(binding),
                getString(R.string.cartoon)
            ),
            ViewPagerData(
                FoodViewPagerFragment.injectStickerBinding(binding),
                getString(R.string.food)
            ),
            ViewPagerData(
                WordsViewPagerFragment.injectStickerBinding(binding),
                getString(R.string.words)
            ),
        )


        binding.stickerViewPager.adapter =
            StickerViewPagerAdapter(childFragmentManager, lifecycle, viewpagerData)

        TabLayoutMediator(binding.stickerTabLayout, binding.stickerViewPager) { tab, position ->

            tab.text = viewpagerData[position].name

        }.attach()




        return binding.root
    }


    /**
     * Inflating menu inside Fragment Toolbar
     */


    private fun setupToolbar() {

        // This for Inflating menu inside Fragment Toolbar
        setHasOptionsMenu(true)

        binding.toolbar.apply {
            setNavigationOnClickListener {
                it.findNavController().navigateUp()
            }

            setOnMenuItemClickListener {
                when (it.itemId) {
                    R.id.tick_id -> {

                        val bitmap = binding.stickerView.createBitmap()
                        val data = getNavArgData(bitmap = bitmap)
                        navigateToScreen(data)

                        true
                    }
                    else -> super.onOptionsItemSelected(it)
                }
            }


        }


    }


   /* override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.common_tick_menu, menu)

    } */

   /* override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.tick_id -> {

                val bitmap = binding.stickerView.createBitmap()

//                val data = CommonParcelData(bitmap = bitmap, availableData = ActiveNavArgsData.BITMAP , isResize = true )
                val data = getNavArgData(bitmap = bitmap)


                navigateToScreen(data)

                return true
            }
            else -> super.onOptionsItemSelected(item)
        }
    } */


    @SuppressLint("UseCompatLoadingForDrawables")
    private fun getDrawable(id: Int): Drawable {
        return requireContext().getDrawable(id)!!
    }


    private fun navigateToScreen(data: CommonParcelData) {
        val action = StickerFragmentDirections.newStickerFragmentToMainEditScreenFragment(data)
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