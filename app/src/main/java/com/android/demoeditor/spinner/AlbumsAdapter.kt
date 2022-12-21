package com.android.demoeditor.spinner

import android.content.Context
import com.android.demoeditor.data.AlbumData
import com.android.demoeditor.databinding.AlbumSpinnerItemBinding
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.ImageView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions

class AlbumsAdapter(context: Context,albumList:List<AlbumData>) :ArrayAdapter<AlbumData> (context,0,albumList){

    private val TAG=AlbumsAdapter::class.java.simpleName

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        return initView(position, convertView, parent,true)
    }

    override fun getDropDownView(position: Int, convertView: View?, parent: ViewGroup): View {
        return initView(position, convertView, parent)
    }


    private fun initView(position: Int, convertView: View?, parent: ViewGroup, willImgViewHide:Boolean=false): View {

        val album=getItem(position)

        val layoutInflater= LayoutInflater.from(parent.context)

//        val view = LayoutInflater.from(context).inflate(R.layout.album_spinner_item,parent,false)

      val   binding=AlbumSpinnerItemBinding.inflate(layoutInflater, parent, false)

//         loadImgWithGlide(album!!.name,)

        Log.d(TAG, "initView: ${album!!.name}")

        binding.albumSpinnerText.text=album!!.name
        if (willImgViewHide) binding.albumSpinnerImg.visibility=View.GONE

        return binding.root

    }

   private fun loadImgWithGlide(path: String ,imageView: ImageView) {

    Glide.with(imageView)
        .load(path)
        .transition(DrawableTransitionOptions().crossFade( ))
        .into(imageView);
}


}