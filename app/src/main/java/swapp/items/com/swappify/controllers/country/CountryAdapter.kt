package swapp.items.com.swappify.controllers.country

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.ViewGroup
import swapp.items.com.swappify.controllers.base.BaseViewHolder
import swapp.items.com.swappify.controllers.country.model.Countries
import swapp.items.com.swappify.controllers.country.model.CountryItemViewModel
import swapp.items.com.swappify.databinding.ItemCountryBinding

class CountryAdapter<N> : RecyclerView.Adapter<BaseViewHolder>() where N : CountryPickerNavigator {

    var countriesList: ArrayList<Countries.Country> = arrayListOf()

    fun setData(countries: List<Countries.Country>) {
        countriesList.addAll(countries)
        this.notifyDataSetChanged()
    }

    private var baseNavigator: N? = null

    fun setBaseNavigator(navigator: N) {
        baseNavigator = navigator
    }

    override fun onCreateViewHolder(parent: ViewGroup?, viewType: Int): BaseViewHolder {
        val itemCountryViewBinding = ItemCountryBinding.inflate(LayoutInflater.from(parent?.getContext()),
                parent, false)
        return CountryViewHolder(itemCountryViewBinding)
    }

    override fun onBindViewHolder(holder: BaseViewHolder?, position: Int) {
        holder?.onBind(position)
    }

    override fun getItemCount(): Int = countriesList.size

    inner class CountryViewHolder constructor(private val binding: ItemCountryBinding) : BaseViewHolder(binding.root), CountryItemViewModel.CountryItemViewModelListener {


        override fun onBind(position: Int) {
            val country: Countries.Country = countriesList.get(position)
            binding.itemViewModel = CountryItemViewModel(
                    country = country, countryItemViewModelListener = this@CountryViewHolder
            )
            binding.executePendingBindings()
        }

        override fun onItemClick(country: Countries.Country?) {
            baseNavigator?.onSelect(country)
        }
    }
}