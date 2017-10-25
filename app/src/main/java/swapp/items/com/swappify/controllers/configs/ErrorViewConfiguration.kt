package swapp.items.com.swappify.controllers.configs

import android.databinding.BaseObservable
import android.databinding.Bindable
import android.graphics.drawable.Drawable
import android.view.View

class ErrorViewConfiguration : BaseObservable() {

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

    @get:Bindable
    var clickListener: View.OnClickListener? = null
        private set (value) {
            field = value
        }

    fun setErrorViewConfig(errorDrawable: Drawable?, errorText: String?) {
        this.errorDrawable = errorDrawable
        this.errorText = errorText
        notifyChange()
    }

    fun setErrorRetryListener(clickListener: View.OnClickListener?) {
        this.clickListener = clickListener
        notifyChange()
    }

}