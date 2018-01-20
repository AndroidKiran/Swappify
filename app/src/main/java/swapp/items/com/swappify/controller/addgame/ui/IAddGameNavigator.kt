package swapp.items.com.swappify.controller.addgame.ui

import android.text.Editable

interface IAddGameNavigator {

    fun onCameraClick()

    fun onGalleryClick()

    fun onAddGameClick()

    fun afterTextChanged(editable: Editable)

}