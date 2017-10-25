package swapp.items.com.swappify.controllers.country.model

import android.databinding.ObservableField


class CountryItemViewModel constructor(val country: Countries.Country?, private val countryItemViewModelListener: CountryItemViewModelListener?) {

    val isoCode: ObservableField<String> = ObservableField<String>(country?.isoCode)

    val countryName: ObservableField<String> = ObservableField<String>(country?.name)

    fun onItemClick() {
        countryItemViewModelListener?.onItemClick(country!!)
    }

    interface CountryItemViewModelListener {
        fun onItemClick(country: Countries.Country?)
    }
}