package swapp.items.com.swappify.controllers.configs

import android.databinding.BaseObservable
import android.databinding.Bindable
import android.databinding.BindingAdapter
import android.graphics.drawable.Drawable
import android.support.v7.widget.Toolbar
import android.view.View
import swapp.items.com.swappify.R


class ToolbarConfiguration : BaseObservable() {

    companion object {
        @JvmStatic
        @BindingAdapter("toolbarConfig")
        fun bindToolbarConfiguration(toolbar: Toolbar?, toolbarConfiguration: ToolbarConfiguration?) {
            toolbar?.navigationIcon = toolbarConfiguration?.toolbarNavigationIcon
            toolbar?.title = toolbarConfiguration?.toolbarTitle
            toolbar?.setTitleTextColor(toolbarConfiguration?.toolbarTitleColor!!)
            toolbar?.setNavigationOnClickListener(toolbarConfiguration?.toolbarOnNavigationClickListener)
            if (toolbarConfiguration?.toolbarMenu != 0) {
                toolbar?.inflateMenu(toolbarConfiguration?.toolbarMenu!!)
            }
            toolbar?.setOnMenuItemClickListener(toolbarConfiguration?.toolbarOnMenuItemClickListener)
        }
    }

    @get:Bindable
    var toolbarTitle: String? = null
        private set (value) {
            field = value
        }

    @get:Bindable
    var toolbarOnNavigationClickListener: View.OnClickListener? = null
        private set (value) {
            field = value
        }

    @get:Bindable
    var toolbarNavigationIcon: Drawable? = null
        private set (value) {
            field = value
        }

    @get:Bindable
    var toolbarTitleColor: Int = 0
        private set(value) {
            field = value
        }

    @get:Bindable
    var toolbarMenu: Int = 0
        private set(value) {
            field = value
        }

    @get:Bindable
    var toolbarOnMenuItemClickListener: Toolbar.OnMenuItemClickListener? = null
        private set(value) {
            field = value
        }

    fun setToolbarConfig(toolbarTitle: String?, toolbarTitleColor: Int = R.color.white, toolbarOnNavigationClickListener: View.OnClickListener?,
                         toolbarNavigationIcon: Drawable?) {
        this.toolbarTitle = toolbarTitle
        this.toolbarTitleColor = toolbarTitleColor
        this.toolbarOnNavigationClickListener = toolbarOnNavigationClickListener
        this.toolbarNavigationIcon = toolbarNavigationIcon
        notifyChange()
    }

    fun setToolbarConfig(toolbarTitle: String?, toolbarTitleColor: Int = R.color.white, toolbarMenu: Int, toolbarOnNavigationClickListener: View.OnClickListener?,
                         toolbarNavigationIcon: Drawable?, toolbarOnMenuItemClickListener: Toolbar.OnMenuItemClickListener?) {
        this.toolbarTitle = toolbarTitle
        this.toolbarTitleColor = toolbarTitleColor
        this.toolbarOnNavigationClickListener = toolbarOnNavigationClickListener
        this.toolbarNavigationIcon = toolbarNavigationIcon
        this.toolbarMenu = toolbarMenu
        this.toolbarOnMenuItemClickListener = toolbarOnMenuItemClickListener
        notifyChange()
    }
}