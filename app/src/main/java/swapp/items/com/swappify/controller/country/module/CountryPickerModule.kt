package swapp.items.com.swappify.controller.country.module

import android.support.v7.widget.LinearLayoutManager
import dagger.Module
import dagger.Provides
import swapp.items.com.swappify.controller.country.ui.CountryAdapter
import swapp.items.com.swappify.controller.country.ui.CountryPickerFragment
import swapp.items.com.swappify.injection.scopes.PerFragment

@Module
class CountryPickerModule {

    @PerFragment
    @Provides
    fun provideCountryAdapter(): CountryAdapter<CountryAdapter.CountryItemViewModelListener> =
            CountryAdapter()

    @PerFragment
    @Provides
    fun provideLinearLayoutManager(countryPickerFragment: CountryPickerFragment): LinearLayoutManager =
            LinearLayoutManager(countryPickerFragment.context)

}