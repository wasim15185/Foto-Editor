package com.android.demoeditor.customClass


import android.annotation.SuppressLint
import android.content.ContentResolver
import android.database.Cursor
import com.android.demoeditor.constant.*
import com.android.demoeditor.data.*
import com.android.demoeditor.utils.doWhile
import android.provider.MediaStore


class ImgContentResolverData(private val contentResolver: ContentResolver) {

    companion object{
        private val TAG=ImgContentResolverData::class.java.simpleName
    }

    fun loadAlbumImages(
        albumItem: AlbumData?,
        page: Int
    ): ArrayList<ImgSelectorData> {

        val offset = page * PAGE_SIZE
        val list: ArrayList<ImgSelectorData> = arrayListOf()
        var photoCursor: Cursor? = null

        try {
            if (albumItem == null || albumItem.isAll) {

                photoCursor = contentResolver.query(
                    cursorUri,
                    arrayOf(
                        ID_COLUMN,
                        PATH_COLUMN
                    ),
                    null,
                    null,
                    "$ORDER_BY LIMIT $PAGE_SIZE OFFSET $offset"
                )

            } else {
                photoCursor = contentResolver.query(
                    cursorUri,
                    arrayOf(
                        ID_COLUMN,
                        PATH_COLUMN
                    ),
                    "${MediaStore.Images.ImageColumns.BUCKET_ID} =?",
                    arrayOf(albumItem.bucketId),
                    "$ORDER_BY LIMIT $PAGE_SIZE OFFSET $offset"
                )
            }


            photoCursor?.isAfterLast ?: return list
            photoCursor.doWhile {

                val image = photoCursor.getString((photoCursor.getColumnIndex(PATH_COLUMN)))
                list.add(ImgSelectorData(imagePath = image))
            }


        } finally {

            if (photoCursor != null && !photoCursor.isClosed) {
                photoCursor.close()
            }

        }


        return list

    }


    @SuppressLint("Range")
    fun loadAlbums(): ArrayList<AlbumData> {
        val albumCursor = contentResolver.query(
            cursorUri,
            arrayOf(DISPLAY_NAME_COLUMN, MediaStore.Images.ImageColumns.BUCKET_ID),
            null,
            null,
            ORDER_BY
        )

        val list = arrayListOf<AlbumData>()

        try {
            list.add(AlbumData("All", true, "0"))
            if (albumCursor == null) {
                return list
            }

            albumCursor.doWhile {
                val bucketId =
                    albumCursor.getString(albumCursor.getColumnIndex(MediaStore.Images.ImageColumns.BUCKET_ID))
                val name = albumCursor.getString(albumCursor.getColumnIndex(DISPLAY_NAME_COLUMN))
                    ?: bucketId
                val albumItem = AlbumData(name, false, bucketId)
                if (!list.contains(albumItem)) {
                    list.add(albumItem)
                }
            }


        } finally {
            if (albumCursor != null && !albumCursor.isClosed) {
                albumCursor.close()
            }
        }

        return list

    }


}