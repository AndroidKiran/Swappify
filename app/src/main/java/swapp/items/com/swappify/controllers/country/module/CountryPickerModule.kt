package swapp.items.com.swappify.controllers.country.module

import android.support.v7.widget.LinearLayoutManager
import dagger.Module
import dagger.Provides
import swapp.items.com.swappify.controllers.country.ui.CountryAdapter
import swapp.items.com.swappify.controllers.country.ui.CountryPickerFragment
import swapp.items.com.swappify.controllers.country.ui.CountryPickerNavigator

@Module
class CountryPickerModule {

    @Provides
    fun provideCountryAdapter(): CountryAdapter<CountryPickerNavigator> =
            CountryAdapter<CountryPickerNavigator>()

    @Provides
    fun provideLinearLayoutManager(countryPickerFragment: CountryPickerFragment): LinearLayoutManager =
            LinearLayoutManager(countryPickerFragment.context)

}