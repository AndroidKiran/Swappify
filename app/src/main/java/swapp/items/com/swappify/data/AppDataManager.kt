package swapp.items.com.swappify.data

import com.google.gson.Gson
import swapp.items.com.swappify.utils.PreferencesHelper
import swapp.items.com.swappify.data.auth.AuthDataSourceRemoteHelper
import swapp.items.com.swappify.firebase.analytics.AnalyticsHelper
import swapp.items.com.swappify.firebase.crashlytics.CrashReportHelper
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AppDataManager @Inject constructor(private val preferencesHelper: PreferencesHelper,
                                         private val crashReportHelper: CrashReportHelper,
                                         private val analyticsHelper: AnalyticsHelper,
                                         private val gson: Gson,
                                         private val authDataSourceRemoteHelper: AuthDataSourceRemoteHelper): DataManagerHelper {


    override fun getCrashLytics(): CrashReportHelper = crashReportHelper

    override fun getAnalytics(): AnalyticsHelper = analyticsHelper

    override fun getAuthRespository(): AuthDataSourceRemoteHelper = authDataSourceRemoteHelper

    override fun getPreferences(): PreferencesHelper = preferencesHelper

    override fun getGson(): Gson = gson

}