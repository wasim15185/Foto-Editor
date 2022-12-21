package com.android.demoeditor.viewModel

import android.annotation.SuppressLint
import android.app.Application
import android.content.Context
import com.android.demoeditor.R
import com.android.demoeditor.customClass.BitmapHelper
import com.android.demoeditor.customClass.ResizePhoto
import com.android.demoeditor.customClass.ResizePhoto.*
import com.android.demoeditor.customClass.UriBitmapUtils
import com.android.demoeditor.customView.stickerPackage.TextSticker
import com.android.demoeditor.data.ColorPickerData
import com.android.demoeditor.data.FontItemData
import com.android.demoeditor.data.StickerDetail
import com.android.demoeditor.data.ViewPagerData
import com.android.demoeditor.data.parcelData.CommonParcelData
import com.android.demoeditor.databinding.FragmentTextStickerBinding
import com.android.demoeditor.enums.ActiveNavArgsData
import com.android.demoeditor.screens.TextStickerFragmentArgs
import com.android.demoeditor.screens.viewPager.textStickerFragment.ColorViewPagerFragment
import com.android.demoeditor.screens.viewPager.textStickerFragment.FontViewPagerFragment
import android.graphics.Bitmap
import android.graphics.Color
import android.graphics.Typeface
import android.graphics.drawable.Drawable
import android.text.Layout
import android.util.Log
import androidx.core.content.res.ResourcesCompat
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import kotlinx.coroutines.*
import java.util.*

