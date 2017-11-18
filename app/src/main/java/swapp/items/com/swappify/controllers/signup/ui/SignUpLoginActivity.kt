package swapp.items.com.swappify.controllers.signup.ui

import android.Manifest
import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProvider
import android.arch.lifecycle.ViewModelProviders
import android.content.IntentFilter
import android.os.Bundle
import android.support.v4.app.Fragment
import android.text.TextUtils
import com.google.firebase.auth.PhoneAuthProvider
import com.google.i18n.phonenumbers.PhoneNumberUtil
import dagger.android.AndroidInjector
import dagger.android.DispatchingAndroidInjector
import dagger.android.support.HasSupportFragmentInjector
import pub.devrel.easypermissions.AfterPermissionGranted
import pub.devrel.easypermissions.EasyPermissions
import swapp.items.com.swappify.BR
import swapp.items.com.swappify.BuildConfig
import swapp.items.com.swappify.R
import swapp.items.com.swappify.components.TextChangeListener
import swapp.items.com.swappify.controllers.base.BaseActivity
import swapp.items.com.swappify.controllers.base.IFragmentCallback
import swapp.items.com.swappify.controllers.configs.ContentLoadingConfiguration
import swapp.items.com.swappify.controllers.country.model.Countries
import swapp.items.com.swappify.controllers.country.model.Countries.Companion.COUNTRY_EXTRA
import swapp.items.com.swappify.controllers.country.ui.CountryPickerFragment
import swapp.items.com.swappify.controllers.signup.SMSReceiver
import swapp.items.com.swappify.controllers.signup.viewModel.SignUpLogInViewModel
import swapp.items.com.swappify.databinding.ActivitySignupBinding
import swapp.items.com.swappify.utils.AppUtils.Companion.getLocale
import javax.inject.Inject

