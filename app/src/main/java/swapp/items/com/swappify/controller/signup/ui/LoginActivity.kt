package swapp.items.com.swappify.controller.signup.ui

import android.Manifest
import android.arch.lifecycle.ViewModelProvider
import android.arch.lifecycle.ViewModelProviders
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v4.content.res.ResourcesCompat
import android.text.Editable
import android.view.View
import com.google.firebase.auth.PhoneAuthProvider
import dagger.android.AndroidInjector
import dagger.android.DispatchingAndroidInjector
import dagger.android.HasBroadcastReceiverInjector
import dagger.android.support.HasSupportFragmentInjector
import kotlinx.android.synthetic.main.include_otp_verify.*
import kotlinx.android.synthetic.main.include_phone_verification.*
import pub.devrel.easypermissions.AfterPermissionGranted
import pub.devrel.easypermissions.EasyPermissions
import swapp.items.com.swappify.BR
import swapp.items.com.swappify.R
import swapp.items.com.swappify.common.extension.*
import swapp.items.com.swappify.controller.base.BaseActivity
import swapp.items.com.swappify.controller.base.IFragmentCallback
import swapp.items.com.swappify.controller.configs.SnackbarConfiguration
import swapp.items.com.swappify.controller.country.model.Countries.Companion.COUNTRY_EXTRA
import swapp.items.com.swappify.controller.country.model.Country
import swapp.items.com.swappify.controller.country.ui.CountryPickerFragment
import swapp.items.com.swappify.controller.signup.SMSReceiver
import swapp.items.com.swappify.controller.signup.model.PhoneAuthDataModel
import swapp.items.com.swappify.controller.signup.viewmodel.LogInViewModel
import swapp.items.com.swappify.databinding.ActivityLogInBinding
import swapp.items.com.swappify.mvvm.NetworkConnectionLifeCycleObserver
import javax.inject.Inject


