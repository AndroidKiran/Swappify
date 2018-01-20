package swapp.items.com.swappify.controller.configs

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
        @BindingAdapter("snackBarBinding")
        fun bindSnackBarText(layout: View?, config: SnackbarConfiguration?) {
            if (config?.snackBarMsg != null) {
                val snackbar = Snackbar.make(layout!!, config.snackBarMsg!!, config.snackBarDuration)
                val view = snackbar.view

                val ta = layout.context?.obtainStyledAttributes(
                        config.snackBarType!!.styleId,
                        R.styleable.SnackbarStyle
                )

                val textColor = ta?.getColor(R.styleable.SnackbarStyle_android_textColor, 0)
                val backgroundColor: Int? = ta?.getColor(R.styleable.SnackbarStyle_android_background, 0)

                view.setBackgroundColor(backgroundColor!!)

                val tv = view.findViewById(android.support.design.R.id.snackbar_text) as TextView
                tv.setTextColor(textColor!!)

                if (!config.snackBarAction.isNullOrEmpty() && config.actionClickListener != null) {
                    snackbar.setAction(config.snackBarAction, config.actionClickListener)
                }

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

    @get:Bindable
    var snackBarAction: CharSequence? = null
        private set(value) {
            field = value
        }

    @get:Bindable
    var actionClickListener: View.OnClickListener? = null
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
        private var action: CharSequence? = null
        private var actionListener: View.OnClickListener? = null

        fun setDuration(duration: Int): Builder {
            this.duration = duration
            return this@Builder
        }

        fun setType(type: Type): Builder {
            this.type = type
            return this@Builder
        }

        fun setAction(action: CharSequence?): Builder {
            this.action = action
            return this@Builder
        }

        fun setActionListener(actionListener: View.OnClickListener?): Builder {
            this.actionListener = actionListener
            return this@Builder
        }

        fun commit() {
            this@SnackbarConfiguration.setConfig(msg, type, duration, action, actionListener)
        }
    }

    private fun setConfig(snackBarMsg: CharSequence, snackBarType: Type, snackBarDuration: Int, snackBarAction: CharSequence?, actionClickListener: View.OnClickListener?) {
        this.snackBarMsg = snackBarMsg
        this.snackBarType = snackBarType
        this.snackBarDuration = snackBarDuration
        this.snackBarAction = snackBarAction
        this.actionClickListener = actionClickListener
        notifyChange()
    }
}