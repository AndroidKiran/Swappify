package swapp.items.com.swappify.repo

import com.google.gson.Gson
import swapp.items.com.swappify.common.PreferenceHelper
import swapp.items.com.swappify.firebase.analytics.AppAnalytics
import swapp.items.com.swappify.firebase.crashlytics.AppCrashlytics
import swapp.items.com.swappify.injection.scopes.PerActivity
import swapp.items.com.swappify.rx.SchedulerProvider
import javax.inject.Inject

@PerActivity
data class AppUtilManager @Inject constructor(val preferencesHelper: PreferenceHelper,
                                              val crashReport: AppCrashlytics,
                                              val analytics: AppAnalytics,
                                              val gson: Gson,
                                              val schedulerProvider: SchedulerProvider)