package swapp.items.com.swappify.controllers.configs

import android.databinding.BaseObservable
import android.databinding.Bindable
import android.graphics.drawable.Drawable
import android.view.View


class ToolbarConfiguration : BaseObservable() {

    @get:Bindable
    var title: String? = null
        private set (value) {
            field = value
        }

    @get:Bindable
    var clickListener: View.OnClickListener? = null
        private set (value) {
            field = value
        }

    @get:Bindable
    var navigationIcon: Drawable? = null
        private set (value) {
            field = value
        }

    fun setConfig(title: String, clickListener: View.OnClickListener, navigationIcon: Drawable) {
        this.title = title
        this.clickListener = clickListener
        this.navigationIcon = navigationIcon
        notifyChange()
    }
}