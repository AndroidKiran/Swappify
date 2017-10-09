package swapp.items.com.swappify.data

import com.google.gson.Gson
import swapp.items.com.swappify.utils.PreferencesHelper
import swapp.items.com.swappify.data.auth.AuthDataSourceRemoteHelper
import swapp.items.com.swappify.firebase.analytics.AnalyticsHelper
import swapp.items.com.swappify.firebase.crashlytics.CrashReportHelper

interface DataManagerHelper {

    fun getAuthRespository(): AuthDataSourceRemoteHelper

    fun getAnalytics(): AnalyticsHelper

    fun getCrashLytics(): CrashReportHelper

    fun getPreferences(): PreferencesHelper

    fun getGson(): Gson
}