package swapp.items.com.swappify.controllers.configs

import android.databinding.BaseObservable
import android.databinding.Bindable
import android.databinding.BindingAdapter
import android.graphics.drawable.Drawable
import swapp.items.com.swappify.components.BindedMultiStateView

class EmptyViewConfiguration : BaseObservable() {

    companion object {
        @JvmStatic
        @BindingAdapter("emptyViewBindingConfig")
        fun bindEmptyViewBindingConfig(multiStateView: BindedMultiStateView<*>?, emptyViewConfiguration: EmptyViewConfiguration?) {
            multiStateView?.setEmptyViewConfiguration(emptyViewConfiguration)
        }
    }

    @get:Bindable
    var emptyScreenDrawable: Drawable? = null
        private set (value) {
            field = value
        }

    @get:Bindable
    var emptyScreenText: CharSequence? = null
        private set (value) {
            field = value
        }

    fun newState(msg: CharSequence?): Builder = Builder(msg)

    inner class Builder constructor(private val msg: CharSequence?) {

        private var emptyScreenDrawable: Drawable? = null

        fun setDrawable(emptyScreenDrawable: Drawable?): Builder {
            this.emptyScreenDrawable = emptyScreenDrawable
            return this@Builder
        }

        fun commit() {
            this@EmptyViewConfiguration.setEmptyViewConfig(msg, emptyScreenDrawable)
        }
    }


    fun setEmptyViewConfig(emptyScreenText: CharSequence?, emptyScreenDrawable: Drawable?) {
        this.emptyScreenText = emptyScreenText
        this.emptyScreenDrawable = emptyScreenDrawable
        notifyChange()
    }
}