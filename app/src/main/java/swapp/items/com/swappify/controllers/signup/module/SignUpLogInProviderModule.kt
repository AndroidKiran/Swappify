package swapp.items.com.swappify.controllers.signup.module

import android.arch.lifecycle.ViewModel
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap
import swapp.items.com.swappify.controllers.signup.viewModel.SignUpLogInViewModel
import swapp.items.com.swappify.injection.scopes.ViewModelKey

/**
 * Created by ravi on 21/10/17.
 */
@Module
abstract class SignUpLogInProviderModule {

    @Binds
    @IntoMap
    @ViewModelKey(SignUpLogInViewModel::class)
    abstract fun bindSignUpLogInViewModel(viewModel: SignUpLogInViewModel): ViewModel
}