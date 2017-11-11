package swapp.items.com.swappify.controllers.signup.viewModel

import android.app.Activity
import android.databinding.ObservableField
import android.text.TextUtils
import com.google.firebase.auth.PhoneAuthCredential
import com.google.firebase.auth.PhoneAuthProvider
import swapp.items.com.swappify.R
import swapp.items.com.swappify.components.TextChangeListener
import swapp.items.com.swappify.controllers.SwapApplication
import swapp.items.com.swappify.controllers.base.BaseViewModel
import swapp.items.com.swappify.controllers.configs.ContentLoadingConfiguration
import swapp.items.com.swappify.controllers.country.model.Countries
import swapp.items.com.swappify.controllers.signup.model.PhoneAuthDataModel
import swapp.items.com.swappify.controllers.signup.ui.SignUpLogInNavigator
import swapp.items.com.swappify.injection.scopes.PerActivity
import swapp.items.com.swappify.utils.AppUtils.Companion.isValidPhone
import javax.inject.Inject

@PerActivity
class SignUpLogInViewModel @Inject constructor(private val signUpLoginDataManager: SignUpLoginDataManager?, application: SwapApplication) : BaseViewModel<SignUpLogInNavigator>(application) {

    companion object {
        val STATE_INITIALIZED = 1
        val STATE_CODE_SENT = 2
        val STATE_VERIFY_FAILED = 3
        val STATE_VERIFY_SUCCESS = 4
        val STATE_SIGNIN_FAILED = 5
        val STATE_SIGNIN_SUCCESS = 6
        val STATE_USER_WRITE_FAILED = 7
        val STATE_USER_WRITE_SUCCESS = 8
    }

    var verifyCodeScreenEnabled: ObservableField<Boolean> = ObservableField<Boolean>()

    var countryCode: ObservableField<String> = ObservableField<String>()

    var selectedCountry: ObservableField<Countries.Country> = ObservableField<Countries.Country>()

    var mobileNumber: ObservableField<String> = ObservableField<String>()

    var otpCode: ObservableField<String> = ObservableField<String>()

    var enableNxtBtn: ObservableField<Boolean> = ObservableField<Boolean>()

    var enableOtpVerificationBtn: ObservableField<Boolean> = ObservableField<Boolean>()

    var phoneError: ObservableField<String> = ObservableField<String>()

    var otpError: ObservableField<String> = ObservableField<String>()

    var verificationCode: ObservableField<String> = ObservableField<String>()

    var token: ObservableField<PhoneAuthProvider.ForceResendingToken> =
            ObservableField<PhoneAuthProvider.ForceResendingToken>()

    var contentLoadingConfigView: ObservableField<ContentLoadingConfiguration> = ObservableField<ContentLoadingConfiguration>()

    var countDownValue: Int? = 0

    init {
        verifyCodeScreenEnabled.set(false)
        enableNxtBtn.set(false)
        updateContentLoading(false)
        enableOtpVerificationBtn.set(false)
    }


    fun onCodeClick() {
        getNavigator().openCountryCodeDialog()
    }

    fun onNextClick() {
        phoneError.set(null)
        updateContentLoading(true)
        getNavigator().onNextClick()
    }

    fun onResendClick() {
        updateContentLoading(true, getApplication<SwapApplication>().getString(R.string.msg_otp_resend))
        getNavigator().onResendClick()
    }

    fun onVerifyOtpClick() {
        updateContentLoading(true, getApplication<SwapApplication>().getString(R.string.msg_validating))
        val credential = PhoneAuthProvider.getCredential(verificationCode.get(), otpCode.get())
        authenticateWithFirebase(credential = credential)
    }

    internal fun startPhoneNumberVerification(activity: Activity) {
        val phoneNumber: String = countryCode.get().plus(mobileNumber.get())
        getCompositeDisposable().add(
                signUpLoginDataManager?.loginRemoteService?.startPhoneVerification(
                        phoneNumber = phoneNumber,
                        activity = activity)!!
                        .subscribe(
                                { handleOnSucces(phoneAuthDataModel = it) },
                                { handleOnError(error = it) }
                        )
        )

    }

    internal fun resendVerificationCode(activity: Activity) {
        val phoneNumber: String = countryCode.get().plus(mobileNumber.get())
        getCompositeDisposable().add(
                signUpLoginDataManager?.loginRemoteService?.resendVerificationCode(
                        phoneNumber = phoneNumber,
                        activity = activity,
                        token = token.get())!!
                        .subscribe(
                                { handleOnSucces(phoneAuthDataModel = it) },
                                { handleOnError(error = it) }
                        )
        )
    }


