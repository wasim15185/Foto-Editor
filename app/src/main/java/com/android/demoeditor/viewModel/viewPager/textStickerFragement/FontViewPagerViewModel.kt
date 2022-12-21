package com.android.demoeditor.viewModel.viewPager.textStickerFragement

import android.annotation.SuppressLint
import android.app.Application
import androidx.lifecycle.AndroidViewModel

class FontViewPagerViewModel(
    application: Application
) : AndroidViewModel(application) {


    private val TAG = FontViewPagerViewModel::class.java.simpleName


    @SuppressLint("StaticFieldLeak")
    private val context = getApplication<Application>().applicationContext


//    private val _recyclerViewData = MutableLiveData<MutableList<FontItemData>>(getFontItemList())
//    val recyclerViewData: LiveData<MutableList<FontItemData>>
//        get() = _recyclerViewData
//
//    private fun getFontItemList(): MutableList<FontItemData> {
//        return mutableListOf(
//
//            defaultFontsDetails,  // Here Always 'Default Font' will appear
//
//            FontItemData(
//                getTypeface(R.font.aclonica),
//                getText(R.string.font_name_alconia)
//            ),
//
//            FontItemData(
//                getTypeface(R.font.acme),
//                getText(R.string.font_name_acme)
//            ),
//
//            FontItemData(
//                getTypeface(R.font.akronim),
//                getText(R.string.font_name_akronim)
//            ),
//
//            FontItemData(
//                getTypeface(R.font.aclonica),
//                getText(R.string.font_name_alex)
//            ),
//
//            FontItemData(
//                getTypeface(R.font.architects_daughter),
//                getText(R.string.font_name_architects_daughter)
//            ),
//
//            FontItemData(
//                getTypeface(R.font.atomic_age),
//                getText(R.string.font_name_atomic_age)
//            ),
//
//
//            FontItemData(
//                getTypeface(R.font.biryani_extralight),
//                getText(R.string.font_name_biryani_extralight)
//            ),
//
//            FontItemData(
//                getTypeface(R.font.chewy),
//                getText(R.string.font_name_chewy)
//            ),
//
//            FontItemData(
//                getTypeface(R.font.courgette),
//                getText(R.string.font_name_courgette)
//            ),
//
//
//            FontItemData(
//                getTypeface(R.font.jolly_lodger),
//                getText(R.string.font_name_alconia)
//            ),
//
//            FontItemData(
//                getTypeface(R.font.kalam_light),
//                getText(R.string.font_name_kalam_light)
//            ),
//
//            FontItemData(
//                getTypeface(R.font.macondo_swash_caps),
//                getText(R.string.font_name_macondo_swash_caps)
//            ),
//
//
//            FontItemData(
//                getTypeface(R.font.meie_script),
//                getText(R.string.font_name_meie_script)
//            ),
//
//
//            )
//    }
//
//
//    private fun getTypeface(id: Int) = ResourcesCompat.getFont(context, id)!!
//
//    private fun getText(textId: Int): String =
//        context.getString(textId).replaceFirstChar { // it: Char
//            if (it.isLowerCase())
//                it.titlecase(Locale.getDefault())
//            else
//                it.toString()
//        }


}