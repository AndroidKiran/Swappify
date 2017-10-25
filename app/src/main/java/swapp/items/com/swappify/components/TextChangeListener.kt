package swapp.items.com.swappify.components

import android.text.Editable
import android.text.TextWatcher

class TextChangeListener constructor(private val textChangerType: TextChangerType?,
                                     private val onTextChangeListener: OnTextChangeListener?): TextWatcher {

    override fun beforeTextChanged(charSequence: CharSequence?, start: Int, count: Int, after: Int) {
        onTextChangeListener?.beforeTextChanged(textChangerType, charSequence, start, count, after)
    }

    override fun onTextChanged(charSequence: CharSequence?, start: Int, before: Int, count: Int) {
        onTextChangeListener?.onTextChanged(textChangerType, charSequence, start, before, count)
    }

    override fun afterTextChanged(editable: Editable?) {
        onTextChangeListener?.afterTextChanged(textChangerType, editable)
    }


    enum class TextChangerType {
        LOGIN_PHONE_NUM
    }

    interface OnTextChangeListener {

        fun beforeTextChanged(textChangerType: TextChangerType?, charSequence: CharSequence?,
                              start: Int, count: Int, after: Int)

        fun onTextChanged(textChangerType: TextChangerType?, charSequence: CharSequence?,
                          start: Int, before: Int, count: Int)

        fun afterTextChanged(textChangerType: TextChangerType?, editable: Editable?)
    }

}
