package swapp.items.com.swappify.controllers.configs

import android.databinding.BaseObservable
import android.databinding.Bindable
import android.databinding.BindingAdapter
import android.support.annotation.StyleRes
import android.support.design.widget.Snackbar
import android.view.View
import android.widget.TextView
import swapp.items.com.swappify.R

class SnackbarConfiguration : BaseObservable() {

    companion object {
        @JvmStatic
        @BindingAdapter("snackbar")
        fun bindSnackBarText(layout: View?, config: SnackbarConfiguration?) {
            if (config?.snackBarMsg != null) {
                val snackbar = Snackbar.make(layout!!, config.snackBarMsg!!, config.snackBarDuration)
                val view = snackbar.getView()

                val ta = layout.context?.obtainStyledAttributes(
                        config.snackBarType!!.styleId,
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
    }

    @get:Bindable
    var snackBarMsg: CharSequence? = null
        private set (value) {
            field = value
        }

    @get:Bindable
    var snackBarDuration: Int = 0
        private set (value) {
            field = value
        }

    @get:Bindable
    var snackBarType: Type? = null
        private set (value) {
            field = value
        }

    enum class Type constructor(@param:StyleRes @field:StyleRes val styleId: Int) {

        ERROR(R.style.Snackbar_Alert),

        VALID(R.style.Snackbar_Confirm),

        NEUTRAL(R.style.Snackbar_Neutral)
    }


    fun newState(msg: String): Builder = Builder(msg)

    inner class Builder constructor(private val msg: CharSequence) {
        private var duration = Snackbar.LENGTH_SHORT
        private var type = Type.NEUTRAL

        fun setDuration(duration: Int): Builder {
            this.duration = duration
            return this@Builder
        }

        fun setType(type: Type): Builder {
            this.type = type
            return this@Builder
        }

        fun commit() {
            this@SnackbarConfiguration.setConfig(msg, type, duration)
        }
    }

    private fun setConfig(snackBarMsg: CharSequence, snackBarType: Type, snackBarDuration: Int) {
        this.snackBarMsg = snackBarMsg
        this.snackBarType = snackBarType
        this.snackBarDuration = snackBarDuration
        notifyChange()
    }
}