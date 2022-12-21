package com.android.demoeditor.recyclerViews

import com.android.demoeditor.data.ColorPickerData
import com.android.demoeditor.databinding.ColorPickerItemBinding
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView


class ColorPickerAdapter(var clickListener: ColorPickerItemListener?=null  ) :
    ListAdapter<ColorPickerData, ColorPickerAdapter.ViewHolder>(ColorPickerDiffCallback()) {

    private val TAG=ColorPickerAdapter::class.java.simpleName

    private var selectedPosition= 0

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        // Binding Created
        val bindView = ColorPickerItemBinding.inflate(layoutInflater, parent, false)
        return ViewHolder(bindView)
    }


    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = getItem(position)
        holder.bind(item!!, clickListener)

        if (selectedPosition==position){
            holder.binding.selectImgView.visibility= View.VISIBLE

        }else{
            holder.binding.selectImgView.visibility= View.INVISIBLE
        }


    }


    fun  setSingleSelection(position: Int) {

        if (position==RecyclerView.NO_POSITION) return

        notifyItemChanged(selectedPosition)
        selectedPosition=position
        notifyItemChanged(selectedPosition)

    }


    class ViewHolder(val binding: ColorPickerItemBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(item: ColorPickerData, clickListener: ColorPickerItemListener?) {

            binding.colorPickerItemData = item
            binding.clickListener = clickListener
            binding.position = absoluteAdapterPosition

            // When You Using 'Binding Adapter' inside 'Recycler-View' must execute -> "binding.executePendingBindings()"
            binding.executePendingBindings()

        }

    }


}

/**
 *  [ColorPickerDiffCallback] is Diff-Util Callback
 */

class ColorPickerDiffCallback : DiffUtil.ItemCallback<ColorPickerData>() {
    override fun areItemsTheSame(oldItem: ColorPickerData, newItem: ColorPickerData): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(oldItem: ColorPickerData, newItem: ColorPickerData): Boolean {
        return oldItem == newItem
    }
}

/**
 * [ColorPickerItemListener] is "Click-Listener"
 */

class ColorPickerItemListener(val clickListener: (colorValue: Int , position:Int) -> Unit) {
    fun onClick(colorData: ColorPickerData,position:Int) = clickListener(colorData.value,position)
}