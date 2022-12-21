package com.android.demoeditor.viewModel

import android.annotation.SuppressLint
import android.app.Application
import com.android.demoeditor.R
import com.android.demoeditor.customClass.BitmapHelper
import com.android.demoeditor.customClass.CustomPhotoEditor
import com.android.demoeditor.customClass.ResizePhoto
import com.android.demoeditor.customClass.ResizePhoto.*
import com.android.demoeditor.customClass.UriBitmapUtils
import com.android.demoeditor.data.ColorPickerData
import com.android.demoeditor.data.parcelData.CommonParcelData
import com.android.demoeditor.enums.ActiveNavArgsData
import com.android.demoeditor.screens.PaintingFragmentArgs
import android.graphics.Bitmap
import android.graphics.Color
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import ja.burhanrashid52.photoeditor.OnSaveBitmap
import ja.burhanrashid52.photoeditor.PhotoEditor
import ja.burhanrashid52.photoeditor.shape.ShapeBuilder
import kotlinx.coroutines.*
import kotlin.math.roundToInt

class PaintingScreenViewModel(
    application: Application,
    photoEditorBuilder: CustomPhotoEditor,
   navArgs: PaintingFragmentArgs,
    private val redirectToScreen:()->Unit
) : AndroidViewModel(application) {
    private val TAG = PaintingScreenViewModel::class.java.simpleName

    // private lateinit var photoEditorBuilder: CustomPhotoEditor
//    private lateinit var imgSrc: Bitmap


    private val DEFAULT_BRUSH_SIZE = 60.0f
    private val DEFAUT_OPACITY_PERCENT = 100.0f
    private val DEFAULT_ERASER_SIZE = DEFAULT_BRUSH_SIZE

    @SuppressLint("StaticFieldLeak")
    private val context = getApplication<Application>().applicationContext

    private val backgroundScope = CoroutineScope(Dispatchers.IO + Job())


    private val resources = context.resources

    private val _defaultRecentTab = resources.getString(R.string.brush_text)

    private val _recentTab = MutableLiveData(_defaultRecentTab)

    fun isBrushLayoutVisible(): Boolean {
        return _defaultRecentTab == _recentTab.value
    }

    fun changeRecentTab(tabText: String) {
        _recentTab.value = tabText
        changeDrawingMode()
    }

    /**
     * Img Src
     */

    private val _imgSrc = MutableLiveData<Bitmap>()
    val imgSrcBitmap: LiveData<Bitmap>
        get() = _imgSrc

    /**
     * Image actual height and width
     */
    private val _imgHeight = MutableLiveData<Int>()
    val imgHeight: LiveData<Int>
        get() = _imgHeight


    private val _imgWidth = MutableLiveData<Int>()
    val imgWidth: LiveData<Int>
        get() = _imgWidth


    /**
     * [_recyclerViewsData] is Mutable-Live-Data
     * [recyclerViewsData] is Live-Data
     */
    private val _recyclerViewsData = MutableLiveData(getRecyclerViewData())

    val recyclerViewsData: LiveData<MutableList<ColorPickerData>>
        get() = _recyclerViewsData


    /**
     * Brush Color
     * [_paintColor] is Mutable-Live-Data
     * [paintColor] is Live-Data
     */

    private val _paintColor =
        MutableLiveData(getDefaultColor())
    val paintColor: LiveData<Int>
        get() = _paintColor


    /**
     * Brush Size Val
     */

    private val _brushSize = MutableLiveData(DEFAULT_BRUSH_SIZE)

    val brushSize: LiveData<Float>
        get() = _brushSize


    /**
     * Eraser Size Val
     */

    private val _eraserSize = MutableLiveData(DEFAULT_ERASER_SIZE)

    val eraserSize: LiveData<Float>
        get() = _eraserSize

    /**
     * Opacity Size Val
     */

    private val _opacitySize = MutableLiveData(DEFAULT_ERASER_SIZE)

    val opacitySize: LiveData<Float>
        get() = _opacitySize


    /**
     * ShapeBuilder Builder
     */

    private val shapeBuilder = ShapeBuilder()
        .withShapeColor(_paintColor.value!!)
        .withShapeSize(_brushSize.value!!)



    /**
     * Photo Editor Builder
     */

    private val _photoEditor = MutableLiveData<PhotoEditor>()

    val photoEditor: LiveData<PhotoEditor?>
        get() = _photoEditor


    /**
     * is Img Loading
     */
    private val _isImgLoading=MutableLiveData<Boolean>(false)
    val isImgLoading:LiveData<Boolean>
        get()=_isImgLoading



    /**
     * [init] is Constractor
     */

    init {
//        _imgHeight.value = height
//        _imgWidth.value = width
//        _imgSrc.value = imgSrc

        setUriOrBitmap(navArgs) /** This set bitmap img inside [_imgSrc] and as well as [_imgHeight] and [_imgWidth] */


        _photoEditor.value = photoEditorBuilder.buildeClassReffernce()?.build()
        

    }

    
    // Setting Img Bitmap

    private fun setUriOrBitmap(navArgs: PaintingFragmentArgs) {
        when (navArgs.imgData.availableData) {
            ActiveNavArgsData.BITMAP -> {
                if(BitmapHelper.bitmap !=null) {
                    _isImgLoading.value=true

                    /**
                     * if [BitmapHelper.isResized] is [true] don't resize
                     * else [BitmapHelper.isResized] is [false] do resize
                     */

                    if (BitmapHelper.isResized) {
                        _isImgLoading.value=false
//                        _imgSrc.value = BitmapHelper.bitmap!!
//                        _imgSrc.value!!.config = Bitmap.Config.ARGB_8888 // this is important

                        _imgSrc.value = BitmapHelper.bitmap!!.copy(Bitmap.Config.ARGB_8888, true)

                        set_Img_Height_Width(_imgSrc.value!!)

                    }else{

                        _isImgLoading.value = true


                        val resizePhoto = ResizePhoto(context, BitmapHelper.bitmap!!, true, RESOLUTION.HD_720P)

                        backgroundScope.launch {

                            val resizeBitmap = resizePhoto.execute()
                            withContext(Dispatchers.Main) {

                                _isImgLoading.value=false

                                _imgSrc.value = resizeBitmap
                                _imgSrc.value!!.config = Bitmap.Config.ARGB_8888 // this is important
                                set_Img_Height_Width(_imgSrc.value!!)
                            }
                        }

                    }



                }else{
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



    private fun set_Img_Height_Width(bitmap: Bitmap){
        _imgHeight.value = bitmap.height
        _imgWidth.value = bitmap.width
    }
    
    
    // -----xxx-----
    

    var defaultDrawingMode: () -> Unit = this::startDraw

    private fun changeDrawingMode() {
        defaultDrawingMode = if (_recentTab.value == _defaultRecentTab) {
            this::startDraw
        } else {
            this::startEraser
        }

        defaultDrawingMode()

    }

    private fun startDraw() {


        _photoEditor.value?.setBrushDrawingMode(true)

        shapeBuilder.withShapeSize(_brushSize.value!!)

        _photoEditor.value?.setShape(shapeBuilder)


    }


    private fun startEraser() {

        shapeBuilder.withShapeSize(_eraserSize.value!!)
        _photoEditor.value?.setShape(shapeBuilder)
        _photoEditor.value?.brushEraser()

    }


    private fun getRecyclerViewData(): MutableList<ColorPickerData> {
        val colorList = context.resources.getStringArray(R.array.colors)

        val list = mutableListOf<ColorPickerData>()

        for ((i, item) in colorList.withIndex()) {
            val colorAsInt = Color.parseColor(item)
            val colorData = ColorPickerData(colorAsInt)
            list.add(i, colorData)
        }

        return list
    }


    private fun getDefaultColor(): Int {

     return   getRecyclerViewData()[0].value

    }

    fun setBrushColor(colorId: Int) {

        _paintColor.value = colorId
        shapeBuilder.withShapeColor(colorId)
    }

    fun setBrushSize(size: Float) {
        _brushSize.value = size
        shapeBuilder.withShapeSize(size)

    }

    fun setOpacity(opacity: Float) {
        _opacitySize.value = opacity
    }

    fun setEraserSize(size: Float) {
        _eraserSize.value = size
        shapeBuilder.withShapeSize(size)

    }


    fun savePhotoAsBitmap(

        navigateToMainScreen: (Bitmap) -> Unit
    ) {
        _photoEditor.value?.saveAsBitmap(object : OnSaveBitmap {
            override fun onBitmapReady(saveBitmap: Bitmap?) {

                if (saveBitmap != null) {

                    _imgSrc?.value = saveBitmap!!
                    val resized = Bitmap.createScaledBitmap(saveBitmap, _imgWidth.value!!, _imgHeight.value!!, true)

                      navigateToMainScreen(resized)
                }
            }

            override fun onFailure(e: Exception?) {
                Log.i(TAG, "${e?.message}")
                throw Exception("Photo Not Saved as Bitmap in PaintingScreenViewModel CLASS")
            }
        })

    }



    /**
     * Transform Live-Data
     * This Live-Data direct connect with XML data binding
     */

    val isDefaultTabRecentTab=Transformations.map(_recentTab) {
        it == _defaultRecentTab
    }

    val stringBrushSize = Transformations.map(brushSize){
       it.roundToInt().toString()
    }

    val stringEraserSize = Transformations.map(eraserSize){
        it.roundToInt().toString()
    }

    /**
     * End Transform Live-Data
     */



}