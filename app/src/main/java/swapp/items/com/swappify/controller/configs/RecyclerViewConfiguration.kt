package swapp.items.com.swappify.controller.configs

import android.databinding.BaseObservable
import android.databinding.Bindable
import android.databinding.BindingAdapter
import android.support.v7.widget.RecyclerView

class RecyclerViewConfiguration : BaseObservable() {

    companion object {
        @JvmStatic
        @BindingAdapter("recyclerBinding")
        fun bindRecyclerViewConfiguration(recyclerView: RecyclerView?, recyclerViewConfig: RecyclerViewConfiguration?) {
            recyclerView?.layoutManager = recyclerViewConfig?.layoutManager
            recyclerView?.adapter = recyclerViewConfig?.recyclerAdapter
        }
    }

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

    fun newState(recyclerAdapter: RecyclerView.Adapter<*>?): Builder = Builder(recyclerAdapter)

    inner class Builder constructor(private val recyclerAdapter: RecyclerView.Adapter<*>?) {

        private var layoutManager: RecyclerView.LayoutManager? = null

        fun setLayoutManger(layoutManager: RecyclerView.LayoutManager?): Builder {
            this.layoutManager = layoutManager
            return this@Builder
        }

        fun commit() {
            this@RecyclerViewConfiguration.setRecyclerConfig(recyclerAdapter, layoutManager)
        }

    }

    fun setRecyclerConfig(recyclerAdapter: RecyclerView.Adapter<*>?, layoutManager: RecyclerView.LayoutManager?) {
        this.layoutManager = layoutManager
        this.recyclerAdapter = recyclerAdapter
        notifyChange()
    }

}

