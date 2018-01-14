package swapp.items.com.swappify.repo

import com.google.gson.Gson
import swapp.items.com.swappify.common.ConnectionUtil
import swapp.items.com.swappify.common.PreferenceUtils
import swapp.items.com.swappify.firebase.analytics.AppAnalytics
import swapp.items.com.swappify.firebase.crashlytics.AppCrashlytics
import swapp.items.com.swappify.injection.scopes.PerActivity
import swapp.items.com.swappify.rx.SchedulerProvider
import javax.inject.Inject

@PerActivity
data class AppUtilManager @Inject constructor(val preferencesUtils: PreferenceUtils,
                                              val crashReport: AppCrashlytics,
                                              val analytics: AppAnalytics,
                                              val gson: Gson,
                                              val schedulerProvider: SchedulerProvider,
                                              val connectionUtil: ConnectionUtil)