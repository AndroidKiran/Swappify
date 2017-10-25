package swapp.items.com.swappify.injection.builder

import android.arch.lifecycle.ViewModelProvider
import dagger.Binds
import dagger.Module
import dagger.android.ContributesAndroidInjector
import swapp.items.com.swappify.controllers.country.module.CountryPickerModule
import swapp.items.com.swappify.controllers.country.module.CountryPickerProviderModule
import swapp.items.com.swappify.controllers.signup.SignUpLoginActivity
import swapp.items.com.swappify.controllers.signup.module.SignUpLogInModule
import swapp.items.com.swappify.controllers.signup.module.SignUpLogInProviderModule
import swapp.items.com.swappify.injection.qualifiers.ViewModelFactory


@Module
abstract class ActivityBuilder {

    @Binds
    abstract fun bindViewModelFactory(factory: ViewModelFactory): ViewModelProvider.Factory

    @ContributesAndroidInjector(modules = arrayOf(SignUpLogInModule::class,
            SignUpLogInProviderModule::class,
            CountryPickerModule::class,
            CountryPickerProviderModule::class))
    abstract fun provideSignUpLogInActivity(): SignUpLoginActivity

}