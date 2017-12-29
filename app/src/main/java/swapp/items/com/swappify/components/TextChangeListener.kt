package swapp.items.com.swappify.components

import android.text.Editable
import android.text.TextWatcher

abstract class TextChangeListener: TextWatcher {

    override fun beforeTextChanged(charSequence: CharSequence?, start: Int, count: Int, after: Int) {
    }

    override fun onTextChanged(charSequence: CharSequence?, start: Int, before: Int, count: Int) {
    }

    override fun afterTextChanged(editable: Editable?) {
        afterTextChanged(editable.toString())
    }

    abstract fun afterTextChanged(newValue: String?)
}
