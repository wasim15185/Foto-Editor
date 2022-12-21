package com.android.demoeditor.recyclerViews

import com.android.demoeditor.data.ImgSelectorData
import com.android.demoeditor.databinding.ImgShowerItemBinding
 import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions

class ImageShowerAdapter(var clickListener: ImgShowerItemListener? = null) :
    ListAdapter<ImgSelectorData, ImageShowerAdapter.ViewHolder>(ImageShowerDiffCallback()) {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)

        val bindView = ImgShowerItemBinding.inflate(layoutInflater, parent, false)
        return ViewHolder(bindView)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = getItem(position)
        loadImgWithGlide(item.imagePath,holder.binding.imageView)
        holder.bind(item, clickListener!!)
    }


    /**
     * [loadImgWithGlide] fun for loading image
     * @param path [String]
     * @param imgView [ImageView]
     */
    private fun loadImgWithGlide(path: String,imgView: ImageView) {
        Glide.with(imgView)
            .load(path)
            .transition(DrawableTransitionOptions().crossFade())
            .into(imgView);
    }



    /**
     * [ViewHolder] is View-Holder
     * @param binding  [ImgShowerItemBinding]
     */
  inner  class ViewHolder(val binding: ImgShowerItemBinding) : RecyclerView.ViewHolder(binding.root) {

        fun bind(item: ImgSelectorData, clickListener: ImgShowerItemListener?) {

            binding.clickListener = clickListener
            binding.position = absoluteAdapterPosition
            binding.imgItemData = item

            // When You Using 'Binding Adapter' inside 'Recycler-View' must execute -> "binding.executePendingBindings()"
            binding.executePendingBindings()

        }

    }


}

/**
 * [ImageShowerDiffCallback] is diff util callback
 */

  class ImageShowerDiffCallback : DiffUtil.ItemCallback<ImgSelectorData>() {
    override fun areItemsTheSame(oldItem: ImgSelectorData, newItem: ImgSelectorData): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(oldItem: ImgSelectorData, newItem: ImgSelectorData): Boolean {
        return oldItem == newItem
    }
}

/**
 * [ImgShowerItemListener] is Click-Listener class
 */
class ImgShowerItemListener(val clickListener: (imgItemData: ImgSelectorData, position: Int) -> Unit) {
    fun onClick(imgItemData: ImgSelectorData, position: Int) = clickListener(imgItemData, position)
}