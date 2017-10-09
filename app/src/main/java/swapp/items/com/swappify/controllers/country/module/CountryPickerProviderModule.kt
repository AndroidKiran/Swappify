package swapp.items.com.swappify.controllers.country.module

import dagger.Module
import dagger.android.ContributesAndroidInjector
import swapp.items.com.swappify.controllers.country.CountryPickerFragment

/**
 * Created by ravi on 05/10/17.
 */

@Module
abstract class CountryPickerProviderModule {

    @ContributesAndroidInjector(modules = arrayOf(CountryPickerModule::class))
    abstract fun provideCountryPickerFragment(): CountryPickerFragment
}