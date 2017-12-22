package swapp.items.com.swappify.injection.builder

import android.arch.lifecycle.ViewModelProvider
import dagger.Binds
import dagger.Module
import dagger.android.ContributesAndroidInjector
import swapp.items.com.swappify.controllers.ViewModelFactory
import swapp.items.com.swappify.controllers.addgame.module.AddGameActivityModule
import swapp.items.com.swappify.controllers.addgame.module.AddGameActivityProviderModule
import swapp.items.com.swappify.controllers.addgame.ui.AddGameActivity
import swapp.items.com.swappify.controllers.country.module.CountryPickerModule
import swapp.items.com.swappify.controllers.country.module.CountryPickerProviderModule
import swapp.items.com.swappify.controllers.signup.ui.SignUpLoginActivity
import swapp.items.com.swappify.injection.scopes.PerActivity


@Module
abstract class ActivityBuilder {

    @Binds
    abstract fun bindViewModelFactory(factory: ViewModelFactory): ViewModelProvider.Factory

    @PerActivity
    @ContributesAndroidInjector(modules = arrayOf(CountryPickerModule::class,
            CountryPickerProviderModule::class))
    abstract fun provideSignUpLogInActivity(): SignUpLoginActivity

    @PerActivity
    @ContributesAndroidInjector(modules = arrayOf(AddGameActivityModule::class, AddGameActivityProviderModule::class))
    abstract fun provideAddItemActivity(): AddGameActivity

}