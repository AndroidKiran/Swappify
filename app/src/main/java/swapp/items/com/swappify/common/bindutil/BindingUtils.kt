package swapp.items.com.swappify.common.bindutil

import android.databinding.BindingAdapter
import android.graphics.drawable.Drawable
import android.support.design.widget.TextInputLayout
import android.support.v4.content.ContextCompat
import android.support.v7.widget.AppCompatEditText
import android.support.v7.widget.AppCompatImageView
import android.support.v7.widget.RecyclerView
import swapp.items.com.swappify.common.AppUtils
import swapp.items.com.swappify.components.glide.GlideApp
import swapp.items.com.swappify.controllers.configs.EditTextConfiguration
import swapp.items.com.swappify.controllers.configs.RecyclerViewConfiguration

class BindingUtils {

    companion object {

        @JvmStatic
        @BindingAdapter(value = *arrayOf("imageUrl", "placeHolder"), requireAll = true)
        fun bindLoadImage(imageView: AppCompatImageView?, url: String?, placeHolder: Int?) {
            if (imageView == null || AppUtils.isEmpty(url) || placeHolder == 0) return
            val context = imageView.context
            GlideApp.with(context)
                    .load(url)
                    .placeholder(ContextCompat.getDrawable(context, placeHolder!!))
                    .into(imageView)
        }

        @JvmStatic
        @BindingAdapter("imageDrawable")
        fun bindImageDrawable(imageView: AppCompatImageView?, imageDrawable: Drawable?) {
            if (imageView == null || imageDrawable == null) return
            imageView.setImageDrawable(imageDrawable)
        }

        @JvmStatic
        @BindingAdapter("imageResource")
        fun bindImageResource(imageView: AppCompatImageView?, imageResource: Int) {
            if (imageView == null || imageResource == 0) return
            imageView.setImageResource(imageResource)
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

