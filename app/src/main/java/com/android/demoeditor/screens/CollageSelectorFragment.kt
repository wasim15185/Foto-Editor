package com.android.demoeditor.screens

import android.app.Application
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.android.demoeditor.R
import com.android.demoeditor.customClass.BitmapHelper
import com.android.demoeditor.data.ViewPagerData
import com.android.demoeditor.data.parcelData.CommonParcelData
import com.android.demoeditor.databinding.FragmentCollageSelectorBinding
import com.android.demoeditor.enums.ActiveNavArgsData
import com.android.demoeditor.recyclerViews.CollageNavbarAdapter
import com.android.demoeditor.screens.dialog.LoadingDialog2
import com.android.demoeditor.screens.viewPager.adapter.CollageSelectorViewPagerAdapter
import com.android.demoeditor.screens.viewPager.collageSelectorFragment.CollageBackgroundColorViewPagerFragment
import com.android.demoeditor.screens.viewPager.collageSelectorFragment.CollageEditViewPagerFragment
import com.android.demoeditor.screens.viewPager.collageSelectorFragment.CollageLayoutViewPagerFragment
import com.android.demoeditor.screens.viewPager.collageSelectorFragment.CollageResizeViewPagerFragment
import com.android.demoeditor.utils.getBitmap
import com.android.demoeditor.utils.getPuzzleData
import com.android.demoeditor.utils.setUpPuzzleLayout
import com.android.demoeditor.viewModel.CollageSelectorViewModel
import com.android.demoeditor.viewModelFactory.CollageSelectorViewModelFactory
import android.graphics.Bitmap
import android.net.Uri
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.findNavController
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.google.android.material.tabs.TabLayoutMediator
import kotlinx.coroutines.*


class CollageSelectorFragment : Fragment() {

    private val TAG = CollageSelectorFragment::class.java.simpleName


    private lateinit var binding: FragmentCollageSelectorBinding

    private val navArgs by navArgs<CollageSelectorFragmentArgs>()

    private lateinit var viewModel: CollageSelectorViewModel


    private lateinit var layoutSelectorAdapter: CollageNavbarAdapter


    private lateinit var collageViewPagerAdapter: CollageSelectorViewPagerAdapter

    private var backgroundScope: CoroutineScope? = CoroutineScope(Dispatchers.Default + Job())


    private val dialog by lazy { LoadingDialog2(requireContext()) }

    @RequiresApi(Build.VERSION_CODES.N)
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = DataBindingUtil.inflate(
            layoutInflater,
            R.layout.fragment_collage_selector,
            container,
            false
        )

//        dialog2=LoadingDialog2(requireContext())

        setupToolbar()

        val factory = CollageSelectorViewModelFactory(
            requireContext().applicationContext as Application,
            navArgs.data.listOfUri,
            requireActivity(),
        )
        viewModel = ViewModelProvider(this, factory).get(CollageSelectorViewModel::class.java)

        /**
         * disable [ViewPager2] swift left right gesture
         */
        binding.viewPager.isUserInputEnabled = false
        // Specify the current activity as the lifecycle owner.
        binding.lifecycleOwner = this

        binding.viewModel = viewModel


//        viewModel.process.observe(viewLifecycleOwner,{
//            Log.d(TAG, "progress $it ")
//            binding.progressBar.setProgress(it.toInt(),true)
//        })


        setUpMainPuzzleView()

        configViewPager()
        initializeBackgroundPuzzleView()

        binding.floatingActionButton.setOnClickListener {
            navigateToMainScreenScreen()
        }


        return binding.root
    }

    private fun initializeBackgroundPuzzleView() {

        binding.puzzleView.apply {
//            isNeedDrawLine = true
//            isNeedDrawOuterLine = true
            isTouchEnable = true
        }


        viewModel.recentColorData.observe(viewLifecycleOwner, {
            binding.puzzleView.setBackgroundColor(it.color)
        })


    }

    private fun setUpMainPuzzleView() {

        viewModel.listOfImages.observe(viewLifecycleOwner, {

            if (viewModel.getInitLayoutForPuzzleLayout() != null) {


                if (it.size == navArgs.data.listOfUri.size) {
                    val layout = viewModel.getInitLayoutForPuzzleLayout()!!.layout

                    val layoutData = getPuzzleData(layout, it.size)

                    setUpPuzzleLayout(layoutData, it, binding.puzzleView)

                }


            }
        })


    }


    private fun configViewPager() {

        val viewpagerData: List<ViewPagerData> = mutableListOf(
            ViewPagerData(
                CollageLayoutViewPagerFragment.injectParentViewModel(viewModel, binding),
                "Layout"
            ),
            ViewPagerData(
                CollageEditViewPagerFragment.injectParentViewModel(viewModel, binding),
                "Edit"
            ),
            ViewPagerData(
                CollageBackgroundColorViewPagerFragment.injectParentViewModel(
                    viewModel,
                    binding
                ), "BG"
            ),
            ViewPagerData(
                CollageResizeViewPagerFragment.injectParentViewModel(viewModel, binding),
                "Resize"
            ),
        )

        collageViewPagerAdapter =
            CollageSelectorViewPagerAdapter(childFragmentManager, lifecycle, viewpagerData)

        binding.viewPager.adapter = collageViewPagerAdapter

        TabLayoutMediator(binding.collageTabLayout, binding.viewPager) { tab, position ->
            tab.text = viewpagerData[position].name
        }.attach()


    }


    private fun setupToolbar() {
        binding.collageToolbar.apply {
            setNavigationOnClickListener {
                it.findNavController().navigateUp()
            }

            setOnMenuItemClickListener {
                when (it.itemId) {
                    R.id.savePhoto -> {


                        navigateToSaveFragment()

                        true
                    }
                    else -> super.onOptionsItemSelected(it)
                }
            }


        }

    }


    private fun navigateToSaveFragment() {

        dialog.show()

        viewModel.savePhotoInFile(getBitmap()) {
            dialog.hide()

            val action = CollageSelectorFragmentDirections
                .collageSelectorFragmentToSaveImageFragment(it)

            findNavController().navigate(action)
        }


    }


    private fun navigateToMainScreenScreen() {

        val action =
            CollageSelectorFragmentDirections.collageSelectorFragmentToMainEditScreenFragment(
                getNavArgData()
            )

        findNavController().navigate(action)


    }


    override fun onDestroy() {
        super.onDestroy()
        backgroundScope = null
    }


    /**
     * [getBitmap] for get the bitmap from image view
     */
    private fun getBitmap(): Bitmap {
        return binding.puzzleView.getBitmap()
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