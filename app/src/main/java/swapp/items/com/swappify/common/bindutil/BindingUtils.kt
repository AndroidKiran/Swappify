package swapp.items.com.swappify.common.bindutil

import android.databinding.BindingAdapter
import android.graphics.drawable.Drawable
import android.support.design.widget.BottomNavigationView
import android.support.design.widget.TextInputLayout
import android.support.v4.view.ViewPager
import android.support.v7.widget.AppCompatImageView
import android.support.v7.widget.AppCompatTextView
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.load.resource.bitmap.CircleCrop
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import fr.ganfra.materialspinner.MaterialSpinner
import swapp.items.com.swappify.R
import swapp.items.com.swappify.components.glide.GlideApp
import swapp.items.com.swappify.components.glide.GlideOptions.bitmapTransform
import swapp.items.com.swappify.controller.signup.viewmodel.LogInViewModel


object BindingUtils {


    @JvmStatic
    @BindingAdapter(value = ["imageUrl"])
    fun bindLoadImage(imageView: AppCompatImageView?, url: String?) {
        if (url.isNullOrEmpty()) return
        val context = imageView?.context
        GlideApp.with(context!!)
                .load(url)
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .into(imageView)

    }


    @JvmStatic
    @BindingAdapter(value = ["imageUrlWithCircularTrans"])
    fun bindLoadImageWithCircularTrans(imageView: AppCompatImageView?, url: String?) {
        if (url.isNullOrEmpty()) return
        val context = imageView?.context
        GlideApp.with(context!!)
                .load(url)
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .apply(bitmapTransform(CircleCrop()))
                .into(imageView)
    }

    @JvmStatic
    @BindingAdapter(value = ["imageUrlWithRoundCornerTrans"])
    fun bindLoadImageWithRoundCornerTrans(imageView: AppCompatImageView?, url: String?) {
        if (url.isNullOrEmpty()) return
        val context = imageView?.context
        GlideApp.with(context!!)
                .load(url)
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .apply(bitmapTransform(RoundedCorners(4)))
                .into(imageView)
    }

    @JvmStatic
    @BindingAdapter(value = ["imageDrawable"])
    fun bindImageDrawable(imageView: AppCompatImageView?, imageDrawable: Drawable?) {
        if (imageView == null || imageDrawable == null) return
        imageView.setImageDrawable(imageDrawable)
    }

    @JvmStatic
    @BindingAdapter(value = ["imageResource"])
    fun bindImageResource(imageView: AppCompatImageView?, imageResource: Int) {
        if (imageView == null || imageResource == 0) return
        imageView.setImageResource(imageResource)
    }

    @JvmStatic
    @BindingAdapter(value = ["textInputLayoutErrorBinding"])
    fun bindTextInputLayoutError(textInputLayout: TextInputLayout?, error: String?) {
        textInputLayout?.error = error
    }


    @JvmStatic
    @BindingAdapter(value = ["spinnerErrorBinding"])
    fun bindSpinnerError(spinner: MaterialSpinner?, error: String?) {
        spinner?.error = error
    }

    @JvmStatic
    @BindingAdapter(value = ["loginHeaderBinding"])
    fun bindLoginHeader(textView: AppCompatTextView, state: LogInViewModel.State?) {
        val text = when (state) {

            LogInViewModel.State.STATE_OTP_VERIFICATION -> textView.context.getString(R.string.str_verify_mob_num)

            LogInViewModel.State.STATE_AUTO_VERIFICATION -> textView.context.getString(R.string.str_verifying_mob_num)

            else -> textView.context.getString(R.string.str_enter_mob_num)
        }
        textView.text = text
    }


    @JvmStatic
    @BindingAdapter(value = ["loginSubHeaderBinding"])
    fun bindLoginSubHeader(textView: AppCompatTextView, state: LogInViewModel.State?) {
        val text = when (state) {

            LogInViewModel.State.STATE_OTP_VERIFICATION -> textView.context.getString(R.string.str_enter_otp_msg)

            LogInViewModel.State.STATE_AUTO_VERIFICATION -> textView.context.getString(R.string.str_sent_otp_msg)

            else -> textView.context.getString(R.string.str_send_otp_msg)
        }
        textView.text = text
    }

    @JvmStatic
    @BindingAdapter(value = ["onNavigationItemSelectedListener"])
    fun bindOnNavigationItemSelected(bottomNavigationView: BottomNavigationView?, navigationItemSelectedListener: BottomNavigationView.OnNavigationItemSelectedListener) {
        bottomNavigationView?.setOnNavigationItemSelectedListener(navigationItemSelectedListener)
    }

    @JvmStatic
    @BindingAdapter(value = ["onPageChangeListener"])
    fun bindOnPageChangeListener(viewPager: ViewPager?, onPageChangeListener: ViewPager.OnPageChangeListener) {
        viewPager?.addOnPageChangeListener(onPageChangeListener)
    }

}

