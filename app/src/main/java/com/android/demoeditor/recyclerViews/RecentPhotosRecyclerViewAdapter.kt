package com.android.demoeditor.recyclerViews

 import com.android.demoeditor.databinding.RecentPhotoItemBinding
 import android.net.Uri
import android.view.LayoutInflater
import android.view.ViewGroup
 import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
 import java.util.*


class RecentPhotosRecyclerViewAdapter( var clickListener: RecentPhotosSelectorClickListener? = null) :
    ListAdapter<RecentPhotoItem, RecentPhotosRecyclerViewAdapter.ViewHolder>(RecentPhotosSelectorDiffCallBack()) {



    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        // Binding Created

        val bindView = RecentPhotoItemBinding.inflate(layoutInflater)
        return ViewHolder(bindView)
    }


    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = getItem(position)
        holder.bind(item!!, clickListener)

     }




    class ViewHolder(val binding: RecentPhotoItemBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(item: RecentPhotoItem, clickListener: RecentPhotosSelectorClickListener?) {
            binding.data = item
            binding.clickListener=clickListener

            // When You Using 'Binding Adapter' inside 'Recycler-View' must execute -> "binding.executePendingBindings()"
            binding.executePendingBindings()
        }

    }


}


/**
 * [RecentPhotosSelectorDiffCallBack] is Diff-Callback
 */

class RecentPhotosSelectorDiffCallBack : DiffUtil.ItemCallback<RecentPhotoItem>() {
    override fun areItemsTheSame(oldItem: RecentPhotoItem, newItem: RecentPhotoItem): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(oldItem: RecentPhotoItem, newItem: RecentPhotoItem): Boolean {
        return oldItem == newItem

    }
}


class RecentPhotosSelectorClickListener(val clickListener: (uri:Uri) -> Unit) {
    fun click(uri:Uri) = clickListener(uri)

}




data class RecentPhotoItem(
    val id: String = UUID.randomUUID().toString(),
    val uri: Uri
 )