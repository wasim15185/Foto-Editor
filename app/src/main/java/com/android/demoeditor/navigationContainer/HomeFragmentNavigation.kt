package com.android.demoeditor.navigationContainer

import com.android.demoeditor.data.parcelData.CollageSelectorData
import com.android.demoeditor.data.parcelData.CommonParcelData
import com.android.demoeditor.enums.ActiveNavArgsData
import com.android.demoeditor.screens.HomeFragmentDirections
import android.net.Uri
import androidx.navigation.NavController


class HomeFragmentNavigation(
    private val findNavController: NavController,
    private val imgPicker:TedImageSelector
 ) {

     fun colleaguePhotosSelector() {
        imgPicker.multiImagePicker {
            val action =
                HomeFragmentDirections.homeFragmentToCollageSelectorFragment(
                    CollageSelectorData(it)
                )
            findNavController.navigate(action)

        }


    }


     fun singlePhotoSelector() {
        imgPicker.singleImagePicker  {
            val data = CommonParcelData(
                uri = it,
                availableData = ActiveNavArgsData.URI,

                )
            val action =
                HomeFragmentDirections.homeFragmentToMainEditScreenFragment(data)


            findNavController.navigate(action)

        }

    }


    /**
     * [gotoAdjustFragment] is for goto adjust fragment
     */
     fun gotoAdjustFragment() {

        imgPicker.singleImagePicker {
            val data = CommonParcelData(
                uri = it,
                availableData = ActiveNavArgsData.URI,

                )

            val action = HomeFragmentDirections.homeFragmentToAdjustEditFragment(data)
            findNavController.navigate(action)
        }

    }


    /**
     *
     * [gotoFilterFragment] is goto Filter Fragment
     */

     fun gotoFilterFragment() {

        imgPicker.singleImagePicker {
            val data = CommonParcelData(
                uri = it,
                availableData = ActiveNavArgsData.URI,

                )


            val action = HomeFragmentDirections.homeFragmentToFiltersFragment(data)

            findNavController.navigate(action)

        }


    }

    /**
     *
     * [gotoCropFragment] is goto Crop Fragment
     */

     fun gotoCropFragment() {
        imgPicker.singleImagePicker {
            val data = CommonParcelData(
                uri = it,
                availableData = ActiveNavArgsData.URI,

                )

            val action = HomeFragmentDirections.homeFragmentToCropFragment(data)

            findNavController.navigate(action)

        }


    }

    /**
     * [gotoRotatedFragment] is for goto Rotated Fragment
     */

     fun gotoRotatedFragment() {
        imgPicker.singleImagePicker {
            val data = CommonParcelData(
                uri = it,
                availableData = ActiveNavArgsData.URI,

                )

            val action = HomeFragmentDirections.homeFragmentToRotationFragment(data)

            findNavController.navigate(action)

        }

    }


    /**
     * [gotoStickerFragment] is for goto Sticker Fragment
     */

     fun gotoStickerFragment() {
        imgPicker.singleImagePicker {
            val data = CommonParcelData(
                uri = it,
                availableData = ActiveNavArgsData.URI,

                )

            val action = HomeFragmentDirections.homeFragmentToNewStickerFragment(data)

            findNavController.navigate(action)

        }

    }


    /**
     * [gotoTextStickerFragment] is for goto Sticker Fragment
     */


    private fun gotoTextStickerFragment() {
        imgPicker.singleImagePicker {
            val data = CommonParcelData(
                uri = it,
                availableData = ActiveNavArgsData.URI,

                )

            val action = HomeFragmentDirections.homeFragmentToTextStickerFragment(data)


            findNavController.navigate(action)

        }
    }



    interface TedImageSelector{
        fun singleImagePicker(result:(uri:Uri)->Unit)
        fun multiImagePicker(result:(uriList:List<Uri>)->Unit)
    }

}



