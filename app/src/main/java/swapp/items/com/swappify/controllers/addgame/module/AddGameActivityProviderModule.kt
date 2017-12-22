package swapp.items.com.swappify.controllers.addgame.module

import android.arch.lifecycle.ViewModel
import dagger.Binds
import dagger.Module
import dagger.android.ContributesAndroidInjector
import dagger.multibindings.IntoMap
import swapp.items.com.swappify.controllers.addgame.ui.AddGameFragment
import swapp.items.com.swappify.controllers.addgame.ui.SearchGameFragment
import swapp.items.com.swappify.controllers.addgame.viewmodel.AddGameViewModel
import swapp.items.com.swappify.injection.qualifiers.ViewModelKey
import swapp.items.com.swappify.injection.scopes.PerFragment

@Module
abstract class AddGameActivityProviderModule {

    @Binds
    @IntoMap
    @ViewModelKey(AddGameViewModel::class)
    abstract fun bindAddItemViewModel(viewModel: AddGameViewModel): ViewModel

    @PerFragment
    @ContributesAndroidInjector()
    abstract fun provideAddGameFragment(): AddGameFragment

    @PerFragment
    @ContributesAndroidInjector(modules = [(SearchGameFragmentModule::class)])
    abstract fun provideSearchGameFragment(): SearchGameFragment
}