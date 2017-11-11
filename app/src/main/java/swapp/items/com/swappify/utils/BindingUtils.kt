package swapp.items.com.swappify.utils

import android.databinding.BindingAdapter
import android.graphics.drawable.Drawable
import android.support.design.widget.TextInputLayout
import android.support.v7.widget.AppCompatEditText
import android.support.v7.widget.AppCompatImageView
import android.support.v7.widget.RecyclerView
import com.bumptech.glide.Glide
import swapp.items.com.swappify.controllers.configs.EditTextConfiguration
import swapp.items.com.swappify.controllers.configs.RecyclerViewConfiguration

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
        @BindingAdapter("recyclerConfig")
        fun bindRecyclerViewConfiguration(recyclerView: RecyclerView?, recyclerViewConfig: RecyclerViewConfiguration?) {
            recyclerView?.layoutManager = recyclerViewConfig?.layoutManager
            recyclerView?.adapter = recyclerViewConfig?.recyclerAdapter
        }

        @JvmStatic
        @BindingAdapter("editTextConfig")
        fun bindEditTextConfiguration(appCompatEditText: AppCompatEditText?, editTextConfiguration: EditTextConfiguration?){
            appCompatEditText?.addTextChangedListener(editTextConfiguration?.textWatcher)
        }

        @JvmStatic
        @BindingAdapter("textInputLayoutErrorText")
        fun bindTextInputLayoutError(textInputLayout: TextInputLayout?, error: String?) {
            textInputLayout?.error = error
        }
    }
}

