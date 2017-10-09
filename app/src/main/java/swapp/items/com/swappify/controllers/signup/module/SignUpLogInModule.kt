package swapp.items.com.swappify.controllers.signup.module

import android.arch.lifecycle.ViewModelProvider
import dagger.Module
import dagger.Provides
import swapp.items.com.swappify.controllers.base.ViewModelProviderFactory
import swapp.items.com.swappify.controllers.signup.viewmodel.SignUpLogInViewModel
import swapp.items.com.swappify.data.DataManagerHelper

@Module
class SignUpLogInModule {

    @Provides
    internal fun provideViewModel(dataManagerHelper: DataManagerHelper): SignUpLogInViewModel =
            SignUpLogInViewModel(dataManagerHelper = dataManagerHelper)

    @Provides
    internal fun signUpLogInViewModelProviderFactory(signUpLogInViewModel: SignUpLogInViewModel): ViewModelProvider.Factory =
            ViewModelProviderFactory<SignUpLogInViewModel>(signUpLogInViewModel)

}