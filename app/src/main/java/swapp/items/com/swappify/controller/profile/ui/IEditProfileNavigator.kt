package swapp.items.com.swappify.controller.profile.ui

import android.text.Editable

interface IEditProfileNavigator {

    fun onCameraClick()

    fun onGalleryClick()

    fun onSaveProfileClick()

    fun afterTextChanged(editable: Editable)

}