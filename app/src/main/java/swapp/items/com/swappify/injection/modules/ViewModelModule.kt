package swapp.items.com.swappify.injection.modules

import android.arch.lifecycle.ViewModel
import android.arch.lifecycle.ViewModelProvider
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap
import swapp.items.com.swappify.controllers.ViewModelFactory
import swapp.items.com.swappify.controllers.signup.viewmodel.SignUpLogInViewModel
import swapp.items.com.swappify.injection.qualifiers.ViewModelKey

@Module
abstract class ViewModelModule {

    @Binds
    abstract fun bindViewModelFactory(factory: ViewModelFactory): ViewModelProvider.Factory

    @Binds
    @IntoMap
    @ViewModelKey(SignUpLogInViewModel::class)
    abstract fun bindSignUpLogInViewModel(viewModel: SignUpLogInViewModel): ViewModel

}
