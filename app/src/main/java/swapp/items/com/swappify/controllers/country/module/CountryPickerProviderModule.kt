package swapp.items.com.swappify.controllers.country.module

import android.arch.lifecycle.ViewModel
import dagger.Binds
import dagger.Module
import dagger.android.ContributesAndroidInjector
import dagger.multibindings.IntoMap
import swapp.items.com.swappify.controllers.country.ui.CountryPickerFragment
import swapp.items.com.swappify.controllers.country.viewmodel.CountryPickerViewModel
import swapp.items.com.swappify.injection.qualifiers.ViewModelKey


@Module
abstract class CountryPickerProviderModule {

    @Binds
    @IntoMap
    @ViewModelKey(CountryPickerViewModel::class)
    abstract fun bindCountryPickerViewModel(viewModel: CountryPickerViewModel): ViewModel

    @ContributesAndroidInjector(modules = arrayOf(CountryPickerModule::class))
    abstract fun provideCountryPickerFragment(): CountryPickerFragment
}