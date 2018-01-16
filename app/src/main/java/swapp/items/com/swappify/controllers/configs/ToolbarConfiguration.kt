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
            toolbar?.setNavigationOnClickListener(toolbarConfiguration?.toolbarNavigationClickListener)
            if (toolbarConfiguration?.toolbarMenu != 0) {
                toolbar?.inflateMenu(toolbarConfiguration?.toolbarMenu!!)
            }
            toolbar?.setOnMenuItemClickListener(toolbarConfiguration?.toolbarMenuItemClickListener)
        }
    }

    @get:Bindable
    var toolbarTitle: CharSequence? = null
        private set (value) {
            field = value
        }

    @get:Bindable
    var toolbarNavigationClickListener: View.OnClickListener? = null
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
    var toolbarMenuItemClickListener: Toolbar.OnMenuItemClickListener? = null
        private set(value) {
            field = value
        }

    fun newState(title: CharSequence?): Builder = Builder(title)

    inner class Builder constructor(private val title: CharSequence?) {

        private var toolbarTitleColor: Int = R.color.white

        private var menu: Int = 0

        private var toolbarNavigationClickListener: View.OnClickListener? = null

        private var toolbarNavigationIcon: Drawable? = null

        private var toolbarMenuItemClickListener: Toolbar.OnMenuItemClickListener? = null

        fun setTitleColor(toolbarTitleColor: Int): Builder {
            this.toolbarTitleColor = toolbarTitleColor
            return this@Builder
        }

        fun setMenu(menu: Int): Builder {
            this.menu = menu
            return this@Builder
        }

        fun setNavigationBackListener(toolbarNavigationClickListener: View.OnClickListener?): Builder {
            this.toolbarNavigationClickListener = toolbarNavigationClickListener
            return this@Builder
        }

        fun setNavigationIcon(toolbarNavigationIcon: Drawable?): Builder {
            this.toolbarNavigationIcon = toolbarNavigationIcon
            return this@Builder
        }

        fun setMenuClickListener(toolbarMenuItemClickListener: Toolbar.OnMenuItemClickListener?): Builder {
            this.toolbarMenuItemClickListener = toolbarMenuItemClickListener
            return this@Builder
        }

        fun commit() {
            this@ToolbarConfiguration.setToolbarConfig(title, toolbarTitleColor, menu, toolbarNavigationClickListener,
                    toolbarNavigationIcon, toolbarMenuItemClickListener)
        }

    }

    fun setToolbarConfig(toolbarTitle: CharSequence?, toolbarTitleColor: Int = R.color.white, toolbarMenu: Int, toolbarNavigationClickListener: View.OnClickListener?,
                         toolbarNavigationIcon: Drawable?, toolbarMenuItemClickListener: Toolbar.OnMenuItemClickListener?) {
        this.toolbarTitle = toolbarTitle
        this.toolbarTitleColor = toolbarTitleColor
        this.toolbarNavigationClickListener = toolbarNavigationClickListener
        this.toolbarNavigationIcon = toolbarNavigationIcon
        this.toolbarMenu = toolbarMenu
        this.toolbarMenuItemClickListener = toolbarMenuItemClickListener
        notifyChange()
    }
}