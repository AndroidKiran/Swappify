package swapp.items.com.swappify.controller.signup.module

import android.arch.lifecycle.ViewModel
import dagger.Binds
import dagger.Module
import dagger.android.ContributesAndroidInjector
import dagger.multibindings.IntoMap
import swapp.items.com.swappify.controller.country.module.CountryPickerModule
import swapp.items.com.swappify.controller.country.ui.CountryPickerFragment
import swapp.items.com.swappify.controller.country.viewmodel.CountryPickerViewModel
import swapp.items.com.swappify.controller.signup.ui.OtpVerificationFragment
import swapp.items.com.swappify.controller.signup.viewmodel.LogInViewModel
import swapp.items.com.swappify.injection.qualifiers.ViewModelKey
import swapp.items.com.swappify.injection.scopes.PerFragment

@Module
abstract class LoginActivityProviderModule {

    @Binds
    @IntoMap
    @ViewModelKey(LogInViewModel::class)
    abstract fun bindSignUpLogInViewModel(viewModel: LogInViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(CountryPickerViewModel::class)
    abstract fun bindCountryPickerViewModel(viewModel: CountryPickerViewModel): ViewModel

    @PerFragment
    @ContributesAndroidInjector(modules = [(CountryPickerModule::class)])
    abstract fun provideCountryPickerFragment(): CountryPickerFragment

    @PerFragment
    @ContributesAndroidInjector
    abstract fun provideOtpVerifyFragment(): OtpVerificationFragment
}
