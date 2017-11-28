package swapp.items.com.swappify.utils

import android.databinding.BindingAdapter
import android.graphics.drawable.Drawable
import android.support.design.widget.TextInputLayout
import android.support.v7.widget.AppCompatEditText
import android.support.v7.widget.AppCompatImageView
import android.support.v7.widget.RecyclerView
import swapp.items.com.swappify.components.glide.GlideApp
import swapp.items.com.swappify.controllers.configs.EditTextConfiguration
import swapp.items.com.swappify.controllers.configs.RecyclerViewConfiguration

class BindingUtils {

    companion object {

        @JvmStatic
        @BindingAdapter(value = *arrayOf("imageUrl", "placeHolder"), requireAll = true)
        fun bindLoadImage(imageView: AppCompatImageView?, url: String?, placeHolder: Drawable?) {
            val context = imageView?.context
            GlideApp.with(context)
                    .load(url)
                    .placeholder(placeHolder)
                    .into(imageView)
        }

        @JvmStatic
        @BindingAdapter("imageDrawable")
        fun bindImageDrawable(imageView: AppCompatImageView?, imageDrawable: Drawable?) {
            imageView?.setImageDrawable(imageDrawable)
        }

        @JvmStatic
        @BindingAdapter("recyclerBinding")
        fun bindRecyclerViewConfiguration(recyclerView: RecyclerView?, recyclerViewConfig: RecyclerViewConfiguration?) {
            recyclerView?.layoutManager = recyclerViewConfig?.layoutManager
            recyclerView?.adapter = recyclerViewConfig?.recyclerAdapter
        }

        @JvmStatic
        @BindingAdapter("editTextConfig")
        fun bindEditTextConfiguration(appCompatEditText: AppCompatEditText?, editTextConfiguration: EditTextConfiguration?) {
            appCompatEditText?.addTextChangedListener(editTextConfiguration?.textWatcher)
        }

        @JvmStatic
        @BindingAdapter("textInputLayoutErrorBinding")
        fun bindTextInputLayoutError(textInputLayout: TextInputLayout?, error: String?) {
            textInputLayout?.error = error
        }
    }
}

