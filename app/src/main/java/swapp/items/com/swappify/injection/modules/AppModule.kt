package swapp.items.com.swappify.injection.modules

import android.content.Context
import com.google.firebase.analytics.FirebaseAnalytics
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.remoteconfig.FirebaseRemoteConfig
import com.google.gson.Gson
import dagger.Module
import dagger.Provides
import swapp.items.com.swappify.utils.PreferenceUtils
import swapp.items.com.swappify.utils.PreferencesHelper
import swapp.items.com.swappify.controllers.SwapApplication
import swapp.items.com.swappify.data.AppDataManager
import swapp.items.com.swappify.data.DataManagerHelper
import swapp.items.com.swappify.data.auth.AuthDataSourceRemote
import swapp.items.com.swappify.data.auth.AuthDataSourceRemoteHelper
import swapp.items.com.swappify.data.auth.datasource.AuthDataSource
import swapp.items.com.swappify.data.auth.datasource.AuthDataSourceHelper
import swapp.items.com.swappify.firebase.analytics.AnalyticsHelper
import swapp.items.com.swappify.firebase.analytics.AppAnalytics
import swapp.items.com.swappify.firebase.config.AppRemoteConfig
import swapp.items.com.swappify.firebase.config.RemoteConfigHelper
import swapp.items.com.swappify.firebase.crashlytics.AppCrashlytics
import swapp.items.com.swappify.firebase.crashlytics.CrashReportHelper
import swapp.items.com.swappify.firebase.listeners.FirebaseObservableListeners
import swapp.items.com.swappify.firebase.listeners.FirebaseObsevableListenerHelper
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
    internal fun provideFirebaseListeners(firebaseObservableListeners: FirebaseObservableListeners): FirebaseObsevableListenerHelper = firebaseObservableListeners

    @Provides
    @Singleton
    internal fun provideAuthDataSource(authDataSource: AuthDataSource): AuthDataSourceHelper = authDataSource

    @Provides
    @Singleton
    internal fun provideAuthRepository(authDataSourceRemote: AuthDataSourceRemote): AuthDataSourceRemoteHelper = authDataSourceRemote

    @Provides
    @Singleton
    internal fun provideDataManager(appDataManager: AppDataManager): DataManagerHelper = appDataManager

    @Provides
    @Singleton
    internal fun provideFirebaseAnalytics(context: Context): FirebaseAnalytics = FirebaseAnalytics.getInstance(context)

    @Provides
    @Singleton
    internal fun provideFirebaseRemoteConfig(): FirebaseRemoteConfig = FirebaseRemoteConfig.getInstance()

    @Provides
    @Singleton
    internal fun provideAppAnalytics(appAnalytics: AppAnalytics): AnalyticsHelper = appAnalytics

    @Provides
    @Singleton
    internal fun provideAppRemoteConfigHelper(appRemoteConfig: AppRemoteConfig): RemoteConfigHelper = appRemoteConfig

    @Provides
    @Singleton
    internal fun provideFirebaseCrashReportHelper(appCrashlytics: AppCrashlytics): CrashReportHelper = appCrashlytics

    @Provides
    @Singleton
    internal fun provideSharedPreference(preferenceUtils: PreferenceUtils): PreferencesHelper = preferenceUtils

    @Provides
    @Singleton
    internal fun provideGson(): Gson = Gson()

}