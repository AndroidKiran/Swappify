package swapp.items.com.swappify.controller.editprofile.ui

import android.text.Editable

interface IEditProfileNavigator {

    fun onCameraClick()

    fun onGalleryClick()

    fun onSaveProfileClick()

    fun onGetLocationClick()

    fun onLocationInfoClick()

    fun afterTextChanged(editable: Editable)

}