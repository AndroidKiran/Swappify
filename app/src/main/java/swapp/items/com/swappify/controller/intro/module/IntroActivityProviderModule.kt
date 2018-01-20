package swapp.items.com.swappify.controller.intro.module

import android.arch.lifecycle.ViewModel
import dagger.Binds
import dagger.Module
import dagger.android.ContributesAndroidInjector
import dagger.multibindings.IntoMap
import swapp.items.com.swappify.controller.intro.ui.IntroFragment
import swapp.items.com.swappify.controller.intro.viewmodel.IntroViewModel
import swapp.items.com.swappify.injection.qualifiers.ViewModelKey
import swapp.items.com.swappify.injection.scopes.PerFragment

@Module
abstract class IntroActivityProviderModule {

    @Binds
    @IntoMap
    @ViewModelKey(IntroViewModel::class)
    abstract fun provideIntroViewModel(viewModel: IntroViewModel): ViewModel

    @PerFragment
    @ContributesAndroidInjector
    abstract fun provideIntroFragment(): IntroFragment

}
