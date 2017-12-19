package swapp.items.com.swappify.components.search

import android.databinding.BindingAdapter
import android.databinding.ObservableField
import swapp.items.com.swappify.controllers.configs.RecyclerViewConfiguration

object SearchDatabindingUtils {

    @JvmStatic
    @BindingAdapter("searchOnQueryChangeListenerBinding")
    fun bindSearchOnQueryChangeListener(searchView: SearchView, searchOnQueryChangeListener: ISearchOnQueryChangeListener?) {
        if (searchOnQueryChangeListener != null) {
            searchView.setOnQueryTextListener(searchOnQueryChangeListener)
        }
    }

    @JvmStatic
    @BindingAdapter("searchOnClickListenerBinding")
    fun bindSearchOnClickListener(searchView: SearchView, searchOnClickListener: ISearchOnClickListener?) {
        if (searchOnClickListener != null) {
            searchView.setOnClickListeners(searchOnClickListener)
        }
    }

    @JvmStatic
    @BindingAdapter("searchRecyclerBinding")
    fun bindSearchRecyclerViewConfiguration(searchView: SearchView, recyclerViewConfig: RecyclerViewConfiguration?) {
        if (recyclerViewConfig?.recyclerAdapter != null) {
            searchView.setAdapter(recyclerViewConfig.recyclerAdapter!!)
        }
    }

    @JvmStatic
    @BindingAdapter("searchQuery")
    fun bindSearchText(searchView: SearchView, text: ObservableField<String>) {
        searchView.setTextInput(text.get())
    }

}
