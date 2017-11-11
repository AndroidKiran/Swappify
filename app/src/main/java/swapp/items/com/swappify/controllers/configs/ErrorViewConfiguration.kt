package swapp.items.com.swappify.controllers.configs

import android.databinding.BaseObservable
import android.databinding.Bindable
import android.databinding.BindingAdapter
import android.graphics.drawable.Drawable
import android.view.View
import swapp.items.com.swappify.components.MultiStateView

class ErrorViewConfiguration : BaseObservable() {

    companion object {
        @JvmStatic
        @BindingAdapter("errorViewConfig")
        fun bindErrorViewConfiguration(multiStateView: MultiStateView?, errorViewConfiguration: ErrorViewConfiguration?) {
            multiStateView?.setErrorViewConfiguration(errorViewConfiguration)
        }
    }

    @get:Bindable
    var errorScreenDrawable: Drawable? = null
        private set (value) {
            field = value
        }

    @get:Bindable
    var errorScreenText: String? = null
        private set (value) {
            field = value
        }

    @get:Bindable
    var errorRetryClickListener: View.OnClickListener? = null
        private set (value) {
            field = value
        }

    fun setErrorViewConfig(errorScreenDrawable: Drawable?, errorScreenText: String?) {
        this.errorScreenDrawable = errorScreenDrawable
        this.errorScreenText = errorScreenText
        notifyChange()
    }

    fun setErrorRetryListener(errorRetryClickListener: View.OnClickListener?) {
        this.errorRetryClickListener = errorRetryClickListener
        notifyChange()
    }

}