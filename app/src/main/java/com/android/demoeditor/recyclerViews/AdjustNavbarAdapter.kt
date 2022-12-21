package com.android.demoeditor.recyclerViews

//import android.example.photoeditor.data.AdjustNavItemData
//import android.example.photoeditor.databinding.AdjustBottomNavbarRecyclerviewItemBinding
//import android.view.LayoutInflater
//import android.view.ViewGroup
//import androidx.recyclerview.widget.DiffUtil
//import androidx.recyclerview.widget.ListAdapter
//import androidx.recyclerview.widget.RecyclerView
//
//class AdjustNavbarAdapter :
//    ListAdapter<AdjustNavItemData, AdjustNavbarAdapter.ViewHolder>(AdjustNavbarDiffCallback()) {
//
//    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
//        val layoutInflater = LayoutInflater.from(parent.context)
//        // Binding Created
//        val bindView =
//            AdjustBottomNavbarRecyclerviewItemBinding.inflate(layoutInflater, parent, false)
//        return ViewHolder(bindView)
//    }
//
//    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
//        TODO("Not yet implemented")
//    }
//
//    class ViewHolder(val binding: AdjustBottomNavbarRecyclerviewItemBinding) :
//        RecyclerView.ViewHolder(binding.root) {
//
//    }
//
//}
//
//class AdjustNavbarDiffCallback : DiffUtil.ItemCallback<AdjustNavItemData>() {
//    override fun areItemsTheSame(oldItem: AdjustNavItemData, newItem: AdjustNavItemData): Boolean {
//        return oldItem.id == newItem.id
//    }
//
//    override fun areContentsTheSame(
//        oldItem: AdjustNavItemData,
//        newItem: AdjustNavItemData
//    ): Boolean {
//        return oldItem == newItem
//    }
//}