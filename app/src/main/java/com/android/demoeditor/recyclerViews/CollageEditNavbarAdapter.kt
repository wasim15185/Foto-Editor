package com.android.demoeditor.recyclerViews

import com.android.demoeditor.data.CollageEditItemData
 import com.android.demoeditor.databinding.CollageEditScreenNavItemBinding
 import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView

class CollageEditNavbarAdapter(val clickListener: CollageEditScreenNavbarListener)
    :ListAdapter<CollageEditItemData,CollageEditNavbarAdapter.ViewHolder>(CollageEditScreenNavbarDiffCallback()){

        private val TAG = this::class.java

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)

        val bindView = CollageEditScreenNavItemBinding.inflate(layoutInflater, parent, false)
        return  ViewHolder(bindView)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = getItem(position)
        holder.bind(item!!, clickListener)
     }

    class ViewHolder(val binding: CollageEditScreenNavItemBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(
            itemData: CollageEditItemData,
            clickListener:CollageEditScreenNavbarListener){
            binding.clickListener = clickListener
            binding.data = itemData

            // When You Using 'Binding Adapter' inside 'Recycler-View' must execute -> "binding.executePendingBindings()"
            binding.executePendingBindings()
        }
    }


}





class CollageEditScreenNavbarDiffCallback : DiffUtil.ItemCallback<CollageEditItemData>() {
    override fun areItemsTheSame(
        oldItem: CollageEditItemData,
        newItem: CollageEditItemData
    ): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(
        oldItem: CollageEditItemData,
        newItem: CollageEditItemData
    ): Boolean {
        return oldItem == newItem
    }
}


class CollageEditScreenNavbarListener(val clickListener:(itemEnum: CollageIconType)->Unit) {

    fun onClick(itemEnum: CollageIconType)=clickListener(itemEnum)

}



enum class CollageIconType {
ROTATE ,
FLIP ,
MIRROR
}