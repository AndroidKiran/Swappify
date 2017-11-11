package swapp.items.com.swappify.firebase.analytics

import com.google.firebase.analytics.FirebaseAnalytics
import swapp.items.com.swappify.injection.scopes.PerActivity
import javax.inject.Inject

/**
 * Created by ravi on 03/10/17.
 */
@PerActivity
class AppAnalytics @Inject constructor(val firebaseAnalytics: FirebaseAnalytics) {


}