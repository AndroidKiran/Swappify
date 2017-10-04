package swapp.items.com.swappify.controllers.signup.module

import android.arch.lifecycle.ViewModelProvider
import dagger.Module
import dagger.Provides
import swapp.items.com.swappify.base.ViewModelProviderFactory
import swapp.items.com.swappify.controllers.signup.model.SignUpLogInViewModel
import swapp.items.com.swappify.data.DataManagerHelper

@Module
class SignUpLogInModule {

    @Provides
    internal fun provideViewModel(dataManagerHelper: DataManagerHelper): SignUpLogInViewModel = SignUpLogInViewModel(dataManagerHelper)

    @Provides
    internal fun signUpLogInViewModelViewModelProvider(signUpLogInViewModel: SignUpLogInViewModel): ViewModelProvider.Factory =
            ViewModelProviderFactory(signUpLogInViewModel)

}