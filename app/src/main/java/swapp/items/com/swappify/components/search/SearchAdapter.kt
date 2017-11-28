package swapp.items.com.swappify.components.search

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.ViewGroup
import swapp.items.com.swappify.BR
import swapp.items.com.swappify.controllers.base.BaseViewHolder
import swapp.items.com.swappify.databinding.SearchItemBinding

class SearchAdapter : RecyclerView.Adapter<BaseViewHolder>() {

    val queryList = arrayListOf<SearchItem>()

    override fun onCreateViewHolder(parent: ViewGroup?, viewType: Int): BaseViewHolder {
        val searchItemBinding = SearchItemBinding.inflate(
                LayoutInflater.from(parent?.context),
                parent, false)
        return SearchViewHolder(searchItemBinding)
    }

    override fun onBindViewHolder(holder: BaseViewHolder?, position: Int) {
        holder?.onBind(position)
    }

    override fun getItemCount(): Int = queryList.size


    inner class SearchViewHolder constructor(private val binding: SearchItemBinding) : BaseViewHolder(binding.root) {

        override fun onBind(position: Int) {
            val searchItem = queryList[position]
            binding.setVariable(BR.searchItem, searchItem)
            binding.executePendingBindings()
        }

    }
}