class SignUpLoginActivity : BaseActivity<ActivitySignupBinding, SignUpLogInViewModel>(),
        IFragmentCallback, HasSupportFragmentInjector, EasyPermissions.PermissionCallbacks,
        SMSReceiver.SMSReceivedListener {

    companion object {
        const val ACTION_SIGNUP: String = BuildConfig.APPLICATION_ID + ".action" + ".SIGNUP"
        const val RC_SMS_PERM: Int = 123
        const val DEFAULT_COUNTRY_CODE = "+91"
        const val PLUS_SIGN = "+"
        const val MAX_TIME_IN_SEC = 60
        const val STATE_SCREEN = "state_screen"
        const val STATE_LOADING = "state_loading"
        const val STATE_COUNT_DOWN_VALUE = "state_count_down_value"
        const val STATE_LOADING_STRING = "state_loading_string"
        const val STATE_CODE = "state_code"
        const val STATE_TOKEN = "state_token"
    }


    @Inject
    lateinit var viewFactory: ViewModelProvider.Factory

    @Inject
    lateinit var fragmentDispatchingAndroidInjector: DispatchingAndroidInjector<Fragment>

    @Inject
    lateinit var signUpLogInViewModel: SignUpLogInViewModel

    private lateinit var activitySignupBinding: ActivitySignupBinding

    private var smsReceiver: SMSReceiver? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initViews()
        observerSeekPermissionLiveData()
    }

    override fun onStart() {
        super.onStart()
        registerSmsReceiver()
    }

    override fun onDestroy() {
        unRegisterSmsReceiver()
        super.onDestroy()
    }

    private fun initViews() {
        if (TextUtils.isEmpty(signUpLogInViewModel.countryCode.get())) {
            signUpLogInViewModel.countryCode.set(prefetchCountryCode())
        }
    }

    private fun prefetchCountryCode(): String {
        val countryISOCode: String = getLocale(this).country
        val code: Int = PhoneNumberUtil.getInstance().getCountryCodeForRegion(countryISOCode)
        var countryCode: String = PLUS_SIGN.plus(code)
        if (code == 0) {
            countryCode = DEFAULT_COUNTRY_CODE
        }
        return countryCode
    }

    override fun getLayoutId(): Int = R.layout.activity_signup

    override fun getViewModel(): SignUpLogInViewModel {
        signUpLogInViewModel = ViewModelProviders.of(this@SignUpLoginActivity,
                viewFactory).get(SignUpLogInViewModel::class.java)
        return signUpLogInViewModel
    }


    override fun executePendingVariablesBinding() {
        activitySignupBinding = getViewDataBinding()
        activitySignupBinding.setVariable(BR.viewModel, signUpLogInViewModel)
        activitySignupBinding.setVariable(BR.clickCallBack, onClickCallBack)
        activitySignupBinding.setVariable(BR.textChangeCallBack, textWatcher)
    }

    override fun supportFragmentInjector(): AndroidInjector<Fragment> = fragmentDispatchingAndroidInjector

    override fun onFragmentInteraction(bundle: Bundle?) {
        val country: Countries.Country? = bundle?.getParcelable(COUNTRY_EXTRA)
        signUpLogInViewModel.countryCode.set(country?.isoCode)
        if (!TextUtils.isEmpty(signUpLogInViewModel.mobileNumber.get())) {
            signUpLogInViewModel.validatePhoneNum()
        }
    }

    override fun onPermissionsDenied(requestCode: Int, perms: MutableList<String>?) {
        //Permission Denied
    }

    override fun onPermissionsGranted(requestCode: Int, perms: MutableList<String>?) {
        //Permission Granted
    }


    @AfterPermissionGranted(RC_SMS_PERM)
    private fun seekSmsReadPermission() {
        if (EasyPermissions.hasPermissions(this@SignUpLoginActivity, Manifest.permission.RECEIVE_SMS)) {
            attachReceiver(MAX_TIME_IN_SEC)
        } else {
            EasyPermissions.requestPermissions(this, getString(R.string.rationale_sms),
                    RC_SMS_PERM, Manifest.permission.RECEIVE_SMS)
        }
    }

    private fun registerSmsReceiver() {
        if (smsReceiver != null) {
            registerReceiver(smsReceiver, IntentFilter(getString(R.string.perm_telephony)))
        }
    }

    private fun unRegisterSmsReceiver() {
        if (smsReceiver != null) {
            unregisterReceiver(smsReceiver)
        }
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        EasyPermissions.onRequestPermissionsResult(requestCode, permissions, grantResults, this@SignUpLoginActivity);
    }

    override fun onSMSReceived(bundle: Bundle?) {
        val otpCode: String? = bundle?.getString(SMSReceiver.VERIFICATION_CODE, "")
        signUpLogInViewModel.validateNewOtp(otpCode)
        signUpLogInViewModel.updateContentLoading(false)
        signUpLogInViewModel.onClickVerifyOtp()
    }

    private var onClickCallBack = object : ISignUpLogInNavigator {

        override fun onClickCountryCode() {
            CountryPickerFragment.newInstance(Bundle()).show(supportFragmentManager,
                    CountryPickerFragment.TAG)
        }

        override fun onClickNext() {
            signUpLogInViewModel.phoneError.set(null)
            signUpLogInViewModel.updateContentLoading(true)
            signUpLogInViewModel.startPhoneNumberVerification(this@SignUpLoginActivity)
        }

        override fun onClickResendOtp() {
            signUpLogInViewModel.updateContentLoading(true, getString(R.string.msg_otp_resend))
            signUpLogInViewModel.resendVerificationCode(this@SignUpLoginActivity)
        }

        override fun onClickVerifyOtp() {
            signUpLogInViewModel.onClickVerifyOtp()
        }
    }

    private var textWatcher: TextChangeListener = object : TextChangeListener() {

        override fun afterTextChanged(newValue: String?) {
            if (signUpLogInViewModel.verifyCodeScreenEnabled.get()) {
                if (TextUtils.isEmpty(newValue)) {
                    signUpLogInViewModel.enableOtpVerificationBtn.set(false)
                } else {
                    signUpLogInViewModel.validateNewOtp(newValue)
                }
            } else {
                signUpLogInViewModel.mobileNumber.set(newValue)
                signUpLogInViewModel.validatePhoneNum()
            }
        }
    }

    private fun observerSeekPermissionLiveData() {
        signUpLogInViewModel.getSeekPermission().observe(this@SignUpLoginActivity,
                Observer { granted: Boolean? ->
                    if (granted!!) {
                        seekSmsReadPermission()
                    }
                })
    }

    override fun onRestoreInstanceState(savedInstanceState: Bundle?) {
        super.onRestoreInstanceState(savedInstanceState)

        val screenState = savedInstanceState?.getBoolean(STATE_SCREEN, false)
        signUpLogInViewModel.verifyCodeScreenEnabled.set(screenState)

        val loadingText = savedInstanceState?.getString(STATE_LOADING_STRING, "")
        var loadingState = savedInstanceState?.getBoolean(STATE_LOADING, false)

        var contentLoadingConfiguration = ContentLoadingConfiguration(loadingState, loadingText)
        signUpLogInViewModel.contentLoadingConfigView.set(contentLoadingConfiguration)

        val countDownValue = savedInstanceState?.getInt(STATE_COUNT_DOWN_VALUE, MAX_TIME_IN_SEC)
        signUpLogInViewModel.countDownValue = countDownValue

        val verificationCode = savedInstanceState?.getString(STATE_CODE, null)
        signUpLogInViewModel.verificationCode = verificationCode

        val token: PhoneAuthProvider.ForceResendingToken? = savedInstanceState?.getParcelable(STATE_TOKEN)
        signUpLogInViewModel.token = token

        if (loadingState!!) {
            if (loadingText?.contains("Secs")!! &&
                    verificationCode != null && screenState!!) {
                contentLoadingConfiguration = ContentLoadingConfiguration(loadingState, loadingText)
                signUpLogInViewModel.contentLoadingConfigView.set(contentLoadingConfiguration)
                attachReceiver(countDownValue!!)
            } else {
                loadingState = false
                contentLoadingConfiguration = ContentLoadingConfiguration(loadingState, loadingText)
                signUpLogInViewModel.contentLoadingConfigView.set(contentLoadingConfiguration)
            }

        }
    }

    override fun onSaveInstanceState(outState: Bundle?) {
        super.onSaveInstanceState(outState)
        outState?.putBoolean(STATE_SCREEN, signUpLogInViewModel.verifyCodeScreenEnabled.get())

        val contentLoadingConfiguration: ContentLoadingConfiguration? =
                signUpLogInViewModel.contentLoadingConfigView.get()
        outState?.putBoolean(STATE_LOADING,
                contentLoadingConfiguration?.isContentLoading?.get()!!)
        outState?.putString(STATE_LOADING_STRING,
                contentLoadingConfiguration?.contentLoadingText?.get())

        outState?.putInt(STATE_COUNT_DOWN_VALUE, signUpLogInViewModel.countDownValue!!)
        outState?.putString(STATE_CODE, signUpLogInViewModel.verificationCode)
        outState?.putParcelable(STATE_TOKEN, signUpLogInViewModel.token)

    }

    private fun attachReceiver(time: Int) {
        if (smsReceiver == null) {
            smsReceiver = SMSReceiver(this@SignUpLoginActivity)
            registerSmsReceiver()
            signUpLogInViewModel.startOtpCountDown(time)
        }
    }

}
