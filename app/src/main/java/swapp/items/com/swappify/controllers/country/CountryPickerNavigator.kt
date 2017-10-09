package swapp.items.com.swappify.controllers.country

import swapp.items.com.swappify.controllers.country.model.Countries

interface CountryPickerNavigator {

    fun update(countries: Countries)

    fun onCancel()

    fun onSelect(country: Countries.Country)
}