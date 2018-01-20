package swapp.items.com.swappify.controller.launcher.module

import android.arch.lifecycle.ViewModel
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap
import swapp.items.com.swappify.controller.launcher.viewmodel.LauncherViewModel
import swapp.items.com.swappify.injection.qualifiers.ViewModelKey

@Module
abstract class LauncherActivityProviderModule {

    @Binds
    @IntoMap
    @ViewModelKey(LauncherViewModel::class)
    abstract fun bindViewModel(viewModel: LauncherViewModel): ViewModel


}
