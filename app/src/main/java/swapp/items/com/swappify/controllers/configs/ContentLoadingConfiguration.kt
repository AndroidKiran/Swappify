package swapp.items.com.swappify.controllers.configs

import android.databinding.BindingAdapter
import android.databinding.ObservableField
import swapp.items.com.swappify.components.MultiStateView

class ContentLoadingConfiguration {

    companion object {
        @JvmStatic
        @BindingAdapter("contentLoadingViewConfig")
        fun bindContentLoadingViewConfig(multiStateView: MultiStateView?, contentLoadingConfiguration: ContentLoadingConfiguration?) {
            multiStateView?.setContentLoadingViewConfiguration(contentLoadingConfiguration)
        }
    }

    var isContentLoading: ObservableField<Boolean> = ObservableField<Boolean>()

    var contentLoadingText: ObservableField<String> = ObservableField<String>()

}