    var phoneNumberTextWatcher: TextChangeListener = object : TextChangeListener() {

        override fun afterTextChanged(newValue: String?) {
            mobileNumber.set(newValue)
            validatePhoneNum()
        }
    }

    var verificationCodeTextWatcher: TextChangeListener = object : TextChangeListener() {

        override fun afterTextChanged(newValue: String?) {
            if (TextUtils.isEmpty(newValue)) {
                enableOtpVerificationBtn.set(false)
            } else {
                validateNewOtp(newValue)
            }
        }
    }

    internal fun validateNewOtp(newValue: String?) {
        if (newValue != otpCode.get()) {
            otpCode.set(newValue)
            otpError.set(null)
            enableOtpVerificationBtn.set(true)
        } else {
            otpError.set(getApplication<SwapApplication>().getString(R.string.err_invalid_otp))
            enableOtpVerificationBtn.set(false)
        }
    }

    fun validatePhoneNum() {
        if (!TextUtils.isEmpty(mobileNumber.get()) && !TextUtils.isEmpty(countryCode.get())) {
            val isValidPhoneNum = isValidPhone(mobileNumber = mobileNumber.get(),
                    countryCode = countryCode.get())
            val errorMsg: String? = if (isValidPhoneNum) null else getApplication<SwapApplication>().getString(R.string.err_verify_number)
            phoneError.set(errorMsg)
            enableNxtBtn.set(isValidPhoneNum)
        } else {
            phoneError.set(null)
            enableNxtBtn.set(false)
        }
    }

    private fun handleOnSucces(phoneAuthDataModel: PhoneAuthDataModel?) {
        when (phoneAuthDataModel?.state) {
            STATE_CODE_SENT -> {
                updateContentLoading(false)
                verifyCodeScreenEnabled.set(true)
                setPhoneAuthProperties(model = phoneAuthDataModel)
                getNavigator().seekSmsReadPermission()
            }

            STATE_VERIFY_FAILED -> {
                updateContentLoading(false)
                phoneError.set(getApplication<SwapApplication>().getString(R.string.err_invalid_phone))
            }

            STATE_VERIFY_SUCCESS -> {
                authenticateWithFirebase(credential = phoneAuthDataModel.phoneAuthCredential)
            }

            STATE_SIGNIN_FAILED -> {
                updateContentLoading(false)
                otpError.set(getApplication<SwapApplication>().getString(R.string.err_invalid_otp))
                enableOtpVerificationBtn.set(false)
            }

            STATE_SIGNIN_SUCCESS -> {
                saveUser(model = phoneAuthDataModel)
            }

            STATE_USER_WRITE_SUCCESS -> {
                updateContentLoading(false)
                getNavigator().startHomeActivity()
            }

            STATE_USER_WRITE_FAILED -> {
                updateContentLoading(false)
                saveUser(model = phoneAuthDataModel)
            }

        }
    }

    private fun setPhoneAuthProperties(model: PhoneAuthDataModel?) {
        token.set(model?.token)
        verificationCode.set(model?.verificationId)
    }


    private fun handleOnError(error: Throwable) {

    }

    private fun authenticateWithFirebase(credential: PhoneAuthCredential?) {
        signUpLoginDataManager?.loginRemoteService?.loginWithPhoneNumber(credential)!!
                .subscribe({ phoneAuthDataModel -> handleOnSucces(phoneAuthDataModel) })
    }

    private fun saveUser(model: PhoneAuthDataModel?) {
        signUpLoginDataManager?.loginRemoteService?.saveUserToDB(model)!!
                .subscribe({ phoneAuthDataModel -> handleOnSucces(phoneAuthDataModel) })
    }

    fun updateContentLoading(enable: Boolean?, message: String? = getApplication<SwapApplication>().getString(R.string.msg_validating)) {
        val contentLoading: ContentLoadingConfiguration
        if (contentLoadingConfigView.get() == null) {
            contentLoading = ContentLoadingConfiguration()
        } else {
            contentLoading = contentLoadingConfigView.get()
        }
        contentLoading.contentLoadingText.set(message)
        contentLoading.isContentLoading.set(enable)
        contentLoadingConfigView.set(contentLoading)
    }

}