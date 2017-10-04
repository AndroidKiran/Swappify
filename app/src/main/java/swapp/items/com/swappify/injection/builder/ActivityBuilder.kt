package swapp.items.com.swappify.injection.builder

import dagger.Module
import dagger.android.ContributesAndroidInjector
import swapp.items.com.swappify.controllers.signup.SignUpLoginActivity
import swapp.items.com.swappify.controllers.signup.module.SignUpLogInModule


@Module
abstract class ActivityBuilder {

    @ContributesAndroidInjector(modules = arrayOf(SignUpLogInModule::class))
    abstract fun provideSignUpLogInActivity(): SignUpLoginActivity

}