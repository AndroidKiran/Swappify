package swapp.items.com.swappify.controllers.country.ui

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.ViewGroup
import swapp.items.com.swappify.controllers.base.BaseViewHolder
import swapp.items.com.swappify.controllers.country.model.Countries
import swapp.items.com.swappify.controllers.country.viewmodel.CountryItemViewModel
import swapp.items.com.swappify.databinding.ItemCountryBinding
import swapp.items.com.swappify.injection.scopes.PerFragment
import javax.inject.Inject

@PerFragment
class CountryAdapter<N : ICountryPickerNavigator> @Inject constructor() : RecyclerView.Adapter<BaseViewHolder>() {

    var countriesList: ArrayList<Countries.Country> = ArrayList()

    fun setData(countries: ArrayList<Countries.Country>) {
        countriesList.addAll(countries)
        this.notifyDataSetChanged()
    }

    private var baseNavigator: N? = null

    fun setBaseNavigator(navigator: N?) {
        baseNavigator = navigator
    }

    override fun onCreateViewHolder(parent: ViewGroup?, viewType: Int): BaseViewHolder {
        val itemCountryViewBinding = ItemCountryBinding.inflate(LayoutInflater.from(parent?.context),
                parent, false)
        return CountryViewHolder(itemCountryViewBinding)
    }

    override fun onBindViewHolder(holder: BaseViewHolder?, position: Int) {
        holder?.onBind(position)
    }

    override fun getItemCount(): Int = countriesList.size

    inner class CountryViewHolder constructor(private val binding: ItemCountryBinding?) :
            BaseViewHolder(binding!!.root),
            CountryItemViewModel.CountryItemViewModelListener {

        override fun onBind(position: Int) {
            val country: Countries.Country? = countriesList.get(position)
            binding?.itemViewModel = CountryItemViewModel(
                    country = country, countryItemViewModelListener = this@CountryViewHolder
            )
            binding?.executePendingBindings()
        }

        override fun onItemClick(country: Countries.Country?) {
            baseNavigator?.onSelect(country)
        }
    }
}