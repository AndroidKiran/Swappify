package swapp.items.com.swappify.controller.configs

import android.databinding.BaseObservable
import android.databinding.Bindable
import android.databinding.BindingAdapter
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
    var contentLoadingText: CharSequence? = null
    private set(value) {
        field = value
    }

    fun newState(msg: CharSequence?): Builder = Builder(msg)

    inner class Builder constructor(private val msg: CharSequence?) {
        fun commit() {
            this@ContentLoadingConfiguration.setConfig(msg)
        }
    }

    fun setConfig(contentLoadingText: CharSequence?) {
        this.contentLoadingText = contentLoadingText
        notifyChange()
    }

}