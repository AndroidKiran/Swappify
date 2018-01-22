package swapp.items.com.swappify.controller.signup.ui

import android.arch.lifecycle.ViewModelProvider
import android.arch.lifecycle.ViewModelProviders
import android.text.Editable
import swapp.items.com.swappify.BR
import swapp.items.com.swappify.R
import swapp.items.com.swappify.controller.base.BaseFragment
import swapp.items.com.swappify.controller.signup.viewmodel.LogInViewModel
import swapp.items.com.swappify.databinding.IncludeOtpVerifyBinding
import javax.inject.Inject

class OtpVerificationFragment: BaseFragment<IncludeOtpVerifyBinding, LogInViewModel>() {

    @Inject
    lateinit var viewFactory: ViewModelProvider.Factory

    @Inject
    lateinit var loginViewModel: LogInViewModel

    private lateinit var fragmentOtpVerifyBinding: IncludeOtpVerifyBinding


    override fun getViewModel(): LogInViewModel {
       loginViewModel = ViewModelProviders.of(this@OtpVerificationFragment, viewFactory)
               .get(LogInViewModel::class.java)
        return loginViewModel
    }

    override fun getLayoutId() = R.layout.include_otp_verify

    override fun executePendingVariablesBinding() {
        fragmentOtpVerifyBinding = getViewDataBinding()
        fragmentOtpVerifyBinding.setVariable(BR.viewModel, loginViewModel)
        fragmentOtpVerifyBinding.setVariable(BR.clickCallBack, clickCallBack)
    }

    private val clickCallBack = object : ILogInNavigator {
        override fun onClickCountryCode() {
        }

        override fun onClickNext() {
        }

        override fun onClickResendOtp() {
        }

        override fun afterMobileNumChanged(editable: Editable) {
        }

        override fun afterOtpChanged(editable: Editable) {
        }

    }

}
