package swapp.items.com.swappify.controllers.signup.module

import dagger.Module
import dagger.Provides
import swapp.items.com.swappify.controllers.base.NetworkConnectionBroadcastReceiver
import swapp.items.com.swappify.injection.scopes.PerActivity

@Module
class LoginActivityModule {

    @PerActivity
    @Provides
    fun provideNetworkBroadcastReceiver() = NetworkConnectionBroadcastReceiver()
}
