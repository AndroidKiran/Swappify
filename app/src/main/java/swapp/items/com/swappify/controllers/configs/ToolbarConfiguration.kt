package swapp.items.com.swappify.controllers.configs

import android.databinding.BaseObservable
import android.databinding.Bindable
import android.graphics.drawable.Drawable
import android.support.v7.widget.Toolbar
import android.view.View
import swapp.items.com.swappify.R


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

    @get:Bindable
    var titleColor: Int = 0
        private set(value) {
            field = value
        }

    @get:Bindable
    var menu: Int = 0
        private set(value) {
            field = value
        }

    @get:Bindable
    var onMenuItemClickListener: Toolbar.OnMenuItemClickListener? = null
        private set(value) {
            field = value
        }

    fun setToolbarConfig(title: String?, titleColor: Int = R.color.white, clickListener: View.OnClickListener?,
                  navigationIcon: Drawable?) {
        this.title = title
        this.titleColor = titleColor
        this.clickListener = clickListener
        this.navigationIcon = navigationIcon
        notifyChange()
    }

    fun setToolbarConfig(title: String?, titleColor: Int = R.color.white, menu: Int, clickListener: View.OnClickListener?,
                  navigationIcon: Drawable?, onMenuItemClickListener: Toolbar.OnMenuItemClickListener?) {
        this.title = title
        this.titleColor = titleColor
        this.clickListener = clickListener
        this.navigationIcon = navigationIcon
        this.menu = menu
        this.onMenuItemClickListener = onMenuItemClickListener
        notifyChange()
    }
}