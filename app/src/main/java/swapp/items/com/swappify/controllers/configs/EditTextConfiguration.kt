package swapp.items.com.swappify.controllers.configs

import android.databinding.BaseObservable
import android.databinding.Bindable
import swapp.items.com.swappify.components.TextChangeListener

class EditTextConfiguration : BaseObservable() {

    @get:Bindable
    var textWatcher: TextChangeListener? = null
        private set (value) {
            field = value
        }

    fun setEditTextConfig(textWatcher: TextChangeListener?) {
        this.textWatcher = textWatcher
        notifyChange()
    }

}
