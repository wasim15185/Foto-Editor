package com.android.demoeditor.data

import com.xiaopo.flying.puzzle.PuzzleLayout
import java.util.*

data class LayoutSelectorRecyclerViewItemData(
    val id: String = UUID.randomUUID().toString(),
//    val listOfBitmap: List<Bitmap>,
    val layout: PuzzleLayout )
