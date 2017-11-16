package swapp.items.com.swappify.controllers.country.module

import android.support.v7.widget.LinearLayoutManager
import dagger.Module
import dagger.Provides
import swapp.items.com.swappify.controllers.country.ui.CountryAdapter
import swapp.items.com.swappify.controllers.country.ui.CountryPickerFragment
import swapp.items.com.swappify.controllers.country.ui.ICountryPickerNavigator
import swapp.items.com.swappify.injection.scopes.PerFragment

@Module
class CountryPickerModule {

    @PerFragment
    @Provides
    fun provideCountryAdapter(): CountryAdapter<ICountryPickerNavigator> =
            CountryAdapter<ICountryPickerNavigator>()

    @PerFragment
    @Provides
    fun provideLinearLayoutManager(countryPickerFragment: CountryPickerFragment): LinearLayoutManager =
            LinearLayoutManager(countryPickerFragment.context)

}