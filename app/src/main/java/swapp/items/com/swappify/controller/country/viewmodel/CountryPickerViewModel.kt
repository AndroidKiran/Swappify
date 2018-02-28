package swapp.items.com.swappify.controller.country.viewmodel

import android.arch.lifecycle.MutableLiveData
import com.google.gson.Gson
import io.reactivex.BackpressureStrategy
import io.reactivex.Flowable
import io.reactivex.FlowableEmitter
import swapp.items.com.swappify.common.AppUtils
import swapp.items.com.swappify.common.extension.switchMap
import swapp.items.com.swappify.common.extension.toLiveData
import swapp.items.com.swappify.controller.SwapApplication
import swapp.items.com.swappify.controller.base.BaseViewModel
import swapp.items.com.swappify.controller.country.model.Countries
import swapp.items.com.swappify.controller.country.model.Country
import javax.inject.Inject

class CountryPickerViewModel @Inject constructor(countryPickerDataManager: CountryPickerDataManager, application: SwapApplication) : BaseViewModel(application) {

    private val jsonPath: String = "json/countries.json"

    private val appUtilDataManager = countryPickerDataManager.appUtilManager

    private val gson: Gson = appUtilDataManager.gson

    var reload: MutableLiveData<Boolean>? = null

    var countriesLiveData = reload?.switchMap {
        if (!it) {
            MutableLiveData<List<Country>>()
        } else {
            getCountryList()
        }
    }!!

    init {
        reload?.value = true
    }

    private fun getCountryList() = Flowable.create({ emitter: FlowableEmitter<List<Country>> ->
        val countries: Countries? = gson.fromJson(AppUtils.loadJSONFromAsset(
                getApplication<SwapApplication>(), jsonPath),
                Countries::class.java
        )
        if (countries!!.countries.isNotEmpty()) {
            emitter.onNext(countries.countries)
            emitter.onComplete()
        } else {
            emitter.onError(Throwable("No data present"))
        }
    }, BackpressureStrategy.MISSING)
            .onErrorResumeNext(Flowable.empty())
            .toLiveData()

}