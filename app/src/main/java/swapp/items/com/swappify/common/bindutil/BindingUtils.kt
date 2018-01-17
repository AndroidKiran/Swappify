package swapp.items.com.swappify.common.bindutil

import android.databinding.BindingAdapter
import android.graphics.drawable.Drawable
import android.support.design.widget.TextInputLayout
import android.support.v7.widget.AppCompatEditText
import android.support.v7.widget.AppCompatImageView
import android.support.v7.widget.AppCompatTextView
import fr.ganfra.materialspinner.MaterialSpinner
import swapp.items.com.swappify.R
import swapp.items.com.swappify.common.AppUtils
import swapp.items.com.swappify.components.glide.GlideApp
import swapp.items.com.swappify.controllers.configs.EditTextConfiguration
import swapp.items.com.swappify.controllers.signup.viewmodel.LogInViewModel

class BindingUtils {

    companion object {

        @JvmStatic
        @BindingAdapter(value = ["imageUrl", "placeHolder"], requireAll = true)
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
        @BindingAdapter(value = "imageDrawable")
        fun bindImageDrawable(imageView: AppCompatImageView?, imageDrawable: Drawable?) {
            if (imageView == null || imageDrawable == null) return
            imageView.setImageDrawable(imageDrawable)
        }

        @JvmStatic
        @BindingAdapter(value = "imageResource")
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

        @JvmStatic
        @BindingAdapter(value = "loginHeaderBinding")
        fun bindLoginHeader(textView: AppCompatTextView, state: LogInViewModel.State?) {
            val text = when (state) {

                LogInViewModel.State.STATE_OTP_VERIFICATION-> textView.context.getString(R.string.str_verify_mob_num)

                LogInViewModel.State.STATE_AUTO_VERIFICATION -> textView.context.getString(R.string.str_verifying_mob_num)

                else -> textView.context.getString(R.string.str_enter_mob_num)
            }
            textView.text = text
        }


        @JvmStatic
        @BindingAdapter(value = "loginSubHeaderBinding")
        fun bindLoginSubHeader(textView: AppCompatTextView, state: LogInViewModel.State?) {
            val text = when (state) {

                LogInViewModel.State.STATE_OTP_VERIFICATION -> textView.context.getString(R.string.str_enter_otp_msg)

                LogInViewModel.State.STATE_AUTO_VERIFICATION -> textView.context.getString(R.string.str_sent_otp_msg)

                else -> textView.context.getString(R.string.str_send_otp_msg)
            }
            textView.text = text
        }
    }
}

