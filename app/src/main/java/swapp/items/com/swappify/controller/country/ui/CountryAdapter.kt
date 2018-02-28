package swapp.items.com.swappify.controller.country.ui

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.ViewGroup
import swapp.items.com.swappify.controller.base.BaseViewHolder
import swapp.items.com.swappify.controller.country.model.Country
import swapp.items.com.swappify.databinding.CountryItemBinding
import swapp.items.com.swappify.injection.scopes.PerFragment
import javax.inject.Inject

@PerFragment
class CountryAdapter<N : CountryAdapter.CountryItemViewModelListener> @Inject constructor() : RecyclerView.Adapter<BaseViewHolder>() {

    var countriesList = arrayListOf<Country>()

    private var itemNavigator: N? = null


    fun setData(countries: List<Country>) {
        countriesList.addAll(countries)
        this.notifyDataSetChanged()
    }

    var navigator: N?
        get() = itemNavigator
        set(navigator) {
            if (itemNavigator == null) {
                itemNavigator = navigator
            }
        }

    override fun onCreateViewHolder(parent: ViewGroup?, viewType: Int): BaseViewHolder {
        val itemCountryViewBinding = CountryItemBinding.inflate(LayoutInflater.from(parent?.context),
                parent, false)
        return CountryViewHolder(itemCountryViewBinding)
    }

    override fun onBindViewHolder(holder: BaseViewHolder?, position: Int) {
        holder?.onBind(position)
    }

    override fun getItemCount(): Int = countriesList.size

    inner class CountryViewHolder constructor(private val binding: CountryItemBinding) :
            BaseViewHolder(binding.root) {

        override fun onBind(position: Int) {
            val country = countriesList[position]
            binding.countryItemViewModel = country
            binding.executePendingBindings()
            binding.root.setOnClickListener({ itemNavigator?.onItemClick(country) })
        }

    }

    interface CountryItemViewModelListener {
        fun onItemClick(country: Country?)
    }
}