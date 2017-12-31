package swapp.items.com.swappify.common.bindutil

import android.databinding.BindingAdapter
import android.graphics.drawable.Drawable
import android.support.design.widget.TextInputLayout
import android.support.v7.widget.AppCompatEditText
import android.support.v7.widget.AppCompatImageView
import android.widget.Spinner
import fr.ganfra.materialspinner.MaterialSpinner
import swapp.items.com.swappify.common.AppUtils
import swapp.items.com.swappify.components.glide.GlideApp
import swapp.items.com.swappify.controllers.configs.EditTextConfiguration

class BindingUtils {

    companion object {

        @JvmStatic
        @BindingAdapter(value = *arrayOf("imageUrl", "placeHolder"), requireAll = true)
        fun bindLoadImage(imageView: AppCompatImageView?, url: String?, placeHolder: Drawable?) {
            if (AppUtils.isEmpty(url) || placeHolder == null) return
            val context = imageView?.context
            GlideApp.with(context)
                    .load(url)
                    .placeholder(placeHolder)
                    .into(imageView)
        }

        @JvmStatic
        @BindingAdapter(value = "imageUrl")
        fun bindLoadImage(imageView: AppCompatImageView?, url: String?) {
            if (AppUtils.isEmpty(url)) return
            val context = imageView?.context
            GlideApp.with(context)
                    .load(url)
                    .into(imageView)
        }

        @JvmStatic
        @BindingAdapter(value ="imageDrawable")
        fun bindImageDrawable(imageView: AppCompatImageView?, imageDrawable: Drawable?) {
            if (imageView == null || imageDrawable == null) return
            imageView.setImageDrawable(imageDrawable)
        }

        @JvmStatic
        @BindingAdapter(value ="imageResource")
        fun bindImageResource(imageView: AppCompatImageView?, imageResource: Int) {
            if (imageView == null || imageResource == 0) return
            imageView.setImageResource(imageResource)
        }

        @JvmStatic
        @BindingAdapter(value = "editTextConfig")
        fun bindEditTextConfiguration(appCompatEditText: AppCompatEditText?, editTextConfiguration: EditTextConfiguration?) {
            appCompatEditText?.addTextChangedListener(editTextConfiguration?.textWatcher)
        }

        @JvmStatic
        @BindingAdapter(value = "textInputLayoutErrorBinding")
        fun bindTextInputLayoutError(textInputLayout: TextInputLayout?, error: String?) {
            textInputLayout?.error = error
        }


        @JvmStatic
        @BindingAdapter(value = "spinnerErrorBinding")
        fun bindSpinnerError(spinner: MaterialSpinner?, error: String?) {
            spinner?.error = error
        }
    }
}

