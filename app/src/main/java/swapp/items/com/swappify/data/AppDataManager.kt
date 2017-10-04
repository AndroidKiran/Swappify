package swapp.items.com.swappify.data

import swapp.items.com.swappify.Utils.PreferencesHelper
import swapp.items.com.swappify.data.auth.AuthDataSourceRemoteHelper
import swapp.items.com.swappify.firebase.analytics.AnalyticsHelper
import swapp.items.com.swappify.firebase.crashlytics.CrashReportHelper
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AppDataManager @Inject constructor(private val preferencesHelper: PreferencesHelper,
                                         private val crashReportHelper: CrashReportHelper,
                                         private val analyticsHelper: AnalyticsHelper,
                                         private val authDataSourceRemoteHelper: AuthDataSourceRemoteHelper): DataManagerHelper {


//    override fun setValue(value: String) {
//        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
//    }
//
//    override fun getValue(): String {
//        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
//    }

    override fun getCrashLytics(): CrashReportHelper = crashReportHelper

    override fun getAnalytics(): AnalyticsHelper = analyticsHelper

    override fun getAuthRespository(): AuthDataSourceRemoteHelper = authDataSourceRemoteHelper

    override fun getPreferences(): PreferencesHelper = preferencesHelper

}