package com.android.demoeditor.repository.viewPager

import com.android.demoeditor.data.EmojiData

class EmojiViewPagerRepository(private val emojiList:Array<String>) {

     fun getEmojiData():MutableList<EmojiData>{



        val mutableList = mutableListOf<EmojiData>()
        for ( i in emojiList.indices){

            val convertEmoji=convertEmojiToUnicode(emojiList[i])!!
            val emojiData=EmojiData(convertEmoji)
            mutableList.add(i,emojiData)
        }
        return mutableList
    }

   private fun convertEmojiToUnicode(emoji:String):String{

       return try {
           val convertEmojiToInt = emoji.substring(2).toInt(16)
           getEmojiByUnicode(convertEmojiToInt)
       } catch (e: NumberFormatException) {
           ""
       }

   }


    private fun getEmojiByUnicode(unicode: Int): String {
        return String(Character.toChars(unicode))
    }


}