class LoginActivity : BaseActivity<ActivityLogInBinding, LogInViewModel>(), HasSupportFragmentInjector,
        IFragmentCallback, SMSReceiver.SMSReceivedListener, EasyPermissions.PermissionCallbacks, HasBroadcastReceiverInjector {


    @Inject
    lateinit var viewFactory: ViewModelProvider.Factory

    @Inject
    lateinit var fragmentDispatchingAndroidInjector: DispatchingAndroidInjector<Fragment>

    @Inject
    lateinit var broadcastReceiverDispatchingAndroidInjector: DispatchingAndroidInjector<BroadcastReceiver>

    @Inject
    lateinit var logInViewModel: LogInViewModel

    private lateinit var activityLogInBinding: ActivityLogInBinding

    private var smsReceiver: SMSReceiver? = null

    private val snackBarConfiguration = SnackbarConfiguration()

    override fun getViewModel(): LogInViewModel {
        logInViewModel = ViewModelProviders.of(this@LoginActivity, viewFactory)
                .get(LogInViewModel::class.java)
        return logInViewModel
    }

    override fun getLayoutId(): Int = R.layout.activity_log_in


    override fun executePendingVariablesBinding() {
        activityLogInBinding = getViewDataBinding()
        activityLogInBinding.setVariable(BR.viewModel, logInViewModel)
        activityLogInBinding.setVariable(BR.clickCallBack, clickCallBack)
        activityLogInBinding.setVariable(BR.snackBarConfig, snackBarConfiguration)
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        observeAuthModelChange()
        observerNetworkChange()
        observerApiCallErrorChange()
        initPinView()
        NetworkConnectionLifeCycleObserver(lifecycle, logInViewModel.isNetConnected, this@LoginActivity)
    }

    private fun initPinView() {
        activityLogInBinding.otpVerifyScreen.pinView.setTextColor(
                ResourcesCompat.getColor(resources, R.color.white, theme))

        activityLogInBinding.otpVerifyScreen.pinView.setLineColor(
                ResourcesCompat.getColor(resources, R.color.accent_light, theme))

        activityLogInBinding.otpVerifyScreen.pinView.setAnimationEnable(true)

    }

    override fun onStart() {
        super.onStart()
        seekSmsReadPermission()
    }

    override fun onStop() {
        unRegisterSmsReceiver()
        super.onStop()
    }

    private val clickCallBack = object : ILogInNavigator {
        override fun afterOtpChanged(editable: Editable) {

            if (pin_view.isFocused && editable.isEmpty()) {
                activityLogInBinding.otpVerifyScreen.pinView.setLineColor(
                        ResourcesCompat.getColor(resources, R.color.accent_light, theme))
            }

            if (pin_view.isFocused && editable.length == 6) {
                hideKeyboard()
                val credential = PhoneAuthProvider.getCredential(logInViewModel.verificationId!!, editable.toString())
                logInViewModel.signInWith(credential)
            }
        }

        override fun afterMobileNumChanged(editable: Editable) {
            if (phone_num_edit_text.isFocused) {
                logInViewModel.mobileNumber.set(editable.toString())
                logInViewModel.validateMobileNum()
            }
        }

        override fun onClickCountryCode()
                = start(CountryPickerFragment.newInstance(null), CountryPickerFragment.TAG)


        override fun onClickNext() {
            hideKeyboard()
            val phoneNumber = logInViewModel.countryCode.get().plus(logInViewModel.mobileNumber.get())
            logInViewModel.startPhoneNumberVerification(this@LoginActivity, phoneNumber)
        }

        override fun onClickResendOtp() {
            hideKeyboard()
            val phoneNumber = logInViewModel.countryCode.get().plus(logInViewModel.mobileNumber.get())
            logInViewModel.resendOtp(this@LoginActivity, phoneNumber, logInViewModel.token!!)
        }

    }

    private fun observeAuthModelChange()
            = logInViewModel.phoneAuthModelLiveData.observe(this@LoginActivity) {
        onPhoneAuthModelChange(it)
    }

    private fun observerNetworkChange() = logInViewModel.isNetConnected.observe(this@LoginActivity) {
        if (it == false) {
            logInViewModel.isSnackBarAlive = true
            hideKeyboard()
            snackBarConfiguration.showNoNetworkSnackBar(getString(R.string.str_no_internet_title),
                    getString(R.string.str_dismiss), View.OnClickListener { })
        } else {
            if (logInViewModel.isSnackBarAlive) {
                snackBarConfiguration.showSnackBar(getString(R.string.str_internet_title), SnackbarConfiguration.Type.VALID)
            }
        }
    }

    private fun observerApiCallErrorChange() = logInViewModel.apiError.observe(this@LoginActivity) {
        if (it == true) {
            hideKeyboard()
            snackBarConfiguration.showSnackBar(getString(R.string.str_something_wrong_msg), SnackbarConfiguration.Type.NEUTRAL)
        }
    }


    private fun onPhoneAuthModelChange(phoneAuthDataModel: PhoneAuthDataModel?) {

        when (phoneAuthDataModel?.state) {

            LogInViewModel.State.STATE_CODE_SENT -> {
                logInViewModel.verificationId = phoneAuthDataModel.verificationId
                logInViewModel.token = phoneAuthDataModel.token

                if (logInViewModel.isSmsReadPermissionGranted.get()) {
                    logInViewModel.state.set(LogInViewModel.State.STATE_AUTO_VERIFICATION)
                    logInViewModel.autoVerification()
                } else {
                    activityLogInBinding.otpVerifyScreen.pinView.setLineColor(
                            ResourcesCompat.getColor(resources, R.color.accent_light, theme))
                    logInViewModel.state.set(LogInViewModel.State.STATE_OTP_VERIFICATION)
                }
            }

            LogInViewModel.State.STATE_VERIFY_SUCCESS -> {
                logInViewModel.signInWith(phoneAuthDataModel.phoneAuthCredential!!)
            }

            LogInViewModel.State.STATE_SIGNIN_FAILED ->
                activityLogInBinding.otpVerifyScreen.pinView.setLineColor(
                        ResourcesCompat.getColor(resources, R.color.faded_red, theme))

            LogInViewModel.State.STATE_SIGNIN_SUCCESS -> {
                activityLogInBinding.otpVerifyScreen.pinView.setLineColor(
                        ResourcesCompat.getColor(resources, R.color.accent_light, theme))
                logInViewModel.saveUser(phoneAuthDataModel.user!!)
            }

            LogInViewModel.State.STATE_USER_WRITE_SUCCESS -> {
                startAddGameActivity()
                finish()
            }

            else -> {

            }
        }
    }

    override fun onFragmentInteraction(bundle: Bundle?) {
        val country: Country? = bundle?.getParcelable(COUNTRY_EXTRA)
        logInViewModel.countryCode.set(country?.isoCode)
        if (logInViewModel.mobileNumber.get() != null) {
            logInViewModel.validateMobileNum()
        }
    }

    override fun onSMSReceived(bundle: Bundle?) {
        val code = bundle?.getString(SMSReceiver.VERIFICATION_CODE, "")
        activityLogInBinding.otpVerifyScreen.pinView.setText(code)
        activityLogInBinding.otpVerifyScreen.pinView.setLineColor(
                ResourcesCompat.getColor(resources, R.color.accent_light, theme))
        logInViewModel.state.set(LogInViewModel.State.STATE_OTP_VERIFICATION)
    }

    override fun supportFragmentInjector(): AndroidInjector<Fragment> = fragmentDispatchingAndroidInjector

    override fun broadcastReceiverInjector(): AndroidInjector<BroadcastReceiver> = broadcastReceiverDispatchingAndroidInjector


    override fun onPermissionsDenied(requestCode: Int, perms: MutableList<String>?)
            = logInViewModel.isSmsReadPermissionGranted.set(false)


    override fun onPermissionsGranted(requestCode: Int, perms: MutableList<String>?) {

    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray)
            = EasyPermissions.onRequestPermissionsResult(requestCode, permissions, grantResults, this@LoginActivity)


    @AfterPermissionGranted(RC_SMS_PERM)
    private fun seekSmsReadPermission() {
        if (EasyPermissions.hasPermissions(this@LoginActivity, Manifest.permission.RECEIVE_SMS)) {
            attachReceiver()
            logInViewModel.isSmsReadPermissionGranted.set(true)
        } else {
            EasyPermissions.requestPermissions(this@LoginActivity, getString(R.string.rationale_sms),
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

    private fun attachReceiver() {
        if (smsReceiver == null) {
            smsReceiver = SMSReceiver(this@LoginActivity)
        }
        registerSmsReceiver()
    }

    companion object {
        const val RC_SMS_PERM: Int = 123

        fun start(context: Context)
                = Intent(context, LoginActivity::class.java)
    }

}
