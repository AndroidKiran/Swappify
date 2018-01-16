package swapp.items.com.swappify.controllers.configs

import android.databinding.BaseObservable
import android.databinding.Bindable
import android.databinding.BindingAdapter
import android.graphics.drawable.Drawable
import android.view.View
import swapp.items.com.swappify.components.BindedMultiStateView

class ErrorViewConfiguration : BaseObservable() {

    companion object {
        @JvmStatic
        @BindingAdapter("errorViewBindingConfig")
        fun bindErrorViewBindingConfig(multiStateView: BindedMultiStateView<*>?, errorViewConfiguration: ErrorViewConfiguration?) {
            multiStateView?.setErrorViewConfiguration(errorViewConfiguration)
        }
    }

    @get:Bindable
    var errorScreenDrawable: Drawable? = null
        private set (value) {
            field = value
        }

    @get:Bindable
    var errorScreenText: CharSequence? = null
        private set (value) {
            field = value
        }

    @get:Bindable
    var errorRetryClickListener: View.OnClickListener? = null
        private set (value) {
            field = value
        }

    fun newState(msg: CharSequence?): Builder = Builder(msg)

    inner class Builder constructor(private val msg: CharSequence?) {

        private var errorScreenDrawable: Drawable? = null

        private var errorRetryClickListener: View.OnClickListener? = null

        fun setDrawable(errorScreenDrawable: Drawable?): Builder {
            this.errorScreenDrawable = errorScreenDrawable
            return this@Builder
        }

        fun setClickCallback(errorRetryClickListener: View.OnClickListener?): Builder {
            this.errorRetryClickListener = errorRetryClickListener
            return this@Builder
        }

        fun commit() {
            this@ErrorViewConfiguration.setErrorViewConfig(msg, errorScreenDrawable, errorRetryClickListener)
        }
    }

    fun setErrorViewConfig(errorScreenText: CharSequence?, errorScreenDrawable: Drawable?,errorRetryClickListener: View.OnClickListener?) {
        this.errorScreenText = errorScreenText
        this.errorScreenDrawable = errorScreenDrawable
        this.errorRetryClickListener = errorRetryClickListener
        notifyChange()
    }
}