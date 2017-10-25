package swapp.items.com.swappify.controllers

import android.app.Activity
import android.support.multidex.MultiDexApplication
import dagger.android.AndroidInjector
import dagger.android.DispatchingAndroidInjector
import dagger.android.HasActivityInjector
import swapp.items.com.swappify.injection.components.DaggerAppComponent
import swapp.items.com.swappify.injection.modules.AppModule
import javax.inject.Inject


class SwapApplication : MultiDexApplication(), HasActivityInjector {

    @Inject
    lateinit var activityInjector: DispatchingAndroidInjector<Activity>

    override fun activityInjector(): AndroidInjector<Activity> = activityInjector

    override fun onCreate() {
        super.onCreate()
        DaggerAppComponent.builder().application(this).injectAppModule(AppModule(this)).build().inject(this)
    }
}