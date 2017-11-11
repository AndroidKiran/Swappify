package swapp.items.com.swappify.controllers.country.viewmodel

import android.databinding.ObservableField
import swapp.items.com.swappify.controllers.country.model.Countries


class CountryItemViewModel constructor(val country: Countries.Country?, private val countryItemViewModelListener: CountryItemViewModelListener?) {

    var isoCode: ObservableField<String> = ObservableField<String>(country?.isoCode)

    var countryName: ObservableField<String> = ObservableField<String>(country?.name)

    fun onItemClick() {
        countryItemViewModelListener?.onItemClick(country!!)
    }

    interface CountryItemViewModelListener {
        fun onItemClick(country: Countries.Country?)
    }
}