package swapp.items.com.swappify.controllers.signup

import android.arch.lifecycle.ViewModelProvider
import android.arch.lifecycle.ViewModelProviders
import android.os.Bundle
import swapp.items.com.swappify.BR
import swapp.items.com.swappify.BuildConfig
import swapp.items.com.swappify.R
import swapp.items.com.swappify.base.BaseActivity
import swapp.items.com.swappify.controllers.signup.model.PhoneAuthDataModel
import swapp.items.com.swappify.controllers.signup.model.SignUpLogInViewModel
import swapp.items.com.swappify.databinding.ActivitySignupBinding

import javax.inject.Inject


class SignUpLoginActivity : BaseActivity<ActivitySignupBinding, SignUpLogInViewModel>() , SignUpLogInNavigator {

    companion object {
        val ACTION_SIGNUP: String = BuildConfig.APPLICATION_ID + ".action" + ".SIGNUP"
    }

    @Inject
    lateinit var mViewFactory: ViewModelProvider.Factory

    private lateinit var mSignUpLogInViewModel: SignUpLogInViewModel
    private lateinit var mActivitySignUpBinding: ActivitySignupBinding


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mActivitySignUpBinding = getViewDataBinding()
        mSignUpLogInViewModel.setNavigator(this)
    }

    override fun getBindingVariable(): Int = BR.viewModel

    override fun getLayoutId(): Int = R.layout.activity_signup

    override fun getViewModel(): SignUpLogInViewModel {
        mSignUpLogInViewModel = ViewModelProviders.of(this, mViewFactory).get(SignUpLogInViewModel::class.java)
        return mSignUpLogInViewModel
    }

    override fun openCountryCodeDialog() {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
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

}