package swapp.items.com.swappify.controller.home.module

import android.arch.lifecycle.ViewModel
import dagger.Binds
import dagger.Module
import dagger.android.ContributesAndroidInjector
import dagger.multibindings.IntoMap
import swapp.items.com.swappify.controller.game.ui.GamesFragment
import swapp.items.com.swappify.controller.game.viewmodel.GamesViewModel
import swapp.items.com.swappify.controller.home.viewmodel.HomeViewModel
import swapp.items.com.swappify.injection.qualifiers.ViewModelKey
import swapp.items.com.swappify.injection.scopes.PerFragment

@Module
abstract class HomeActivityProviderModule {

    @Binds
    @IntoMap
    @ViewModelKey(HomeViewModel::class)
    abstract fun provideHomeViewModel(homeViewMode: HomeViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(GamesViewModel::class)
    abstract fun provideGamesViewModel(gamesViewModel: GamesViewModel): ViewModel

    @PerFragment
    @ContributesAndroidInjector()
    abstract fun provideGamesFragment(): GamesFragment

}