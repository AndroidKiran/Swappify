package swapp.items.com.swappify.controllers.signup.viewmodel

import android.app.Activity
import android.arch.lifecycle.MutableLiveData
import android.databinding.ObservableField
import android.text.TextUtils
import com.google.firebase.auth.PhoneAuthCredential
import com.google.firebase.auth.PhoneAuthProvider
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import swapp.items.com.swappify.R
import swapp.items.com.swappify.controllers.SwapApplication
import swapp.items.com.swappify.controllers.base.BaseViewModel
import swapp.items.com.swappify.controllers.configs.ContentLoadingConfiguration
import swapp.items.com.swappify.controllers.signup.model.PhoneAuthDataModel
import swapp.items.com.swappify.controllers.signup.ui.SignUpLoginActivity
import swapp.items.com.swappify.injection.scopes.PerActivity
import swapp.items.com.swappify.common.extension.getObservableAsync
import swapp.items.com.swappify.common.extension.getSingleAsync
import swapp.items.com.swappify.common.AppUtils.Companion.isValidPhone
import java.util.concurrent.TimeUnit
import javax.inject.Inject

@PerActivity
class SignUpLogInViewModel @Inject constructor(signUpLoginDataManager: SignUpLoginDataManager, application: SwapApplication) : BaseViewModel(application) {

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

    var verifyCodeScreenEnabled = ObservableField<Boolean>(false)

    var countryCode = ObservableField<String>()

    var mobileNumber = ObservableField<String>()

    var otpCode = ObservableField<String>()

    var enableNxtBtn = ObservableField<Boolean>()

    var enableOtpVerificationBtn = ObservableField<Boolean>()

    var phoneError = ObservableField<String>()

    var otpError = ObservableField<String>()

    var verificationCode: String? = null

    var token: PhoneAuthProvider.ForceResendingToken? = null

    var contentLoadingConfigView: ObservableField<ContentLoadingConfiguration> = ObservableField<ContentLoadingConfiguration>(ContentLoadingConfiguration(false, ""))

    var countDownValue: Int? = SignUpLoginActivity.MAX_TIME_IN_SEC

    private var seekPermission = MutableLiveData<Boolean>()

    private val schedulerProvider = signUpLoginDataManager.appUtilManager.schedulerProvider

    private val loginRemoteService = signUpLoginDataManager.loginRemoteService


    fun onClickVerifyOtp() {
        updateContentLoading(true, getApplication<SwapApplication>().getString(R.string.msg_validating))
        val credential = PhoneAuthProvider.getCredential(verificationCode!!, otpCode.get())
        authenticateWithFirebase(credential = credential)
    }

    internal fun startPhoneNumberVerification(activity: Activity) {
        val phoneNumber: String = countryCode.get().plus(mobileNumber.get())
        getCompositeDisposable().add(
                loginRemoteService.startPhoneVerification(
                        phoneNumber = phoneNumber,
                        activity = activity)
                        .getSingleAsync(schedulerProvider)
                        .subscribe(
                                { handleOnSucces(phoneAuthDataModel = it) },
                                { handleOnError(error = it) }
                        )
        )

    }

    internal fun resendVerificationCode(activity: Activity) {
        val phoneNumber: String = countryCode.get().plus(mobileNumber.get())
        getCompositeDisposable().add(
                loginRemoteService.resendVerificationCode(
                        phoneNumber = phoneNumber,
                        activity = activity,
                        token = token!!)
                        .getSingleAsync(schedulerProvider)
                        .subscribe(
                                { handleOnSucces(phoneAuthDataModel = it) },
                                { handleOnError(error = it) }
                        )
        )
    }

    fun validateNewOtp(newValue: String?) {
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
                seekPermission.value = true
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
//                getNavigator().startHomeActivity()
            }

            STATE_USER_WRITE_FAILED -> {
                updateContentLoading(false)
                saveUser(model = phoneAuthDataModel)
            }

        }
    }

    private fun setPhoneAuthProperties(model: PhoneAuthDataModel?) {
        token = model?.token
        verificationCode = model?.verificationId
    }


    private fun handleOnError(error: Throwable) {

    }

    private fun authenticateWithFirebase(credential: PhoneAuthCredential?) =
        loginRemoteService.loginWithPhoneNumber(credential)
                .getSingleAsync(schedulerProvider)
                .subscribe({ phoneAuthDataModel -> handleOnSucces(phoneAuthDataModel) })

    private fun saveUser(model: PhoneAuthDataModel?) =
        loginRemoteService.saveUserToDB(model)
                .getObservableAsync(schedulerProvider)
                .subscribe({ phoneAuthDataModel -> handleOnSucces(phoneAuthDataModel) })


    fun updateContentLoading(enable: Boolean?, message: String? = getApplication<SwapApplication>()
            .getString(R.string.msg_validating)) {
        val contentLoading = ContentLoadingConfiguration(enable, message)
        contentLoadingConfigView.set(contentLoading)
    }


    fun startOtpCountDown(time: Int) =
            getCompositeDisposable().add(
                    Observable.interval(1, TimeUnit.SECONDS)
                            .subscribeOn(AndroidSchedulers.mainThread())
                            .observeOn(AndroidSchedulers.mainThread())
                            .map({ increaseTime: Long -> time - increaseTime.toInt() })
                            .take(time + 1.toLong())
                            .getObservableAsync(schedulerProvider)
                            .subscribe({ timeLeft: Int -> updateCountDown(timeLeft) })
            )


    private fun updateCountDown(timeLeft: Int) {
        if (timeLeft > 0) {
            updateContentLoading(true, getApplication<SwapApplication>()
                    .getString(R.string.msg_otp_read, countDownValue))
        } else {
            updateContentLoading(false)
        }
        countDownValue = timeLeft
    }

    fun getSeekPermission(): MutableLiveData<Boolean> = seekPermission
}