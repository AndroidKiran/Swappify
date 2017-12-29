package swapp.items.com.swappify.components.imagepicker

import android.annotation.SuppressLint
import android.content.Context
import android.os.Bundle


class ImagePicker constructor(val context: Context) {

    private var imagePickerListener: ImagePickerListener? = null

    fun requestImage(source: Sources) {
//        context.startActivity(OverlapPickerActivity.start(context = context, source = source))
    }

    fun setImagePickerListener(imagePickerListener: ImagePickerListener) {
        this.imagePickerListener = imagePickerListener
    }

    fun onImagePicked(bundle: Bundle) {
        if (imagePickerListener != null) {
            imagePickerListener!!.onImagePick(bundle)
        }
    }

    interface ImagePickerListener {
        fun onImagePick(bundle: Bundle)
    }

    object Picker {

        @SuppressLint("StaticFieldLeak")
        private var INSTANCE: ImagePicker? = null

        @Synchronized
        fun getInstance(context: Context): ImagePicker? {
            if(INSTANCE == null) {
                INSTANCE = ImagePicker(context)
            }
            return INSTANCE
        }

    }


}