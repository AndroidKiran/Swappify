package swapp.items.com.swappify.injection.modules

import com.google.firebase.analytics.FirebaseAnalytics
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.FirebaseFirestoreSettings
import com.google.firebase.remoteconfig.FirebaseRemoteConfig
import com.google.firebase.storage.FirebaseStorage
import dagger.Module
import dagger.Provides
import swapp.items.com.swappify.controllers.SwapApplication
import swapp.items.com.swappify.firebase.analytics.AppAnalytics
import swapp.items.com.swappify.firebase.config.AppRemoteConfig
import swapp.items.com.swappify.firebase.crashlytics.AppCrashlytics
import swapp.items.com.swappify.injection.scopes.PerApplication

@Module
class FirebaseModule constructor(val application: SwapApplication) {

    @Provides
    @PerApplication
    internal fun provideFirebaseDatabase(): FirebaseFirestore {
        val firebaseFirestore: FirebaseFirestore = FirebaseFirestore.getInstance()
        val settings: FirebaseFirestoreSettings = FirebaseFirestoreSettings.Builder()
                .setPersistenceEnabled(true)
                .build()
        firebaseFirestore.firestoreSettings = settings;
        return firebaseFirestore
    }

    @Provides
    @PerApplication
    internal fun provideFirebaseAuth(): FirebaseAuth = FirebaseAuth.getInstance()

    @Provides
    @PerApplication
    internal fun provideFirebaseStorage(): FirebaseStorage = FirebaseStorage.getInstance()

    @Provides
    @PerApplication
    internal fun provideFirebaseAnalytics(): FirebaseAnalytics = FirebaseAnalytics.getInstance(application)

    @Provides
    @PerApplication
    internal fun provideFirebaseRemoteConfig(): FirebaseRemoteConfig = FirebaseRemoteConfig.getInstance()

    @Provides
    @PerApplication
    internal fun provideAppAnalytics(firebaseAnalytics : FirebaseAnalytics): AppAnalytics = AppAnalytics(firebaseAnalytics)

    @Provides
    @PerApplication
    internal fun provideAppRemoteConfigHelper(appRemoteConfig: AppRemoteConfig): AppRemoteConfig = appRemoteConfig

    @Provides
    @PerApplication
    internal fun provideFirebaseCrashReportHelper(): AppCrashlytics = AppCrashlytics()

}