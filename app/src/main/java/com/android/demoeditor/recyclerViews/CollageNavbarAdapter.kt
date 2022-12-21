package com.android.demoeditor.recyclerViews

import android.content.Context
import com.android.demoeditor.data.LayoutSelectorRecyclerViewItemData
import com.android.demoeditor.data.PuzzleData
import com.android.demoeditor.databinding.CollageNavbarItemBinding
import com.android.demoeditor.utils.getPuzzleData
import android.graphics.Color
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView

class CollageNavbarAdapter(
    private val listImgSize:Int,
    private val context: Context,
    var clickListener: CollageNavbarItemListener? = null,
) : ListAdapter<LayoutSelectorRecyclerViewItemData, CollageNavbarAdapter.ViewHolder>(
    CollageDiffCallback()
) {

    private val TAG = this::class.java.simpleName

    private var selectedPosition= 0

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)

        val bindView = CollageNavbarItemBinding.inflate(layoutInflater, parent, false)
        return ViewHolder(bindView, listImgSize)

    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        val item = getItem(position)
        holder.bind(item!!, clickListener)

        if (selectedPosition==position){
            holder.binding.puzzleView.lineColor = Color.RED

        }else{
            holder.binding.puzzleView.lineColor = Color.GRAY
        }

    }

    fun  setSingleSelection(position: Int) {

        if (position==RecyclerView.NO_POSITION) return

        notifyItemChanged(selectedPosition)
        selectedPosition=position
        notifyItemChanged(selectedPosition)

    }



    class ViewHolder(val binding: CollageNavbarItemBinding, val listImgSize:Int) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(
            item: LayoutSelectorRecyclerViewItemData,
            clickListener: CollageNavbarItemListener?
        ) {
            binding.collageNavbarItemData = item
            binding.position = absoluteAdapterPosition
            binding.clickListener = clickListener

            val layoutData=getPuzzleData(item.layout,listImgSize)
            binding.layoutInfoData=layoutData


            binding.puzzleView.isNeedDrawLine = true;
            binding.puzzleView.isNeedDrawOuterLine = true;
            binding.puzzleView.isTouchEnable = false;
            binding.puzzleView.lineColor = Color.GRAY


            binding.puzzleView.setPuzzleLayout(item.layout);



            // When You Using 'Binding Adapter' inside 'Recycler-View' must execute -> "binding.executePendingBindings()"
            binding.executePendingBindings()
        }
    }

}

/**
 * [CollageDiffCallback] is DiffCallBack
 */
class CollageDiffCallback : DiffUtil.ItemCallback<LayoutSelectorRecyclerViewItemData>() {
    override fun areItemsTheSame(
        oldItem: LayoutSelectorRecyclerViewItemData,
        newItem: LayoutSelectorRecyclerViewItemData
    ): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(
        oldItem: LayoutSelectorRecyclerViewItemData,
        newItem: LayoutSelectorRecyclerViewItemData
    ): Boolean {
        return oldItem == newItem
    }
}


class CollageNavbarItemListener(val clickListener: (layoutInfoData: PuzzleData, position: Int) -> Unit) {

    fun click(layoutInfoData: PuzzleData,position: Int)=clickListener(layoutInfoData,position)

}