package com.android.demoeditor.customClass


import android.content.Context
import com.android.demoeditor.R
import android.graphics.Typeface
import androidx.core.content.res.ResourcesCompat
import ja.burhanrashid52.photoeditor.PhotoEditor
import ja.burhanrashid52.photoeditor.PhotoEditorView

class CustomPhotoEditor(private val appContext: Context, private val photoEditorView: PhotoEditorView) {




     fun buildeClassReffernce(): PhotoEditor.Builder? {
        val mTextRobotoTf: Typeface? =
            ResourcesCompat.getFont(appContext, R.font.roboto_medium_500)

         val mEmojiTypeFace= ResourcesCompat.getFont(appContext,R.font.emoji_font)!!
//         val mEmojiTypeFace=Typeface.createFromAsset(appContext.assets,"emoji_font.ttf")

        return PhotoEditor
            .Builder(appContext, photoEditorView)
            .setPinchTextScalable(true)
            .setClipSourceImage(true)
            .setDefaultEmojiTypeface(mEmojiTypeFace)
            .setDefaultTextTypeface(mTextRobotoTf)

    }



}