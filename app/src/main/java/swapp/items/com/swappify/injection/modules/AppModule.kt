package swapp.items.com.swappify.injection.modules

import android.content.Context
import com.google.firebase.analytics.FirebaseAnalytics
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.remoteconfig.FirebaseRemoteConfig
import com.google.gson.Gson
import dagger.Module
import dagger.Provides
import swapp.items.com.swappify.controllers.SwapApplication
import swapp.items.com.swappify.firebase.analytics.AppAnalytics
import swapp.items.com.swappify.firebase.config.AppRemoteConfig
import swapp.items.com.swappify.firebase.crashlytics.AppCrashlytics
import swapp.items.com.swappify.utils.PreferenceUtils
import javax.inject.Inject
import javax.inject.Singleton

@Module
class AppModule @Inject constructor(val application: SwapApplication) {


    @Provides
    @Singleton
    fun provideApplication(): Context = application


    @Provides
    @Singleton
    internal fun provideFirebaseDatabase(): FirebaseDatabase {
        val firebaseDatabase = FirebaseDatabase.getInstance()
        firebaseDatabase.setPersistenceEnabled(true)
        return firebaseDatabase
    }

    @Provides
    @Singleton
    internal fun provideFirebaseAuth(): FirebaseAuth = FirebaseAuth.getInstance()

    @Provides
    @Singleton
    internal fun provideFirebaseAnalytics(context: Context): FirebaseAnalytics = FirebaseAnalytics.getInstance(context)

    @Provides
    @Singleton
    internal fun provideFirebaseRemoteConfig(): FirebaseRemoteConfig = FirebaseRemoteConfig.getInstance()

    @Provides
    @Singleton
    internal fun provideAppAnalytics(firebaseAnalytics : FirebaseAnalytics): AppAnalytics = AppAnalytics(firebaseAnalytics)

    @Provides
    @Singleton
    internal fun provideAppRemoteConfigHelper(appRemoteConfig: AppRemoteConfig): AppRemoteConfig = appRemoteConfig

    @Provides
    @Singleton
    internal fun provideFirebaseCrashReportHelper(): AppCrashlytics = AppCrashlytics()

    @Provides
    @Singleton
    internal fun provideSharedPreference(context: Context): PreferenceUtils = PreferenceUtils(context = context)

    @Provides
    @Singleton
    internal fun provideGson(): Gson = Gson()
}