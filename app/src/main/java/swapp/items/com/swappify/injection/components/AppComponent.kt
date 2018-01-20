package swapp.items.com.swappify.injection.components

import android.support.multidex.MultiDexApplication
import dagger.BindsInstance
import dagger.Component
import dagger.android.support.AndroidSupportInjectionModule
import swapp.items.com.swappify.controller.SwapApplication
import swapp.items.com.swappify.injection.builder.ActivityBuilder
import swapp.items.com.swappify.injection.modules.AppModule
import swapp.items.com.swappify.injection.modules.FirebaseModule
import swapp.items.com.swappify.injection.scopes.PerApplication

@PerApplication
@Component(modules = [(AndroidSupportInjectionModule::class), (AppModule::class), (ActivityBuilder::class)])
interface AppComponent {

    @Component.Builder
    interface Builder {
        @BindsInstance
        fun application(application: MultiDexApplication): Builder

        fun appModule(appModule: AppModule): Builder

        fun fireBaseModule(firebaseModule: FirebaseModule): Builder

        fun build(): AppComponent

    }

    fun inject(application: SwapApplication)

}