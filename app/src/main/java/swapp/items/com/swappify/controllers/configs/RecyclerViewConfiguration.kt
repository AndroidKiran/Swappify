package swapp.items.com.swappify.controllers.configs

import android.databinding.BaseObservable
import android.databinding.Bindable
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView

class RecyclerViewConfiguration : BaseObservable() {

    @get:Bindable
    var layoutManager: RecyclerView.LayoutManager? = null
        private set(value) {
            field = value
        }

    @get:Bindable
    var recyclerAdapter: RecyclerView.Adapter<*>? = null
        private set(value) {
            field = value
        }

    @get:Bindable
    var recyclerOrientation: Int = LinearLayoutManager.VERTICAL
        private set(value) {
            field = value
        }

    fun setRecyclerConfig(layoutManager: RecyclerView.LayoutManager?,
                          recyclerAdapter: RecyclerView.Adapter<*>?,
                          recyclerOrientation: Int = LinearLayoutManager.VERTICAL) {
        this.layoutManager = layoutManager
        this.recyclerAdapter = recyclerAdapter
        this.recyclerOrientation = recyclerOrientation
        notifyChange()
    }
}

