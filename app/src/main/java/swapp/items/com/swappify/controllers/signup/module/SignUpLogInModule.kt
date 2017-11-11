package swapp.items.com.swappify.controllers.signup.module

import dagger.Module
import dagger.Provides
import swapp.items.com.swappify.controllers.SwapApplication

@Module
class SignUpLogInModule {

    @Provides
    fun provideApplication(application: SwapApplication): SwapApplication = application

}