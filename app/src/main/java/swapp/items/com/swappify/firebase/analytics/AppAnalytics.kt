package swapp.items.com.swappify.firebase.analytics

import com.google.firebase.analytics.FirebaseAnalytics
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Created by ravi on 03/10/17.
 */
@Singleton
class AppAnalytics @Inject constructor(firebaseAnalytics: FirebaseAnalytics): AnalyticsHelper {

    private val mFirebaseAnalytics: FirebaseAnalytics

    init {
        mFirebaseAnalytics = firebaseAnalytics;
    }
}