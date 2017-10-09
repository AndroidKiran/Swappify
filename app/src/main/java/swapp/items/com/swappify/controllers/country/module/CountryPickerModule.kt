package swapp.items.com.swappify.controllers.country.module

import android.content.Context
import android.support.v7.widget.LinearLayoutManager
import dagger.Module
import dagger.Provides
import swapp.items.com.swappify.controllers.country.CountryAdapter
import swapp.items.com.swappify.controllers.country.CountryPickerFragment
import swapp.items.com.swappify.controllers.country.CountryPickerNavigator
import swapp.items.com.swappify.controllers.country.viewmodel.CountryPickerViewModel
import swapp.items.com.swappify.data.DataManagerHelper

@Module
class CountryPickerModule {

    @Provides
    internal fun provideViewModel(dataManagerHelper: DataManagerHelper, context: Context): CountryPickerViewModel =
            CountryPickerViewModel(dataManagerHelper = dataManagerHelper, application = context)

    @Provides
    internal fun provideCountryAdapter(): CountryAdapter<CountryPickerNavigator> = CountryAdapter<CountryPickerNavigator>()

    @Provides
    internal fun provideLinearLayoutManager(countryPickerFragment: CountryPickerFragment): LinearLayoutManager =
            LinearLayoutManager(countryPickerFragment.activity)
}