@SuppressLint("StaticFieldLeak")
class TextStickerViewModel(
    application: Application,
    navArgs: TextStickerFragmentArgs,
    private val textStickerBind: FragmentTextStickerBinding,
    private val redirectToScreen:()->Unit
) : AndroidViewModel(application) {

    companion object {
        private val TAG: String = TextStickerViewModel::class.java.simpleName
    }

    private val backgroundScope = CoroutineScope(Dispatchers.IO + Job())

    private val _imgSrc = MutableLiveData<Bitmap>()
    val imgSrc: LiveData<Bitmap>
        get() = _imgSrc


    private val context = getApplication<Application>().applicationContext


    /**
     * [_stickersInfoList] is for Array of sticker Info
     */

    private val _stickersInfoList = MutableLiveData<MutableList<StickerDetail>>()
    val stickerInfoList: LiveData<MutableList<StickerDetail>>
        get() = _stickersInfoList


    /**
     * [_currentSticker] for current sticker
     */
    private val _currentSticker = MutableLiveData<com.android.demoeditor.customView.stickerPackage.Sticker>()
    val currentSticker: LiveData<com.android.demoeditor.customView.stickerPackage.Sticker>?
        get() {
            if (_currentSticker.value != null) {
                return _currentSticker
            }
            return null
        }

    /**
     * [colorPositionInRecyclerView] live for color position in Recycler View
     */
    val colorPositionInRecyclerView = Transformations.map(_currentSticker) { currentSticker ->
        val convertToTextSticker = currentSticker as com.android.demoeditor.customView.stickerPackage.TextSticker
        val element = _stickersInfoList.value?.find { it.id == convertToTextSticker.id }
        return@map element?.colorPositionInRecyclerView

    }

    /**
     * [fontPositionInRecyclerView] live for font position in Recycler View
     */

    val fontPositionInRecyclerView = Transformations.map(_currentSticker) { currentSticker ->
        val convertToTextSticker = currentSticker as com.android.demoeditor.customView.stickerPackage.TextSticker
        val element = _stickersInfoList.value?.find { it.id == convertToTextSticker.id }
        return@map element?.fontPositionInRecyclerView

    }

    /**
     *  [_fontViewPager_RecyclerViewData] is data for RecyclerView for "FontViewPagerFragment"
     */
    private val _fontViewPager_RecyclerViewData = MutableLiveData<MutableList<FontItemData>>()
    val fontViewPager_RecyclerViewData: LiveData<MutableList<FontItemData>>
        get() = _fontViewPager_RecyclerViewData


    /**
     * [_colorViewPager_RecyclerViewData] is Mutable-Live-Data
     * [colorViewPager_RecyclerViewData] is Live-Data
     */
    private val _colorViewPager_RecyclerViewData = MutableLiveData<MutableList<ColorPickerData>>()

    val colorViewPager_RecyclerViewData: LiveData<MutableList<ColorPickerData>>
        get() = _colorViewPager_RecyclerViewData

    /**
     * [_viewpagerData] LiveData
     */
    private val _viewpagerData = MutableLiveData<List<ViewPagerData>>()
    val viewPagerData: LiveData<List<ViewPagerData>>
        get() = _viewpagerData


    /**
     *  [defaultFontDetail] is Default font details
     *  [defaultColor] is Default font color
     */

    private var defaultFontDetail: FontItemData
    private var defaultColor: Int? = null
    private val initialPosition_of_font_in_recyclerView = 0
    private val initialPosition_of_color_in_recyclerView = getInitialPositionOfColorInRecyclerView()


    /**
     * is Img Loading
     */
    private val _isImgLoading = MutableLiveData<Boolean>(false)
    val isImgLoading: LiveData<Boolean>
        get() = _isImgLoading


    init {
//        _imgSrc.value = imgSrcBitmap
        setUriOrBitmap(navArgs)
        /** This set bitmap img inside [_imgSrc]   */

        _viewpagerData.value = getListOfViewPagerData()
        _stickersInfoList.value = mutableListOf()


        _fontViewPager_RecyclerViewData.value = getFontItemList()
        _colorViewPager_RecyclerViewData.value = getColorListForRecyclerViewData()

        defaultFontDetail =
            _fontViewPager_RecyclerViewData.value!![initialPosition_of_font_in_recyclerView]
        defaultColor =
            _colorViewPager_RecyclerViewData.value!![initialPosition_of_color_in_recyclerView].value


    }

    /** bitmap set inside _imgSrc.value  */

    private fun setUriOrBitmap(navArgs: TextStickerFragmentArgs) {
        when (navArgs.imgData.availableData) {
            ActiveNavArgsData.BITMAP -> {
                if (BitmapHelper.bitmap != null) {

                    _isImgLoading.value = true

                    /**
                     * if [BitmapHelper.isResized] is [true] don't resize
                     * else [BitmapHelper.isResized] is [false] do resize
                     */


                    if (BitmapHelper.isResized) {
                        _imgSrc.value = BitmapHelper.bitmap
                        _isImgLoading.value = false

                    } else {
                        val resizePhoto =
                            ResizePhoto(context, BitmapHelper.bitmap!!, true, RESOLUTION.HD_720P)
                        backgroundScope.launch {
                            val resizeBitmap = resizePhoto.execute()
                            withContext(Dispatchers.Main) {
                                _imgSrc.value = resizeBitmap
                                _isImgLoading.value = false
                            }
                        }
                    }


                } else {

                    // Navigate screen
                    redirectToScreen()

                }

            }

            ActiveNavArgsData.URI -> {
                _isImgLoading.value = true

                convertUriToBitmap(navArgs.imgData)
            }

        }

    }


    private fun convertUriToBitmap(imgData: CommonParcelData) {
        if (imgData.uri != null) {
            UriBitmapUtils(imgData.uri, context, backgroundScope) {
                val resizePhoto = ResizePhoto(context, it, true, RESOLUTION.HD_720P)

                 backgroundScope.launch {
                    val resizeBitmap = resizePhoto.execute()

                    withContext(Dispatchers.Main) {
                        _isImgLoading.value = false
                        _imgSrc.value = resizeBitmap

                    }
                }

            }

        } else {
            throw Exception("Uri not available")
        }
    }

    /** -----------------xxx------------------*/

    /**
     * this fun change value of [_isImgLoading]
     */
    fun setLoadingState(value: Boolean) {
        _isImgLoading.value = value
    }


    private fun getInitialPositionOfColorInRecyclerView(): Int {

        val list = getFontItemList()

        return list.indexOfLast {
            it.name == context.getString(R.string.Default)
        }


    }


    /**
     * [getListOfViewPagerData] getList data of [ViewPagerData]
     */
    private fun getListOfViewPagerData(): MutableList<ViewPagerData> {

        return mutableListOf(
            ViewPagerData(
                ColorViewPagerFragment.injectParentStickerView(this),
                getString(R.string.colorViewPager_name)
            ),

            ViewPagerData(
                FontViewPagerFragment.injectParentStickerView(this),
                getString(R.string.fontViewPager_name)
            )

        )

    }

    /**
     * [getString] give string from string resource
     * @param stringId [Int]
     * @return [String]
     */
    private fun getString(stringId: Int): String = context.getString(stringId)

    /**
     * [updateCurrentSticker]
     */

    fun updateCurrentSticker(sticker: com.android.demoeditor.customView.stickerPackage.Sticker) {

        Log.d(TAG, " called ")

        _currentSticker.value = sticker

        val textSticker = sticker as com.android.demoeditor.customView.stickerPackage.TextSticker

        val isStickerInList = checkIsStickerInsideList(textSticker)

        //TODO:- if [isStickerInList] is "False" add Sticker in stickerList

        isStickerInList?.let {
            if (!it) addOneStickerInList(textSticker)
        }


        Log.d(TAG, " updated list is :- ${_stickersInfoList.value} ")

    }


    private fun checkIsStickerInsideList(textSticker: com.android.demoeditor.customView.stickerPackage.TextSticker): Boolean? {
        return _stickersInfoList.value?.any { it.id == textSticker.id }
    }

    /**
     * [addOneStickerInList] is for adding [TextSticker] inside list
     * @param textSticker
     */

    fun addOneStickerInList(textSticker: com.android.demoeditor.customView.stickerPackage.TextSticker) {

        val stickerDetail = StickerDetail(
            id = textSticker.id,
            text = textSticker.text!!,
            color = textSticker.textColor,
            typefaceName = textSticker.typeFaceName,
            colorPositionInRecyclerView = initialPosition_of_color_in_recyclerView,
            fontPositionInRecyclerView = initialPosition_of_font_in_recyclerView
        )

        _stickersInfoList.value?.add(stickerDetail)
    }

    /**
     *  [deleteOneStickerFromList] find the sticker and delete it from the list
     */

    fun deleteOneStickerFromList(textSticker: com.android.demoeditor.customView.stickerPackage.TextSticker) {
        _stickersInfoList.value = _stickersInfoList.value?.filter {
            it.id != textSticker.id
        }?.toMutableList()

        Log.d(TAG, "After Delete 1 Sticker from List is : ${_stickersInfoList.value}")

    }

    /**
     * [updateOneStickerInList] find the sticker and update in the list
     */

    fun updateOneStickerInList(textSticker: com.android.demoeditor.customView.stickerPackage.TextSticker) {
        val element = _stickersInfoList.value?.find { it.id == textSticker.id }
        element?.apply {
            text = textSticker.text.toString()
            color = textSticker.textColor
            typefaceName = textSticker.typeFaceName
        }

        Log.d(
            TAG,
            "updateOneStickerInList: After Update One Element in list ${_stickersInfoList.value}"
        )
    }


    /**
     * [getDefaultFont] is for getting default font
     * @param context [Context]
     * @return [FontItemData]
     */

    private fun getDefaultFont(context: Context): FontItemData {

        val defaultFont_id = R.font.roboto_medium_500
        val typeFace = ResourcesCompat.getFont(context, defaultFont_id)!!
        return FontItemData(typeFace, context.getString(R.string.Default))

    }


    private fun getFontItemList(defaultFontDetail: FontItemData = getDefaultFont(context)): MutableList<FontItemData> {
        return mutableListOf(

            defaultFontDetail,  // Here Always 'Default Font' will appear

            FontItemData(
                getTypeface(R.font.aclonica),
                getText(R.string.font_name_alconia)
            ),

            FontItemData(
                getTypeface(R.font.acme),
                getText(R.string.font_name_acme)
            ),

            FontItemData(
                getTypeface(R.font.akronim),
                getText(R.string.font_name_akronim)
            ),

            FontItemData(
                getTypeface(R.font.aclonica),
                getText(R.string.font_name_alex)
            ),

            FontItemData(
                getTypeface(R.font.architects_daughter),
                getText(R.string.font_name_architects_daughter)
            ),

            FontItemData(
                getTypeface(R.font.atomic_age),
                getText(R.string.font_name_atomic_age)
            ),


            FontItemData(
                getTypeface(R.font.biryani_extralight),
                getText(R.string.font_name_biryani_extralight)
            ),

            FontItemData(
                getTypeface(R.font.chewy),
                getText(R.string.font_name_chewy)
            ),

            FontItemData(
                getTypeface(R.font.courgette),
                getText(R.string.font_name_courgette)
            ),


            FontItemData(
                getTypeface(R.font.jolly_lodger),
                getText(R.string.font_name_alconia)
            ),

            FontItemData(
                getTypeface(R.font.kalam_light),
                getText(R.string.font_name_kalam_light)
            ),

            FontItemData(
                getTypeface(R.font.macondo_swash_caps),
                getText(R.string.font_name_macondo_swash_caps)
            ),


            FontItemData(
                getTypeface(R.font.meie_script),
                getText(R.string.font_name_meie_script)
            ),


            )
    }


    private fun getTypeface(id: Int) = ResourcesCompat.getFont(context, id)!!

    private fun getText(textId: Int): String =
        context.getString(textId).replaceFirstChar { // it: Char
            if (it.isLowerCase())
                it.titlecase(Locale.getDefault())
            else
                it.toString()
        }


    /**
     * [getColorListForRecyclerViewData] for getting list color for [_colorViewPager_RecyclerViewData]
     * @return [MutableList<ColorPickerData>]
     */

    private fun getColorListForRecyclerViewData(): MutableList<ColorPickerData> {
        val colorList = context.resources.getStringArray(R.array.colors)

        val list = mutableListOf<ColorPickerData>()

        for ((i, item) in colorList.withIndex()) {
            val colorAsInt = Color.parseColor(item)
            val colorData = ColorPickerData(colorAsInt)
            list.add(i, colorData)
        }

        return list
    }


    /**
     * [createNewSticker] create new Text-Sticker
     * @param textStr
     * @return [TextSticker]
     */
    fun createNewSticker(textStr: String): com.android.demoeditor.customView.stickerPackage.TextSticker {
        return com.android.demoeditor.customView.stickerPackage.TextSticker(context).apply {
            drawable = getDrawable(R.drawable.sticker_emoji_transparent_background)
            text = textStr
            setTextAlign(Layout.Alignment.ALIGN_CENTER)
            resizeText()
            setTypeface(defaultFontDetail.font, defaultFontDetail.name)
            textColor = defaultColor!!

        }

    }

    @SuppressLint("UseCompatLoadingForDrawables")
    private fun getDrawable(id: Int): Drawable {
        return context.getDrawable(id)!!
    }


    fun changeStickerColor(color: Int, positionInRecyclerView: Int) {

        if (_currentSticker.value == null) return

        val textSticker = _currentSticker.value as com.android.demoeditor.customView.stickerPackage.TextSticker
        val element = _stickersInfoList.value?.find { it.id == textSticker.id }
        // Updating Color in List
        element?.color = color
        // Updating recycler view position
        element?.colorPositionInRecyclerView = positionInRecyclerView
        // Updating sticker TextColor
        textSticker.textColor = color

        val sticker = textSticker as com.android.demoeditor.customView.stickerPackage.Sticker
        updateStickerView(sticker)

    }

    fun updateStickerView(sticker: com.android.demoeditor.customView.stickerPackage.Sticker) {
        textStickerBind.textStickerView.apply {
            replace(sticker)
            invalidate()
        }
    }


    fun changeTypeFace(typeface: Typeface, typefaceName: String, positionInRecyclerView: Int) {

        if (_currentSticker.value == null) return


        val textSticker = _currentSticker.value as com.android.demoeditor.customView.stickerPackage.TextSticker
        val element = _stickersInfoList.value?.find { it.id == textSticker.id }
        // Updating TypeFace Name in List
        element?.typefaceName = typefaceName
        // Updating recycler view position
        element?.fontPositionInRecyclerView = positionInRecyclerView

        // Updating sticker TextColor
        textSticker.setTypeface(typeface, typefaceName)

        val sticker = textSticker as com.android.demoeditor.customView.stickerPackage.Sticker
        updateStickerView(sticker)
    }


}