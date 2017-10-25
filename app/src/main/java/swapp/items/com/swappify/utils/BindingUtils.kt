package swapp.items.com.swappify.utils

import android.databinding.BindingAdapter
import android.graphics.drawable.Drawable
import android.support.design.widget.Snackbar
import android.support.v7.widget.AppCompatEditText
import android.support.v7.widget.AppCompatImageView
import android.support.v7.widget.RecyclerView
import android.support.v7.widget.Toolbar
import android.view.View
import android.widget.TextView
import com.bumptech.glide.Glide
import swapp.items.com.swappify.R
import swapp.items.com.swappify.components.MultiStateView
import swapp.items.com.swappify.controllers.configs.*

class BindingUtils {

    companion object {

        @JvmStatic
        @BindingAdapter("imageUrl")
        fun setImageUrl(imageView: AppCompatImageView?, url: String?) {
            val context = imageView?.context
            Glide.with(context).load(url).into(imageView)
        }

        @JvmStatic
        @BindingAdapter("imageDrawable")
        fun setImageDrawable(imageView: AppCompatImageView?, imageDrawable: Drawable?) {
            imageView?.setImageDrawable(imageDrawable)
        }

        @JvmStatic
        @BindingAdapter("snackbar")
        fun bindSnackBarText(layout: View?, config: SnackbarConfiguration?) {
            if (config?.msg != null) {
                val snackbar = Snackbar.make(layout!!, config.msg!!, config.duration)
                val view = snackbar.getView()

                val ta = layout.context?.obtainStyledAttributes(
                        config.type!!.styleId,
                        R.styleable.SnackbarStyle
                )

                val textColor = ta?.getColor(R.styleable.SnackbarStyle_android_textColor, 0)
                val backgroundColor: Int? = ta?.getColor(R.styleable.SnackbarStyle_android_background, 0)

                view.setBackgroundColor(backgroundColor!!)

                val tv = android.support.design.R.id.snackbar_text as TextView
                tv.setTextColor(textColor!!)

                snackbar.show()

                ta.recycle()
            }
        }

        @JvmStatic
        @BindingAdapter("recyclerConfig")
        fun bindRecyclerViewConfiguration(recyclerView: RecyclerView?, recyclerViewConfig: RecyclerViewConfiguration?) {
            recyclerView?.layoutManager = recyclerViewConfig?.layoutManager
            recyclerView?.adapter = recyclerViewConfig?.adapter
        }

        @JvmStatic
        @BindingAdapter("emptyViewConfig")
        fun bindEmptyViewConfiguration(multiStateView: MultiStateView?, emptyViewConfiguration: EmptyViewConfiguration?) {
            multiStateView?.setEmptyViewConfiguration(emptyViewConfiguration)
        }

        @JvmStatic
        @BindingAdapter("errorViewConfig")
        fun bindErrorViewConfiguration(multiStateView: MultiStateView?, errorViewConfiguration: ErrorViewConfiguration?) {
            multiStateView?.setErrorViewConfiguration(errorViewConfiguration)
        }


        @JvmStatic
        @BindingAdapter("toolbarConfig")
        fun bindToolbarConfiguration(toolbar: Toolbar?, toolbarConfiguration: ToolbarConfiguration?) {
            toolbar?.navigationIcon = toolbarConfiguration?.navigationIcon
            toolbar?.title = toolbarConfiguration?.title
            toolbar?.setTitleTextColor(toolbarConfiguration?.titleColor!!)
            toolbar?.setNavigationOnClickListener(toolbarConfiguration?.clickListener)
            if(toolbarConfiguration?.menu != 0) {
                toolbar?.inflateMenu(toolbarConfiguration?.menu!!)
            }
            toolbar?.setOnMenuItemClickListener(toolbarConfiguration?.onMenuItemClickListener)
        }


        @JvmStatic
        @BindingAdapter("editTextConfig")
        fun bindEditTextConfiguration(appCompatEditText: AppCompatEditText?, editTextConfiguration: EditTextConfiguration?){
            appCompatEditText?.addTextChangedListener(editTextConfiguration?.textWatcher)
        }
    }
}

