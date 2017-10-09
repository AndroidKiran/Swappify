package swapp.items.com.swappify.controllers.signup

import android.arch.lifecycle.ViewModelProvider
import android.arch.lifecycle.ViewModelProviders
import android.os.Bundle
import android.support.v4.app.Fragment
import dagger.android.AndroidInjector
import dagger.android.DispatchingAndroidInjector
import dagger.android.support.HasSupportFragmentInjector
import swapp.items.com.swappify.BR
import swapp.items.com.swappify.BuildConfig
import swapp.items.com.swappify.R
import swapp.items.com.swappify.controllers.base.BaseActivity
import swapp.items.com.swappify.controllers.country.CountryPickerFragment
import swapp.items.com.swappify.controllers.signup.model.PhoneAuthDataModel
import swapp.items.com.swappify.controllers.signup.viewmodel.SignUpLogInViewModel
import swapp.items.com.swappify.databinding.ActivitySignupBinding

import javax.inject.Inject


class SignUpLoginActivity : BaseActivity<ActivitySignupBinding, SignUpLogInViewModel>() , SignUpLogInNavigator, HasSupportFragmentInjector {


    companion object {
        val ACTION_SIGNUP: String = BuildConfig.APPLICATION_ID + ".action" + ".SIGNUP"
    }

    @Inject
    lateinit var viewFactory: ViewModelProvider.Factory

    @Inject
    lateinit var fragmentDispatchingAndroidInjector: DispatchingAndroidInjector<Fragment>

    private lateinit var signUpLogInViewModel: SignUpLogInViewModel

    private lateinit var activitySignupBinding: ActivitySignupBinding


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        activitySignupBinding = viewDataBinding
        signUpLogInViewModel.baseNavigator = this
    }

    override fun getBindingVariable(): Int = BR.viewModel

    override fun getLayoutId(): Int = R.layout.activity_signup

    override fun getViewModel(): SignUpLogInViewModel {
        signUpLogInViewModel = ViewModelProviders.of(this, viewFactory).get(SignUpLogInViewModel::class.java)
        return signUpLogInViewModel
    }

    override fun openCountryCodeDialog() {
        CountryPickerFragment.newInstance(Bundle()).show(supportFragmentManager, CountryPickerFragment.TAG)
    }

    override fun verifyPhoneNumber() {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun handleOnSucces(phoneAuthDataModel: PhoneAuthDataModel) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun handleOnError(error: Throwable) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun supportFragmentInjector(): AndroidInjector<Fragment> = fragmentDispatchingAndroidInjector

}