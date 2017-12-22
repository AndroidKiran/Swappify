package swapp.items.com.swappify.controllers.configs

import android.databinding.BaseObservable
import android.databinding.Bindable
import android.databinding.BindingAdapter
import android.graphics.drawable.Drawable
import swapp.items.com.swappify.components.BindedMultiStateView
import swapp.items.com.swappify.components.MultiStateView

class EmptyViewConfiguration : BaseObservable() {

    companion object {
        @JvmStatic
        @BindingAdapter("emptyViewBinding")
        fun bindEmptyViewConfiguration(multiStateView: MultiStateView?, emptyViewConfiguration: EmptyViewConfiguration?) {
            multiStateView?.setEmptyViewConfiguration(emptyViewConfiguration)
        }

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
    var emptyScreenText: String? = null
        private set (value) {
            field = value
        }

    fun setEmptyViewConfig(emptyScreenDrawable: Drawable?, emptyScreenText: String?) {
        this.emptyScreenDrawable = emptyScreenDrawable
        this.emptyScreenText = emptyScreenText
        notifyChange()
    }
}