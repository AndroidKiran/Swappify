package swapp.items.com.swappify.common.extension

import android.graphics.drawable.Drawable
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import swapp.items.com.swappify.controllers.configs.ContentLoadingConfiguration
import swapp.items.com.swappify.controllers.configs.EmptyViewConfiguration
import swapp.items.com.swappify.controllers.configs.ErrorViewConfiguration
import swapp.items.com.swappify.controllers.configs.RecyclerViewConfiguration


fun EmptyViewConfiguration.emptyViewBinding(emptyDrawable: Drawable?, emptyMsg: String?) {
    setEmptyViewConfig(emptyDrawable, emptyMsg)
}

fun ErrorViewConfiguration.errorViewBinding(errorDrawable: Drawable?, errorMsg: String?) {
    setErrorViewConfig(errorDrawable, errorMsg)
}

fun ContentLoadingConfiguration.contentLoadingBinding(contentLoadingText: String?) {
    setConfig(contentLoadingText)
}

fun RecyclerViewConfiguration.recyclerViewBinding(linearLayoutManager: LinearLayoutManager?,
                                                  adapter: RecyclerView.Adapter<*>?) {
    setRecyclerConfig(linearLayoutManager, adapter)
}
