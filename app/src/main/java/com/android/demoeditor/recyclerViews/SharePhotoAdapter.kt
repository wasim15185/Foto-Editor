package com.android.demoeditor.recyclerViews

import com.android.demoeditor.data.ShareItemData
import com.android.demoeditor.databinding.ShareItemBinding
import com.android.demoeditor.enums.ShareIconType
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView

class SharePhotoAdapter(private val clickListener: ShareItemListener) :
    ListAdapter<ShareItemData, SharePhotoAdapter.ViewHolder>(ShareItemDiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        // Binding Created
        val bindView = ShareItemBinding.inflate(layoutInflater, parent, false)
        return ViewHolder(bindView)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = getItem(position)
        holder.bind(item!!, clickListener)
    }

    class ViewHolder(val binding: ShareItemBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(item: ShareItemData, clickListener: ShareItemListener) {
            binding.clickListener = clickListener
            binding.shareItemData = item
            binding.position = absoluteAdapterPosition

            // When You Using 'Binding Adapter' inside 'Recycler-View' must execute -> "binding.executePendingBindings()"
            binding.executePendingBindings()
        }
    }
}

/**
 * [ShareItemDiffCallback] is Diff-Util callback
 */
class ShareItemDiffCallback() : DiffUtil.ItemCallback<ShareItemData>() {
    override fun areItemsTheSame(oldItem: ShareItemData, newItem: ShareItemData): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(oldItem: ShareItemData, newItem: ShareItemData): Boolean {
        return oldItem == newItem
    }

}

/**
 * [ShareItemListener] is "Click-Listener"
 */
class ShareItemListener(val clickListener: (iconType:ShareIconType)->Unit) {
fun onClick(shareItemData: ShareItemData) = clickListener(shareItemData.type)
}


