package swapp.items.com.swappify.controllers.country.viewmodel

import android.content.Context
import android.databinding.ObservableArrayList
import swapp.items.com.swappify.controllers.base.BaseAndroidViewModel
import swapp.items.com.swappify.controllers.country.CountryPickerNavigator
import swapp.items.com.swappify.controllers.country.model.Countries
import swapp.items.com.swappify.data.AppDataManager
import swapp.items.com.swappify.utils.AppUtils


class CountryPickerViewModel constructor(dataManager: AppDataManager, application: Context) : BaseAndroidViewModel<CountryPickerNavigator>(dataManager, application) {

    val jsonPath: String = "json/countries.json"

    var countriesLiveData: ObservableArrayList<Countries.Country>? = ObservableArrayList()

    fun fetchCountries() {
        if (countriesLiveData!!.isEmpty()) {
            val countries: Countries? = dataManager.gson?.fromJson(AppUtils.loadJSONFromAsset(
                    context = context, assetPath = jsonPath),
                    Countries::class.java
            )
            countriesLiveData?.addAll(countries?.countries!!)
        }
        getNavigator()?.updateAdapter()
    }

}