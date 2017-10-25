package swapp.items.com.swappify.controllers.country

import swapp.items.com.swappify.controllers.country.model.Countries

interface CountryPickerNavigator {

    fun updateAdapter()

    fun onSelect(country: Countries.Country?)
}