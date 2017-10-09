package swapp.items.com.swappify.utils

import android.databinding.BindingAdapter
import android.graphics.drawable.Drawable
import android.support.design.widget.Snackbar
import android.support.v7.widget.AppCompatImageView
import android.support.v7.widget.RecyclerView
import android.support.v7.widget.Toolbar
import android.view.View
import android.widget.TextView
import com.bumptech.glide.Glide
import swapp.items.com.swappify.R
import swapp.items.com.swappify.controllers.configs.RecyclerViewConfiguration
import swapp.items.com.swappify.controllers.configs.SnackbarConfiguration

class BindingUtils {

    companion object {
        @BindingAdapter("imageUrl")
        fun setImageUrl(imageView: AppCompatImageView, url: String) {
            val context = imageView.context
            Glide.with(context).load(url).into(imageView)
        }

        @BindingAdapter("imageDrawable")
        fun setImageDrawable(imageView: AppCompatImageView, drawable: Drawable) {
            imageView.setImageDrawable(drawable)
        }

        @BindingAdapter("navigationIcon")
        fun setNavigationIcon(toolbar: Toolbar, drawable: Drawable) {
            toolbar.navigationIcon = drawable
        }

        @BindingAdapter("snackbar")
        fun bindSnackBarText(layout: View, config: SnackbarConfiguration?) {
            if (config?.msg != null) {
                val snackbar = Snackbar.make(layout, config.msg!!, config.duration)
                val view = snackbar.getView()

                val ta = layout.context.obtainStyledAttributes(
                        config.type!!.styleId,
                        R.styleable.SnackbarStyle
                )

                val textColor = ta.getColor(R.styleable.SnackbarStyle_android_textColor, 0)
                val backgroundColor = ta.getColor(R.styleable.SnackbarStyle_android_background, 0)

                view.setBackgroundColor(backgroundColor)

                val tv = android.support.design.R.id.snackbar_text as TextView
                tv.setTextColor(textColor)

                snackbar.show()

                ta.recycle()
            }
        }


        @BindingAdapter("recyclerConfig")
        fun bindRecyclerViewConfiguration(view: RecyclerView, recyclerConfig: RecyclerViewConfiguration?) {
            if (recyclerConfig != null) {
                if (view.adapter == null && recyclerConfig.adapter != null) {
                    view.adapter = recyclerConfig.adapter
                }
                if (view.layoutManager == null && recyclerConfig.layoutManager != null) {
                    view.layoutManager = recyclerConfig.layoutManager
                }
            }
        }
    }

}

