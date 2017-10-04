package swapp.items.com.swappify.base

import android.arch.lifecycle.ViewModel
import android.arch.lifecycle.ViewModelProvider

class ViewModelProviderFactory<V: ViewModel> constructor(val viewModel: V) : ViewModelProvider.Factory {

    override fun <T : ViewModel?> create(modelClass: Class<T>?): T {
        if (modelClass != null) {
            if (modelClass.isAssignableFrom(viewModel::class.java)) {
                return viewModel as T
            }
        }
        throw IllegalArgumentException("Unknown class name")
    }
}
