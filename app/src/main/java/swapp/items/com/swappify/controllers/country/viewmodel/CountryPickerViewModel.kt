package swapp.items.com.swappify.controllers.country.viewmodel

import android.arch.lifecycle.MutableLiveData
import android.content.Context
import swapp.items.com.swappify.utils.AppUtils
import swapp.items.com.swappify.controllers.base.BaseAndroidViewModel
import swapp.items.com.swappify.controllers.country.CountryPickerNavigator
import swapp.items.com.swappify.controllers.country.model.Countries
import swapp.items.com.swappify.data.DataManagerHelper


class CountryPickerViewModel constructor(dataManagerHelper: DataManagerHelper, application: Context) : BaseAndroidViewModel<CountryPickerNavigator>(dataManagerHelper, application) {
    override fun onRetryClick() {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    val jsonPath: String = "json/countries.json"

    var countriesLiveData: MutableLiveData<ArrayList<Countries.Country>> = MutableLiveData()

    fun onCancel() {
        baseNavigator.onCancel()
    }

    fun fetchCountries() {
        val countries: Countries = dataManager.getGson().fromJson(AppUtils.loadJSONFromAsset(
                context = context, assetPath = jsonPath),
                Countries::class.java
        )
        baseNavigator.update(countries)
    }

}