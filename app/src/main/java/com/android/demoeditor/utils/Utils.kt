package com.android.demoeditor.utils


import android.content.Context
import com.android.demoeditor.customView.RulerView
import com.android.demoeditor.viewModel.AdjustScreenViewModel
import android.graphics.*
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.Drawable
import android.net.Uri
import android.os.Build
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.widget.AppCompatSpinner
import androidx.databinding.BindingAdapter
import com.bumptech.glide.Glide
import ja.burhanrashid52.photoeditor.PhotoEditorView
import jp.co.cyberagent.android.gpuimage.GPUImage
import jp.co.cyberagent.android.gpuimage.filter.GPUImageFilter
import kotlinx.coroutines.*

import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions


//@BindingAdapter("LoadBitmapImg")
//fun ImageView.convert(filterData: FilterNavItemData) {
//
//
//    val filterBitmap = when (filterData.filterName) {
//
//
//        "Struck" -> FilterPack.getAweStruckVibeFilter(context)
//        "Clarendon" -> FilterPack.getClarendon(context)
//        "OldMan" -> FilterPack.getOldManFilter(context)
//        "Mars" -> FilterPack.getMarsFilter(context)
//        "Rise" -> FilterPack.getRiseFilter(context)
//        "April" -> FilterPack.getAprilFilter(context)
//        "Amazon" -> FilterPack.getAmazonFilter(context)
//        "Starlit" -> FilterPack.getStarLitFilter(context)
//        "Whisper" -> FilterPack.getNightWhisperFilter(context)
//        "Lime" -> FilterPack.getLimeStutterFilter(context)
//        "Haan" -> FilterPack.getLimeStutterFilter(context)
//        "Bluemess" -> FilterPack.getBlueMessFilter(context)
//        "Adele" -> FilterPack.getAdeleFilter(context)
//        "Cruz" -> FilterPack.getCruzFilter(context)
//        "Metropolis" -> FilterPack.getMetropolis(context)
//        "Audrey" -> FilterPack.getAudreyFilter(context)
//        else -> return
//    }
//
//    CoroutineScope(Dispatchers.Default).launch {
//
//        val mutableBimapImg = filterData.filterImg.copy(Bitmap.Config.ARGB_8888, true)
////            val bitmap=filterData.filterImg
//
//        val outPutBitmap =
//            filterBitmap?.processFilter(mutableBimapImg)
//        withContext(Dispatchers.Main) {
//            setImageBitmap(outPutBitmap)
//
//        }
//
//
//    }
//
//
//}


//@BindingAdapter("SetFilterText")
//fun TextView.setFiltertext(filterDataText: String) {
//    text = filterDataText
//}
//
//
//fun ImageView.getStringData(imgResId: Int): String {
//    return resources.getString(imgResId)
//}


/**
 * This method is two times called
 * 1. In Fragment_Adjust XML
 * 2. In AdjustEditFragment.kt
 */


//@BindingAdapter("ResetSliderMinmaxAgain")
//fun Slider.resetSliderValAgain(adjustViewModel:AdjustScreenViewModel) {
//
//    //Log.i("-->>","Slider min "+adjustViewModel.getMinVal().toString()+"Slider max  "+adjustViewModel?.getMaxVal().toString())
//    /**
//     * Setting Slider ->  "Minimum" , "Maximum" , "Initial" value
//     */
//
//
//    valueTo = adjustViewModel.getMaxVal()
//    valueFrom = adjustViewModel.getMinVal()
//    value = adjustViewModel.getSliderInitVal()
//    stepSize= adjustViewModel.getSliderStepVal()!!
//
//
//}

/**
 * This method is two times called
 * 1. In Fragment_Adjust XML
 * 2. In AdjustEditFragment.kt
 */

@BindingAdapter("ResetSliderMinmaxAgain")
fun RulerView.resetSliderValAgain(adjustViewModel: AdjustScreenViewModel) {

    //Log.i("-->>","Slider min "+adjustViewModel.getMinVal().toString()+"Slider max  "+adjustViewModel?.getMaxVal().toString())
    /**
     * Setting Slider ->  "Minimum" , "Maximum" , "Initial" value
     */


    max = adjustViewModel.getMaxVal()
    min = adjustViewModel.getMinVal()
    value = adjustViewModel.getSliderInitVal()
    step = adjustViewModel.getSliderStepVal()!!


}


@BindingAdapter("ImgTransitionName")
fun PhotoEditorView.setImgTransitionName(transitionName: String) {
    source.transitionName = transitionName

}

@BindingAdapter("setBitmapImg")
fun ImageView.setBitmapImg(bitmap: Bitmap) {

    setImageBitmap(bitmap)
}

@BindingAdapter("setBitmapImgView")
fun ImageView.setBitmapImgView(adjustViewModel: AdjustScreenViewModel) {


    setImageBitmap(adjustViewModel.imgBitmap.value)


}


@BindingAdapter(value = ["setFilter", "drawableImg"], requireAll = false)
fun ImageView.loadImg(filter: GPUImageFilter, drawable: Drawable) {

    val bitmapImg = (drawable as BitmapDrawable).bitmap

    CoroutineScope(Dispatchers.Default).launch {

        var gpuImage: GPUImage? = GPUImage(context)
        gpuImage?.setFilter(filter)
        val bitmap = gpuImage?.getBitmapWithFilterApplied(bitmapImg);

        withContext(Dispatchers.Main) {
            setImageBitmap(bitmap)

            gpuImage = null
        }
    }


}


//@BindingAdapter("convertToEmojiString")
//fun TextView.convertToEmojiString(emojiUnicode:Int){
//    text=getEmojiByUnicode(emojiUnicode)
//}
//
//
//private fun getEmojiByUnicode(unicode: Int): String {
//    return String(Character.toChars(unicode))
//}


//fun Context.isDarkThemeOn(): Boolean {
//    return resources.configuration.uiMode and
//            Configuration.UI_MODE_NIGHT_MASK == UI_MODE_NIGHT_YES
//}


//@BindingAdapter("visibility")
//fun setVisibility(view: View, visible: Boolean) {
//    view.visibility = if (visible) View.VISIBLE else View.GONE
//}

fun Context.dpToPx(dp: Int): Int {
    return (dp * resources.displayMetrics.density).toInt()
}

fun Context.pxToDp(px: Int): Int {
    return (px / resources.displayMetrics.density).toInt()
}

@BindingAdapter("setTypeFaceFromXml")
fun TextView.setTypeFaceFromXml(t: Typeface) {
    typeface = t
}

@BindingAdapter("loadImgWithGlide")
fun  ImageView.loadImgWithGlide(  uri: Uri) {

    Glide.with( context)
        .load(uri)
        .transition(DrawableTransitionOptions().crossFade( ))
        .into(this);
}






fun AppCompatSpinner.changeIconColor(  color:Int) {


    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
         this.background.colorFilter = BlendModeColorFilter(color, BlendMode.SRC_ATOP)
    } else {
        this.background.setColorFilter(resources.getColor(color), PorterDuff.Mode.SRC_ATOP);
    }
}




