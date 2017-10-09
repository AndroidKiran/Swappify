package swapp.items.com.swappify.controllers.configs

import android.databinding.BaseObservable
import android.databinding.Bindable
import android.support.v7.widget.RecyclerView

class RecyclerViewConfiguration : BaseObservable() {

    @get:Bindable
    var layoutManager: RecyclerView.LayoutManager? = null
        private set(value) {
            field = value
        }

    @get:Bindable
    var adapter: RecyclerView.Adapter<*>? = null
        private set(value) {
            field = value
        }

    fun setConfig(layoutManager: RecyclerView.LayoutManager, adapter: RecyclerView.Adapter<*>) {
        this.layoutManager = layoutManager
        this.adapter = adapter
        notifyChange()
    }
}

