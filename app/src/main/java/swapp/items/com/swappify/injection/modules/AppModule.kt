package swapp.items.com.swappify.injection.modules

import android.content.Context
import com.google.gson.Gson
import dagger.Module
import dagger.Provides
import swapp.items.com.swappify.controllers.SwapApplication
import swapp.items.com.swappify.injection.scopes.PerApplication
import swapp.items.com.swappify.utils.PreferenceUtils
import javax.inject.Singleton

@Module(includes = arrayOf(FirebaseModule::class, ViewModelModule::class))
class AppModule constructor(val application: SwapApplication) {

    @Provides
    @PerApplication
    fun provideApplication(): SwapApplication = application

    @Provides
    @PerApplication
    fun provideContext(): Context = application.applicationContext

    @PerApplication
    @Singleton
    internal fun provideSharedPreference(context: Context): PreferenceUtils = PreferenceUtils(context = context)

    @Provides
    @PerApplication
    internal fun provideGson(): Gson = Gson()
}