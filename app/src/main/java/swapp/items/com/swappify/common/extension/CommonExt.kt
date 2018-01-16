package swapp.items.com.swappify.common.extension

import android.graphics.drawable.Drawable
import android.support.design.widget.Snackbar
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.support.v7.widget.Toolbar
import android.view.View
import swapp.items.com.swappify.R
import swapp.items.com.swappify.controllers.configs.*


fun EmptyViewConfiguration.emptyViewBinding(emptyMsg: CharSequence?, emptyDrawable: Drawable?) =
    this.newState(emptyMsg)
            .setDrawable(emptyDrawable)
            .commit()


fun ErrorViewConfiguration.errorViewBinding(errorMsg: CharSequence?, errorDrawable: Drawable?, errorRetryClickListener: View.OnClickListener?) =
    this.newState(errorMsg)
            .setDrawable(errorDrawable)
            .setClickCallback(errorRetryClickListener)
            .commit()


fun ContentLoadingConfiguration.contentLoadingBinding(contentLoadingText: CharSequence?)  =
        this.newState(contentLoadingText)
                .commit()

fun ToolbarConfiguration.toolbarBinding(toolbarTitle: CharSequence?, toolbarTitleColor: Int = R.color.white,
                                        toolbarMenu: Int, toolbarNavigationClickListener: View.OnClickListener?,
                                        toolbarNavigationIcon: Drawable?, toolbarMenuItemClickListener: Toolbar.OnMenuItemClickListener?) =
        this.newState(toolbarTitle)
                .setTitleColor(toolbarTitleColor)
                .setMenu(toolbarMenu)
                .setNavigationBackListener(toolbarNavigationClickListener)
                .setNavigationIcon(toolbarNavigationIcon)
                .setMenuClickListener(toolbarMenuItemClickListener)
                .commit()


fun RecyclerViewConfiguration.recyclerViewBinding(adapter: RecyclerView.Adapter<*>?,
                                                  linearLayoutManager: LinearLayoutManager?) =
    this.newState(adapter)
            .setLayoutManger(linearLayoutManager)
            .commit()


fun SnackbarConfiguration.showSnackBar(msg: String, type: SnackbarConfiguration.Type) =
        this.newState(msg)
                .setDuration(Snackbar.LENGTH_SHORT)
                .setType(type)
                .commit()

fun SnackbarConfiguration.showNoNetworkSnackBar(msg: String, actionName: String?, actionListener: View.OnClickListener?) =
        this.newState(msg)
                .setDuration(Snackbar.LENGTH_INDEFINITE)
                .setAction(actionName)
                .setActionListener(actionListener)
                .commit()