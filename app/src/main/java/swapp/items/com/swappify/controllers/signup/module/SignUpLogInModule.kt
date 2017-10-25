package swapp.items.com.swappify.controllers.signup.module

import com.google.firebase.auth.FirebaseAuth
import dagger.Module
import dagger.Provides
import swapp.items.com.swappify.controllers.signup.viewModel.SignUpLogInViewModel
import swapp.items.com.swappify.data.AppDataManager
import swapp.items.com.swappify.data.auth.AuthDataSourceRemote
import swapp.items.com.swappify.data.auth.datasource.AuthDataSource
import swapp.items.com.swappify.firebase.listeners.FirebaseObservableListeners

@Module
class SignUpLogInModule {

    @Provides
    internal fun provideAuthDataSource(fireBaseAuth: FirebaseAuth, firebaseObservableListeners: FirebaseObservableListeners): AuthDataSource =
            AuthDataSource(firebaseAuth = fireBaseAuth, firebaseObservableListeners = firebaseObservableListeners)

    @Provides
    internal fun provideAuthRepository(authDataSource: AuthDataSource): AuthDataSourceRemote =
            AuthDataSourceRemote(authDataSource = authDataSource)

    @Provides
    internal fun provideViewModel(dataManager: AppDataManager): SignUpLogInViewModel =
            SignUpLogInViewModel(dataManager = dataManager)

}