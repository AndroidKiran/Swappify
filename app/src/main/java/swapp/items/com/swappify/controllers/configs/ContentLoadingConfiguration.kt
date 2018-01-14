package swapp.items.com.swappify.controllers.configs

import android.databinding.BaseObservable
import android.databinding.Bindable
import android.databinding.BindingAdapter
import swapp.items.com.swappify.BR
import swapp.items.com.swappify.components.BindedMultiStateView

class ContentLoadingConfiguration : BaseObservable() {

    companion object {
        @JvmStatic
        @BindingAdapter("contentLoadingBindingConfig")
        fun bindContentLoadingBindingConfig(multiStateView: BindedMultiStateView<*>?,
                                            contentLoadingConfiguration: ContentLoadingConfiguration?) {
            multiStateView?.setContentLoadingViewConfiguration(contentLoadingConfiguration)
        }

    }

    @get:Bindable
    var contentLoadingText: String? = null
    private set(value) {
        field = value
    }

    fun setConfig(contentLoadingText: String?) {
        this.contentLoadingText = contentLoadingText
        notifyPropertyChanged(BR.contentLoadingText)
    }

}