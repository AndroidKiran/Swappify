package swapp.items.com.swappify.injection.components

import android.support.multidex.MultiDexApplication
import dagger.BindsInstance
import dagger.Component
import dagger.android.support.AndroidSupportInjectionModule
import swapp.items.com.swappify.controllers.SwapApplication
import swapp.items.com.swappify.injection.builder.ActivityBuilder
import swapp.items.com.swappify.injection.modules.AppModule
import javax.inject.Singleton

@Singleton
@Component(modules = arrayOf(AndroidSupportInjectionModule::class, AppModule::class, ActivityBuilder::class))
interface AppComponent {

    @Component.Builder
    interface Builder {
        @BindsInstance
        fun application(application: MultiDexApplication): Builder

        fun injectAppModule(appModule: AppModule): Builder

        fun build(): AppComponent

    }

    fun inject(application: SwapApplication)

}