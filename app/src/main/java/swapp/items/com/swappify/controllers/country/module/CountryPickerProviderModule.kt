package swapp.items.com.swappify.controllers.country.module

import android.arch.lifecycle.ViewModel
import dagger.Binds
import dagger.Module
import dagger.android.ContributesAndroidInjector
import dagger.multibindings.IntoMap
import swapp.items.com.swappify.controllers.country.CountryPickerFragment
import swapp.items.com.swappify.controllers.country.viewmodel.CountryPickerViewModel
import swapp.items.com.swappify.injection.scopes.ViewModelKey

/**
 * Created by ravi on 05/10/17.
 */

@Module
abstract class CountryPickerProviderModule {

    @Binds
    @IntoMap
    @ViewModelKey(CountryPickerViewModel::class)
    abstract fun bindCountryPickerViewModel(viewModel: CountryPickerViewModel): ViewModel

    @ContributesAndroidInjector(modules = arrayOf(CountryPickerModule::class))
    abstract fun provideCountryPickerFragment(): CountryPickerFragment
}