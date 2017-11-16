package swapp.items.com.swappify.controllers.signup.ui

import android.Manifest
import android.arch.lifecycle.ViewModelProvider
import android.arch.lifecycle.ViewModelProviders
import android.content.IntentFilter
import android.os.Bundle
import android.support.v4.app.Fragment
import android.text.TextUtils
import com.google.i18n.phonenumbers.PhoneNumberUtil
import dagger.android.AndroidInjector
import dagger.android.DispatchingAndroidInjector
import dagger.android.support.HasSupportFragmentInjector
import pub.devrel.easypermissions.AfterPermissionGranted
import pub.devrel.easypermissions.EasyPermissions
import swapp.items.com.swappify.BR
import swapp.items.com.swappify.BuildConfig
import swapp.items.com.swappify.R
import swapp.items.com.swappify.controllers.base.BaseActivity
import swapp.items.com.swappify.controllers.base.IFragmentCallback
import swapp.items.com.swappify.controllers.country.model.Countries
import swapp.items.com.swappify.controllers.country.model.Countries.Companion.COUNTRY_EXTRA
import swapp.items.com.swappify.controllers.country.ui.CountryPickerFragment
import swapp.items.com.swappify.controllers.signup.SMSReceiver
import swapp.items.com.swappify.controllers.signup.viewModel.SignUpLogInViewModel
import swapp.items.com.swappify.databinding.ActivitySignupBinding
import swapp.items.com.swappify.utils.AppUtils.Companion.getLocale
import javax.inject.Inject




class SignUpLoginActivity : BaseActivity<ActivitySignupBinding, SignUpLogInViewModel>(),
        ISignUpLogInNavigator, IFragmentCallback, HasSupportFragmentInjector, EasyPermissions.PermissionCallbacks, SMSReceiver.SMSReceivedListener {

    companion object {
        const val ACTION_SIGNUP: String = BuildConfig.APPLICATION_ID + ".action" + ".SIGNUP"
        const val RC_SMS_PERM: Int = 123
    }
    val DEFAULT_COUNTRY_CODE = "+91"
    val PLUS_SIGN = "+"
    val MAX_TIME_IN_SEC = 60

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
        signUpLogInViewModel.setNavigator(this@SignUpLoginActivity)
        initViews()
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
    }

    override fun openCountryCodeDialog() {
        CountryPickerFragment.newInstance(Bundle()).show(supportFragmentManager, CountryPickerFragment.TAG)
    }

    override fun supportFragmentInjector(): AndroidInjector<Fragment> = fragmentDispatchingAndroidInjector

    override fun onFragmentInteraction(bundle: Bundle?) {
        val country: Countries.Country? = bundle?.getParcelable(COUNTRY_EXTRA)
        signUpLogInViewModel.selectedCountry.set(country)
        signUpLogInViewModel.countryCode.set(country?.isoCode)
        if (!TextUtils.isEmpty(signUpLogInViewModel.mobileNumber.get())) {
            signUpLogInViewModel.validatePhoneNum()
        }
    }

    override fun onNextClick() {
        signUpLogInViewModel.startPhoneNumberVerification(this@SignUpLoginActivity)
    }

    override fun onResendClick() {
        signUpLogInViewModel.resendVerificationCode(this@SignUpLoginActivity)
    }

    override fun startHomeActivity() {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun onPermissionsDenied(requestCode: Int, perms: MutableList<String>?) {
        //Permission Denied
    }

    override fun onPermissionsGranted(requestCode: Int, perms: MutableList<String>?) {
        //Permission Granted
    }


    @AfterPermissionGranted(RC_SMS_PERM)
    override fun seekSmsReadPermission() {
        if (EasyPermissions.hasPermissions(this@SignUpLoginActivity, Manifest.permission.RECEIVE_SMS)) {
            if (smsReceiver == null) {
                smsReceiver = SMSReceiver(this@SignUpLoginActivity)
                registerSmsReceiver()
                signUpLogInViewModel.startOtpCountDown(MAX_TIME_IN_SEC)
            }
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
        signUpLogInViewModel.onVerifyOtpClick()
    }
}