package com.android.demoeditor.screens

import android.app.Application
import com.android.demoeditor.R
import com.android.demoeditor.databinding.FragmentImgSelectorBinding
import android.os.Bundle
import androidx.fragment.app.Fragment
import com.android.demoeditor.recyclerViews.ImageShowerAdapter
import com.android.demoeditor.recyclerViews.ImgShowerItemListener
import com.android.demoeditor.spinner.AlbumsAdapter
import com.android.demoeditor.viewModel.ImgViewModel
import com.android.demoeditor.viewModelFactory.ImgViewModelFactory
import android.util.Log
import android.view.*
import androidx.appcompat.widget.AppCompatSpinner
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView


class ImgSelectorFragment : Fragment() {

    private lateinit var binding: FragmentImgSelectorBinding

    private lateinit var viewModel: ImgViewModel


    private lateinit var mLayoutManager: GridLayoutManager

    private lateinit var spinner: AppCompatSpinner


//    private var visibleThreshold = 5
//    private var lastVisibleItem = 0
//    private var totalItemCount = 0
//    private var isLoading: Boolean = false


    var visibleThreshold = 5
        set(value) {
            if (value < 0) {
                throw Exception("You can't set negative")
            } else {
                field = value
            }
        }

    var lastVisibleItem = 0
        set(value) {
            if (value < 0) {
                throw Exception("You can't set negative")
            } else {
                field = value
            }
        }

    var totalItemCount = 0
        set(value) {
            if (value < 0) {
                throw Exception("You can't set negative")
            } else {
                field = value
            }
        }

    var isLoading: Boolean = false


    companion object {
        private const val STORAGE_PERMISSION = 100;
        private const val REQUEST_IMAGE_CAPTURE = 101;
        private const val PICK_IMAGES = 102;

        private val TAG = ImgSelectorFragment::class.java.simpleName

    }

    private lateinit var adapter: ImageShowerAdapter
    private lateinit var albumSpinnerAdapter: AlbumsAdapter


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        // This for Inflating option-menu inside Fragment Toolbar
        setHasOptionsMenu(true)


        // Inflate the layout for this fragment
        binding =
            DataBindingUtil.inflate(inflater, R.layout.fragment_img_selector, container, false)

        // Back-Btn setup
//        binding.backBtn.setOnClickListener {
//            requireActivity().onBackPressed()
//        }


        adapter = ImageShowerAdapter()

        mLayoutManager = GridLayoutManager(requireContext(), 3)




        visibleThreshold *= mLayoutManager.spanCount

        val factory = ImgViewModelFactory(
            (requireContext().applicationContext) as Application,
            adapter,
            requireActivity()
        )

        viewModel = ViewModelProvider(this, factory).get(ImgViewModel::class.java)
        // Specify the current activity as the lifecycle owner.
        binding.lifecycleOwner = this






//viewModel.loadAlbums()





        configImgShowerRecyclerView()
        setLoadMoreListener()




        return binding.root
    }




    private fun configImgShowerRecyclerView() {

        val listener = ImgShowerItemListener() { imgItemData, position ->
        }


        adapter.clickListener = listener


        binding.listImgRecyclerView.apply {
            layoutManager = mLayoutManager
            setHasFixedSize(true)
            adapter = this@ImgSelectorFragment.adapter
        }


        viewModel.dataOfImgShowerRecyclerView.observe(viewLifecycleOwner, Observer {
             it?.let {
                Log.d(TAG, "page no : ${viewModel.mPage}")

                adapter.submitList(it)

            }
        })



        viewModel._isListUpdated.observe(viewLifecycleOwner, {
            adapter.notifyDataSetChanged()
            isLoading = false

        })


    }


    private fun setLoadMoreListener() {

        binding.listImgRecyclerView.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)

                //total no. of items
                totalItemCount = mLayoutManager.itemCount
                //last visible item position
                lastVisibleItem = mLayoutManager.findLastCompletelyVisibleItemPosition()

                if (!isLoading && totalItemCount <= (lastVisibleItem + visibleThreshold)) {
                    loadMore()
                    isLoading = true
                }


            }
        })


    }

    private fun loadMore() {
        viewModel.loadMoreImages()
    }


    /**
     * Toolbar setup
     */


     override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.img_selector_menu, menu)

    }





}