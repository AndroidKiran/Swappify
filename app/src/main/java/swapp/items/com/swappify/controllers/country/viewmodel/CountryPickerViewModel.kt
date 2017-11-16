package swapp.items.com.swappify.controllers.country.viewmodel

import android.arch.lifecycle.MutableLiveData
import com.google.gson.Gson
import swapp.items.com.swappify.controllers.SwapApplication
import swapp.items.com.swappify.controllers.base.BaseViewModel
import swapp.items.com.swappify.controllers.country.model.Countries
import swapp.items.com.swappify.controllers.country.ui.ICountryPickerNavigator
import swapp.items.com.swappify.injection.scopes.PerActivity
import swapp.items.com.swappify.utils.AppUtils
import javax.inject.Inject

@PerActivity
class CountryPickerViewModel @Inject constructor(countryPickerDataManager: CountryPickerDataManager, application: SwapApplication) : BaseViewModel<ICountryPickerNavigator>(application) {

    private val jsonPath: String = "json/countries.json"

    private var countriesLiveData: MutableLiveData<ArrayList<Countries.Country>> = MutableLiveData()

    private val appUtilDataManager = countryPickerDataManager.appUtilManager

    private val gson: Gson = appUtilDataManager.gson

    fun fetchCountries() {
        val countries: Countries? = gson.fromJson(AppUtils.loadJSONFromAsset(
                getApplication<SwapApplication>(), jsonPath),
                Countries::class.java
        )
        countriesLiveData.value = countries?.countries
    }

    fun getCountriesLiveData(): MutableLiveData<ArrayList<Countries.Country>> = countriesLiveData
}