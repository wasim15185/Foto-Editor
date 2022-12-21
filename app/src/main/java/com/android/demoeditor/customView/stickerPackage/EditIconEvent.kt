package com.android.demoeditor.customView.stickerPackage

import android.view.MotionEvent

class EditIconEvent(private val listener:(stickerView: com.android.demoeditor.customView.stickerPackage.StickerView?)->Unit) :
    com.android.demoeditor.customView.stickerPackage.StickerIconEvent {
    override fun onActionDown(stickerView: com.android.demoeditor.customView.stickerPackage.StickerView?, event: MotionEvent?) {

    }

    override fun onActionMove(stickerView: com.android.demoeditor.customView.stickerPackage.StickerView?, event: MotionEvent?) {
    }

    override fun onActionUp(stickerView: com.android.demoeditor.customView.stickerPackage.StickerView?, event: MotionEvent?) {
        listener(stickerView)
     }
}