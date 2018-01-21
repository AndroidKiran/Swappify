package swapp.items.com.swappify.injection.builder

import android.arch.lifecycle.ViewModelProvider
import dagger.Binds
import dagger.Module
import dagger.android.ContributesAndroidInjector
import swapp.items.com.swappify.controller.addgame.module.AddGameActivityModule
import swapp.items.com.swappify.controller.addgame.module.AddGameActivityProviderModule
import swapp.items.com.swappify.controller.addgame.ui.AddGameActivity
import swapp.items.com.swappify.controller.home.module.HomeActivityModule
import swapp.items.com.swappify.controller.home.module.HomeActivityProviderModule
import swapp.items.com.swappify.controller.home.ui.HomeActivity
import swapp.items.com.swappify.controller.intro.module.IntroActivityProviderModule
import swapp.items.com.swappify.controller.intro.ui.IntroActivity
import swapp.items.com.swappify.controller.launcher.LauncherActivity
import swapp.items.com.swappify.controller.launcher.module.LauncherActivityProviderModule
import swapp.items.com.swappify.controller.profile.module.EditProfileProviderModule
import swapp.items.com.swappify.controller.profile.ui.EditProfileActivity
import swapp.items.com.swappify.controller.signup.module.LoginActivityProviderModule
import swapp.items.com.swappify.controller.signup.ui.LoginActivity
import swapp.items.com.swappify.injection.scopes.PerActivity
import swapp.items.com.swappify.mvvm.ViewModelFactory


@Module
abstract class ActivityBuilder {

    @Binds
    abstract fun bindViewModelFactory(factory: ViewModelFactory): ViewModelProvider.Factory

    @PerActivity
    @ContributesAndroidInjector(modules = [(LauncherActivityProviderModule::class)])
    abstract fun provideLauncherActivity(): LauncherActivity

    @PerActivity
    @ContributesAndroidInjector(modules = [(IntroActivityProviderModule::class)])
    abstract fun provideIntroActivity(): IntroActivity

    @PerActivity
    @ContributesAndroidInjector(modules = [(EditProfileProviderModule::class)])
    abstract fun provideEditProfileActivity(): EditProfileActivity

    @PerActivity
    @ContributesAndroidInjector(modules = [(HomeActivityProviderModule::class), (HomeActivityModule::class)])
    abstract fun provideHomeActivity(): HomeActivity

    @PerActivity
    @ContributesAndroidInjector(modules = [(LoginActivityProviderModule::class)])
    abstract fun provideLogInActivity(): LoginActivity

    @PerActivity
    @ContributesAndroidInjector(modules = [(AddGameActivityModule::class), (AddGameActivityProviderModule::class)])
    abstract fun provideAddItemActivity(): AddGameActivity

}