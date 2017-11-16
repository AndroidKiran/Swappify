package swapp.items.com.swappify.controllers.country.ui

import swapp.items.com.swappify.controllers.country.model.Countries

interface ICountryPickerNavigator {

    fun onSelect(country: Countries.Country?)
}