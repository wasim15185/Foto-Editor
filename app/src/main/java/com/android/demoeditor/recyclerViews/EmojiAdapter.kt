package com.android.demoeditor.recyclerViews

import com.android.demoeditor.data.EmojiData
import com.android.demoeditor.databinding.ViewPagerEmojiItemBinding
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView

class EmojiAdapter(val clickListener: EmojiItemListener ) : ListAdapter<EmojiData, EmojiAdapter.ViewHolder>(EmojiDiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)

        /**
         * Binding Creating
         */
        val bindView = ViewPagerEmojiItemBinding.inflate(layoutInflater, parent, false)
        return ViewHolder(bindView)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item =getItem(position)
        holder.bind(item!!,clickListener)

    }

    class ViewHolder(val binding: ViewPagerEmojiItemBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(itemData: EmojiData,clickListener: EmojiItemListener) {
            binding.emojiItemData=itemData
            binding.clickListener=clickListener

            // When You Using 'Binding Adapter' inside 'Recycler-View' must execute -> "binding.executePendingBindings()"
            binding.executePendingBindings()
        }

    }

}

/**
 * DiffUtil
 */
class EmojiDiffCallback : DiffUtil.ItemCallback<EmojiData>() {
    override fun areItemsTheSame(oldItem: EmojiData, newItem: EmojiData): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(oldItem: EmojiData, newItem: EmojiData): Boolean {
        return oldItem == newItem
    }
}

/**
 * Click-Listener
 */

class EmojiItemListener(val clickListener:(emojiUnicode:String)->Unit){
    fun onClick(emojiItemData:EmojiData)=clickListener(emojiItemData.emoji)
}