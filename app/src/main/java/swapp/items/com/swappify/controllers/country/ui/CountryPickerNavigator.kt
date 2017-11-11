package swapp.items.com.swappify.controllers.country.ui

import swapp.items.com.swappify.controllers.country.model.Countries

interface CountryPickerNavigator {

    fun onSelect(country: Countries.Country?)
}