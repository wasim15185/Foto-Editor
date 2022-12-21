package com.android.demoeditor.screens

import android.annotation.SuppressLint
import android.app.Application
import com.android.demoeditor.R
import com.android.demoeditor.customClass.BitmapHelper
import com.android.demoeditor.data.parcelData.CommonParcelData
import android.os.Bundle
import androidx.fragment.app.Fragment
import com.android.demoeditor.databinding.FragmentFiltersBinding
import com.android.demoeditor.enums.ActiveNavArgsData
import com.android.demoeditor.recyclerViews.FiltersNavItemListener
import com.android.demoeditor.recyclerViews.FiltersNavItemsAdapter
import com.android.demoeditor.viewModel.FiltersViewModel
 import com.android.demoeditor.viewModelFactory.FiltersViewModelFactory
import android.graphics.Bitmap
import android.graphics.drawable.BitmapDrawable
import android.net.Uri
import android.transition.TransitionInflater
import android.view.*
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.findNavController
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import jp.co.cyberagent.android.gpuimage.GPUImage
import jp.co.cyberagent.android.gpuimage.filter.GPUImageFilter


class FiltersFragment : Fragment() {

    private val TAG=FiltersFragment::class.java.simpleName


    private lateinit var binding: FragmentFiltersBinding
     private lateinit var filtersViewModel: FiltersViewModel

    private val navArgs by navArgs<FiltersFragmentArgs>()



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val animation=TransitionInflater.from(requireContext()).inflateTransition(android.R.transition.move)
        sharedElementEnterTransition=animation
        sharedElementReturnTransition=animation
    }


    @SuppressLint("ResourceAsColor")
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {



        // This for Inflating menu inside Fragment Toolbar
        setHasOptionsMenu(true)

        // Inflate the layout for this fragment
        binding= FragmentFiltersBinding.inflate(layoutInflater,container, false)

        setupToolBar()

        val factory=FiltersViewModelFactory(requireContext().applicationContext as Application , navArgs ){
            // redirect to screen

            findNavController().navigate(R.id.filtersFragment_to_homeFragment)

        }
        filtersViewModel=ViewModelProvider(this,factory).get(FiltersViewModel::class.java)

        binding.lifecycleOwner=this

        binding.viewModel=filtersViewModel

        filtersViewModel.imgSrc.observe(viewLifecycleOwner, Observer {
            binding.imgId.setImageBitmap(it)
        })

        val  adapter = FiltersNavItemsAdapter()

        val recyclerViewListener = FiltersNavItemListener { filter, position ->
            applyFilter(filter)
            adapter.setSingleSelection(position)
//          binding.filterRecyclerView.smoothScrollToPosition(position+1)
        }





        adapter.clickListener=recyclerViewListener

        binding.filterRecyclerView.adapter = adapter

        filtersViewModel.listOfFilters.observe(viewLifecycleOwner, Observer {
            it?.let {
                adapter.submitList(it)
            }

        })

        return binding.root
    }

    /** [applyFilter] is to Apply Filter
     * @param filter
     *
     */

    private fun applyFilter(filter: GPUImageFilter,) {
        var gpuImg:GPUImage?=GPUImage(context)
        gpuImg?.setFilter(filter)
        val sourceImg=filtersViewModel.imgSrc.value
       val resultImgBitmap= gpuImg?.getBitmapWithFilterApplied(sourceImg);
        binding.imgId.setImageBitmap(resultImgBitmap)
        gpuImg=null
    }


    /**
     * Inflating menu inside Fragment Toolbar
     */

    private fun setupToolBar(){
        binding.toolbar.apply {
            setNavigationOnClickListener {
                it.findNavController().navigateUp()
            }

            setOnMenuItemClickListener {
                when (it.itemId) {
                    R.id.tick_id -> {

                         navigateToMainScreen()

                        true
                    }
                    else -> super.onOptionsItemSelected(it)
                }
            }


        }
    }


    /**
     * [navigateToMainScreen] is Navigation fun .
     */

    private fun navigateToMainScreen( ) {

        val bitmap = (binding.imgId.drawable as BitmapDrawable).bitmap
        val data = getNavArgData(bitmap = bitmap)

        val action = FiltersFragmentDirections.filtersFragmentToMainEditScreenFragment(data)

        findNavController().navigate(action)
    }


    /**
     * [getNavArgData] for get the nav arg data
     */
    private fun getNavArgData(uri: Uri?=null , bitmap: Bitmap?=null ) : CommonParcelData {

        bitmap?.also {
            BitmapHelper.setBitmap(it,true)
        }

        return CommonParcelData(
            uri=uri,
            availableData = if (uri !=null ) ActiveNavArgsData.URI else ActiveNavArgsData.BITMAP
        )
    }





}