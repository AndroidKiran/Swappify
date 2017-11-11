package swapp.items.com.swappify.injection.builder

import dagger.Module
import dagger.android.ContributesAndroidInjector
import swapp.items.com.swappify.controllers.additem.module.AddItemProviderModule
import swapp.items.com.swappify.controllers.additem.ui.AddItemActivity
import swapp.items.com.swappify.controllers.country.module.CountryPickerModule
import swapp.items.com.swappify.controllers.country.module.CountryPickerProviderModule
import swapp.items.com.swappify.controllers.signup.ui.SignUpLoginActivity
import swapp.items.com.swappify.injection.scopes.PerActivity


@Module
abstract class ActivityBuilder {

    @PerActivity
    @ContributesAndroidInjector(modules = arrayOf(CountryPickerModule::class,
            CountryPickerProviderModule::class))
    abstract fun provideSignUpLogInActivity(): SignUpLoginActivity

    @PerActivity
    @ContributesAndroidInjector(modules = arrayOf(AddItemProviderModule::class))
    abstract fun provideAddItemActivity(): AddItemActivity

}