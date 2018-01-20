package swapp.items.com.swappify.components.search

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Filter
import android.widget.Filterable
import swapp.items.com.swappify.controller.base.BaseViewHolder
import swapp.items.com.swappify.databinding.SearchItemBinding
import swapp.items.com.swappify.injection.scopes.PerActivity
import swapp.items.com.swappify.room.SearchDao
import java.util.*
import javax.inject.Inject

@PerActivity
class SearchAdapter<N : SearchAdapter.SearchViewItemListener> constructor() :
        RecyclerView.Adapter<BaseViewHolder>(), Filterable {

    var queryList:ArrayList<SearchItem>? = null
    var searchDao: SearchDao? = null
    private var clickNavigator: N? = null

    @Inject
    constructor(searchDao: SearchDao) : this() {
        this.searchDao = searchDao
        filter.filter("")
    }

    var navigator: N?
        get() = clickNavigator
        set(navigator) {
            if (clickNavigator == null) {
                clickNavigator = navigator
            }
        }

    override fun onCreateViewHolder(parent: ViewGroup?, viewType: Int): BaseViewHolder {
        val searchItemBinding = SearchItemBinding.inflate(
                LayoutInflater.from(parent?.context),
                parent, false)
        return SearchViewHolder(searchItemBinding)
    }

    override fun onBindViewHolder(holder: BaseViewHolder?, position: Int) {
        holder?.onBind(position)
    }

    override fun getItemCount(): Int = if (queryList == null) 0 else queryList?.size!!

    fun setData(searchList: ArrayList<SearchItem>) {
        if (queryList == null) {
            queryList = searchList
            notifyDataSetChanged()
        } else {
            val previousSize = queryList?.size
            val nextSize = searchList.size
            queryList = searchList
            if (previousSize == nextSize && nextSize != 0)
                notifyItemRangeChanged(0, previousSize)
            else if (previousSize!! > nextSize) {
                if (nextSize == 0)
                    notifyItemRangeRemoved(0, previousSize)
                else {
                    notifyItemRangeChanged(0, nextSize)
                    notifyItemRangeRemoved(nextSize - 1, previousSize)
                }
            } else {
                notifyItemRangeChanged(0, previousSize)
                notifyItemRangeInserted(previousSize, nextSize - previousSize)
            }
        }
    }

    fun insertSearchItem(searchItem: SearchItem?) {
        val queryItem: SearchItem? = searchDao?.getSearchItemFor(searchItem?.text)
        if (queryItem != null) {
            queryItem.count += 1
            searchDao?.updateSearchItem(queryItem)
        } else {
            searchItem!!.count += 1
            searchDao?.insert(searchItem = searchItem)
        }
    }

    inner private class SearchViewHolder constructor(private val binding: SearchItemBinding) :
            BaseViewHolder(binding.root) {


        override fun onBind(position: Int) {
            val searchItem = queryList!![position]
            binding.searchItem = searchItem
            binding.executePendingBindings()
            binding.root.setOnClickListener({
                onItemClick(searchItem)
            })
        }

        fun onItemClick(searchItem: SearchItem?) {
            insertSearchItem(searchItem)
            clickNavigator?.onItemClick(searchItem)
        }
    }

    override fun getFilter(): Filter = onFilter

    private val onFilter = object : Filter() {

        override fun performFiltering(charSequence: CharSequence?): FilterResults {
            val filterResults = Filter.FilterResults()
            var key = charSequence.toString()
            val searchResultList: List<SearchItem>?
            if (!key.isNullOrEmpty()) {
                key = key.toLowerCase(Locale.getDefault())
                searchResultList = searchDao?.getSearchResults(key)
            } else {
                searchResultList = searchDao?.getSearchResults()
            }

            if (searchResultList != null && searchResultList.isNotEmpty()) {
                filterResults.values = searchResultList
                filterResults.count = searchResultList.size
            }
            return filterResults
        }

        override fun publishResults(charSequence: CharSequence?, results: FilterResults?) {

            if (results?.count!! > 0) {
                setData(results.values as ArrayList<SearchItem>)
            }
        }
    }

    interface SearchViewItemListener {
        fun onItemClick(searchItem: SearchItem?)
    }
}