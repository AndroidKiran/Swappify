package swapp.items.com.swappify.injection.modules

import android.content.Context
import dagger.Module
import dagger.Provides
import swapp.items.com.swappify.controllers.SwapApplication
import swapp.items.com.swappify.injection.scopes.PerApplication
import swapp.items.com.swappify.utils.PreferenceUtils

@Module(includes = arrayOf(FirebaseModule::class, NetworkModule::class))
class AppModule constructor(val application: SwapApplication) {

    @Provides
    @PerApplication
    fun provideApplication(): SwapApplication = application

    @Provides
    @PerApplication
    fun provideContext(): Context = application.applicationContext

    @Provides
    @PerApplication
    fun provideSharedPreference(context: Context): PreferenceUtils = PreferenceUtils(context = context)
}