package com.android.demoeditor.repository

import android.content.Context
import com.android.demoeditor.R
import com.android.demoeditor.data.CollageEditItemData
import com.android.demoeditor.data.ColorPickerData
import com.android.demoeditor.data.LayoutSelectorRecyclerViewItemData
import com.android.demoeditor.recyclerViews.CollageIconType
import android.graphics.Color
import android.net.Uri
import androidx.core.content.ContextCompat

class CollageSelectorRepository(private val context: Context) {

    private val TAG = this::class.java.simpleName

    /**
     * [dataForRecyclerViewForLayoutSelector] is for creating data for [CollageNavbarAdapter] recycler-view
     * @return [MutableList] of [LayoutSelectorRecyclerViewItemData]
     */
    fun dataForRecyclerViewForLayoutSelector(listOfUris: List<Uri>): MutableList<LayoutSelectorRecyclerViewItemData> {
        val listOfPuzzleLayout = com.android.demoeditor.customView.layoutsForColleagueView.PuzzleUtils.getPuzzleLayouts(listOfUris.size)
        val arr = mutableListOf<LayoutSelectorRecyclerViewItemData>()
        for (i in 0 until listOfPuzzleLayout.size) {
            val data = LayoutSelectorRecyclerViewItemData(layout = listOfPuzzleLayout[i])
            arr.add(data)
        }
        return arr
    }


    /**
     * [dataForBackgroundColor] is getting color data from resource
     * @return [MutableList] of [ColorPickerData]
     */
    fun dataForBackgroundColor(): MutableList<ColorPickerData> {

        val colorList = context.resources.getStringArray(R.array.colors)

        val list = mutableListOf<ColorPickerData>()

        for ((i, item) in colorList.withIndex()) {
            val colorAsInt = Color.parseColor(item)
            val colorData = ColorPickerData(colorAsInt)
            list.add(i, colorData)
        }

        val c = ColorPickerData(Color.TRANSPARENT)
        list.add(c)
        list.reverse()

        return list
    }



    // this should delete
    /**
     * [dataForEditViewPagerRecyclerView] is for [CollageEditViewPagerFragment] recycler-view
     * @return [MutableList] of [CollageEditItemData]
     */
    fun dataForEditViewPagerRecyclerView(): MutableList<CollageEditItemData> {
        return mutableListOf(
            getData(
                R.drawable.ic_icon_rotate_right_90_degree,
                CollageIconType.ROTATE,
                R.string.Rotate_Collage_Edit
            ),
            getData(
                R.drawable.ic_baseline_flip_24,
                CollageIconType.FLIP,
                R.string.Flip_Collage_Edit
            ),
            getData(
                R.drawable.ic_mirror_vertical_24,
                CollageIconType.MIRROR,
                R.string.Mirror_Collage_Edit
            ),

            )
    }

    /**
     * [getData] is for getting data from Resource
     * @return [CollageEditItemData] data
     */
    private fun getData(
        iconId: Int,
        iconType: CollageIconType,
        nameId: Int
    ): CollageEditItemData {
        val iconDrawable = ContextCompat.getDrawable(context, iconId)
        val iconText = context.getText(nameId).toString()
        return CollageEditItemData(icon = iconDrawable!!, iconType = iconType, iconName = iconText)
    }


}