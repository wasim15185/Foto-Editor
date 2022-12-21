package com.android.demoeditor.viewModel

import android.annotation.SuppressLint
import android.app.Activity
import android.app.Application
import android.content.ActivityNotFoundException
import android.content.Context
import android.content.Intent
import com.android.demoeditor.R
import com.android.demoeditor.data.database.EditedPhoto
import com.android.demoeditor.database.RecentPhotoDatabase
import android.net.Uri
import android.widget.Toast
import androidx.lifecycle.AndroidViewModel
import kotlinx.coroutines.*

@SuppressLint("StaticFieldLeak")
class SaveImageViewModel(
    private val recentPhotoDatabase: RecentPhotoDatabase,
    application: Application,
    private var activity: Activity? = null
) :
    AndroidViewModel(application) {


    private val viewModelJob = Job()
    private val uiScope = CoroutineScope(Dispatchers.Main + viewModelJob)


    private var context: Context? = getApplication<Application>().applicationContext

    override fun onCleared() {
        super.onCleared()
        viewModelJob.cancel()
        context = null
        activity = null
    }

    fun insertUriIntoDB(uri: Uri) {

        uiScope.launch {
            withContext(Dispatchers.IO) {
                val editedPhoto = EditedPhoto(uriString = uri.toString())
                recentPhotoDatabase.photoDatabaseDao.insert(editedPhoto)
            }
        }


    }


    fun whatsAppShareIntent(imgUri: Uri) {

        val whatsappIntent = Intent(Intent.ACTION_SEND)
        whatsappIntent.type = "text/plain"
        whatsappIntent.setPackage("com.whatsapp")
        whatsappIntent.putExtra(Intent.EXTRA_STREAM, imgUri)
        whatsappIntent.type = "image/jpeg"
        whatsappIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)

        try {
            activity!!.startActivity(whatsappIntent)
        } catch (ex: ActivityNotFoundException) {
            Toast.makeText(
                context,
                "Whatsapp have not been installed.",
                Toast.LENGTH_SHORT
            ).show()
        }
    }

    fun instagramShareIntent(imgUri: Uri) {


        val feedIntent = Intent(Intent.ACTION_SEND)
        feedIntent.type = "image/*"
        feedIntent.putExtra(Intent.EXTRA_STREAM, imgUri)
        feedIntent.setPackage("com.instagram.android")

        val storiesIntent: Intent = Intent("com.instagram.share.ADD_TO_STORY")
        storiesIntent.setDataAndType(imgUri, "image/*")
        storiesIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
        storiesIntent.setPackage("com.instagram.android")


        feedIntent.putExtra(Intent.EXTRA_INITIAL_INTENTS, arrayOf(storiesIntent))

        try {
            activity!!.startActivity(feedIntent)
        } catch (ex: ActivityNotFoundException) {
            Toast.makeText(
                context,
                "Instagram have not been installed.",
                Toast.LENGTH_SHORT
            ).show()
        }


    }

    fun twitterShareIntent(uri: Uri) {


        val shareTwitterIntent = Intent(Intent.ACTION_SEND)
            .apply {
                putExtra(
                    Intent.EXTRA_TEXT,
                    "This photo is edited  by ${context?.getString(R.string.app_name)}"
                );
                type = "text/plain";
                putExtra(Intent.EXTRA_STREAM, uri);
                type = "image/jpeg";
                setPackage("com.twitter.android");
            }

        try {
            activity!!.startActivity(shareTwitterIntent)
        } catch (ex: ActivityNotFoundException) {
            Toast.makeText(
                context,
                "Twitter have not been installed.",
                Toast.LENGTH_SHORT
            ).show()
        }


    }


}