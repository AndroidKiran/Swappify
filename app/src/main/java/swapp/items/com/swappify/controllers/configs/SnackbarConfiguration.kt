package swapp.items.com.swappify.controllers.configs

import android.databinding.BaseObservable
import android.databinding.Bindable
import android.support.annotation.StyleRes
import android.support.design.widget.Snackbar
import swapp.items.com.swappify.R

class SnackbarConfiguration : BaseObservable() {

    @get:Bindable
    var msg: CharSequence? = null
        private set (value) {
            field = value
        }

    @get:Bindable
    var duration: Int = 0
        private set (value) {
            field = value
        }

    @get:Bindable
    var type: Type? = null
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

    private fun setConfig(msg: CharSequence, type: Type, duration: Int) {
        this.msg = msg
        this.type = type
        this.duration = duration
        notifyChange()
    }
}