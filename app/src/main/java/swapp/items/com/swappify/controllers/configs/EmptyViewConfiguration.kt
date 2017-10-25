package swapp.items.com.swappify.controllers.configs

import android.databinding.BaseObservable
import android.databinding.Bindable
import android.graphics.drawable.Drawable

class EmptyViewConfiguration : BaseObservable() {

    @get:Bindable
    var errorDrawable: Drawable? = null
        private set (value) {
            field = value
        }

    @get:Bindable
    var errorText: String? = null
        private set (value) {
            field = value
        }

    fun setEmptyViewConfig(errorDrawable: Drawable?, errorText: String?) {
        this.errorDrawable = errorDrawable
        this.errorText = errorText
        notifyChange()
    }
}