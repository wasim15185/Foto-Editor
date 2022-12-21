package com.android.demoeditor.recyclerViews

import com.android.demoeditor.data.MainScreenNavScreenItemData
import com.android.demoeditor.databinding.MainScreenNavbarItemBinding
import com.android.demoeditor.enums.MainScreenItems
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView

class MainEditScreenNavbarAdapter(val clickListener: MainEditScreenNavbarListener) :
    ListAdapter<MainScreenNavScreenItemData, MainEditScreenNavbarAdapter.ViewHolder>(MainEditScreenNavbarDiffCallback()) {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)

        /**
         * Binding Creating
         */
        val bindView = MainScreenNavbarItemBinding.inflate(layoutInflater, parent, false)
        return ViewHolder(bindView)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = getItem(position)
        holder.bind(item!!, clickListener)
    }


    class ViewHolder(val binding: MainScreenNavbarItemBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(
            itemData: MainScreenNavScreenItemData,
            clickListener: MainEditScreenNavbarListener
        ) {

            binding.clickListener = clickListener
            binding.data = itemData

            // When You Using 'Binding Adapter' inside 'Recycler-View' must execute -> "binding.executePendingBindings()"
            binding.executePendingBindings()
        }


    }

}


/**
 * DiffUtil
 */

class MainEditScreenNavbarDiffCallback : DiffUtil.ItemCallback<MainScreenNavScreenItemData>() {
    override fun areItemsTheSame(
        oldItem: MainScreenNavScreenItemData,
        newItem: MainScreenNavScreenItemData
    ): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(
        oldItem: MainScreenNavScreenItemData,
        newItem: MainScreenNavScreenItemData
    ): Boolean {
        return oldItem == newItem
    }
}


class MainEditScreenNavbarListener(val clickListener:(itemEnum:MainScreenItems)->Unit) {

    fun onClick(itemEnum:MainScreenItems)=clickListener(itemEnum)

}