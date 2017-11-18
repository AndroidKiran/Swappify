package swapp.items.com.swappify.controllers.configs

import android.databinding.BindingAdapter
import android.databinding.ObservableField
import swapp.items.com.swappify.components.MultiStateView

class ContentLoadingConfiguration constructor(isContentLoading: Boolean?, contentLoadingText: String?) {

    companion object {
        @JvmStatic
        @BindingAdapter("contentLoadingViewConfig")
        fun bindContentLoadingViewConfig(multiStateView: MultiStateView?, contentLoadingConfiguration: ContentLoadingConfiguration?) {
            multiStateView?.setContentLoadingViewConfiguration(contentLoadingConfiguration)
        }
    }



    var isContentLoading: ObservableField<Boolean> = ObservableField<Boolean>(isContentLoading)

    var contentLoadingText: ObservableField<String> = ObservableField<String>(contentLoadingText)

}