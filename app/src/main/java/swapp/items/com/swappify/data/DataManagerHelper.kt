package swapp.items.com.swappify.data

import swapp.items.com.swappify.Utils.PreferencesHelper
import swapp.items.com.swappify.data.auth.AuthDataSourceRemoteHelper
import swapp.items.com.swappify.firebase.analytics.AnalyticsHelper
import swapp.items.com.swappify.firebase.crashlytics.CrashReportHelper

/**
 * Created by ravi on 02/10/17.
 */
interface DataManagerHelper {

//    fun getValue(): String
//
//    fun setValue(value: String)

    fun getAuthRespository(): AuthDataSourceRemoteHelper

    fun getAnalytics(): AnalyticsHelper

    fun getCrashLytics(): CrashReportHelper

    fun getPreferences(): PreferencesHelper
}