package com.android.demoeditor.recyclerViews



import com.android.demoeditor.data.StickerData
import com.android.demoeditor.databinding.StickerRecyclerViewItemBinding
import android.graphics.drawable.Drawable
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView

class StickerAdapter( val clickListener: StickerItemListener) :
    ListAdapter<StickerData, StickerAdapter.ViewHolder>(StickerDiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)

        /**
         * Binding Creating
         */
        val bindView = StickerRecyclerViewItemBinding.inflate(layoutInflater, parent, false)
        return ViewHolder(bindView)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = getItem(position)
        holder.bind(item!!, clickListener)

    }

    class ViewHolder(val binding: StickerRecyclerViewItemBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(itemData: StickerData, clickListener: StickerItemListener ) {
            binding.stickerData = itemData
            binding.clickListener = clickListener

            // When You Using 'Binding Adapter' inside 'Recycler-View' must execute -> "binding.executePendingBindings()"
            binding.executePendingBindings()
        }

    }

}

/**
 * DiffUtil
 */
class StickerDiffCallback : DiffUtil.ItemCallback<StickerData>() {
    override fun areItemsTheSame(oldItem: StickerData, newItem: StickerData): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(oldItem: StickerData, newItem: StickerData): Boolean {
        return oldItem == newItem
    }
}

/**
 * Click-Listener
 */

class StickerItemListener(val clickListener: (stickerImgId: Drawable) -> Unit) {
    fun onClick(emojiItemData: StickerData) = clickListener(emojiItemData.stickerBitmapImg)
}