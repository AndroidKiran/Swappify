package swapp.items.com.swappify.controllers.signup.viewmodel

import android.app.Activity
import android.databinding.ObservableBoolean
import android.databinding.ObservableField
import android.databinding.ObservableInt
import com.google.firebase.FirebaseNetworkException
import com.google.firebase.auth.PhoneAuthCredential
import com.google.firebase.auth.PhoneAuthProvider
import com.google.i18n.phonenumbers.PhoneNumberUtil
import swapp.items.com.swappify.common.AppUtils.Companion.getLocale
import swapp.items.com.swappify.common.AppUtils.Companion.isValidPhone
import swapp.items.com.swappify.common.SingleLiveEvent
import swapp.items.com.swappify.controllers.SwapApplication
import swapp.items.com.swappify.controllers.base.BaseViewModel
import swapp.items.com.swappify.controllers.signup.model.PhoneAuthDataModel
import swapp.items.com.swappify.firebase.utils.Result
import swapp.items.com.swappify.repo.user.model.User
import javax.inject.Inject

class LogInViewModel @Inject constructor(loginDataManager: LoginDataManager, swapApplication: SwapApplication) : BaseViewModel(swapApplication) {

    var state = ObservableField<State>(State.STATE_INITIALIZED)

    var countryCode = ObservableField<String>(prefetchCountryCode())

    var mobileNumber = ObservableField<String>()

    var phoneError = ObservableBoolean(true)

    var isSmsReadPermissionGranted = ObservableBoolean()

    var remainingTime = ObservableInt(60)

    var isLoading = ObservableBoolean(false)

    var phoneAuthModelLiveData = SingleLiveEvent<PhoneAuthDataModel>()

    var verificationId: String? = null

    var token: PhoneAuthProvider.ForceResendingToken? = null

    private var loginRepository = loginDataManager.loginRepository

    enum class State {
        STATE_INITIALIZED,
        STATE_CODE_SENT,
        STATE_AUTO_VERIFICATION,
        STATE_OTP_VERIFICATION,
        STATE_VERIFY_SUCCESS,
        STATE_VERIFY_FAILED,
        STATE_SIGNIN_FAILED,
        STATE_USER_WRITE_FAILED,
        STATE_USER_WRITE_SUCCESS,
        STATE_SIGNIN_SUCCESS
    }

    private fun prefetchCountryCode(): String {
        val countryISOCode: String = getLocale(getApplication()).country
        val code: Int = PhoneNumberUtil.getInstance().getCountryCodeForRegion(countryISOCode)
        var countryCode: String = "+".plus(code)
        if (code == 0) {
            countryCode = "+91"
        }
        return countryCode
    }

    fun validateMobileNum()
            = phoneError.set(isValidPhone(mobileNumber.get(), countryCode.get()))


    fun startPhoneNumberVerification(activity: Activity, phoneNumber: String)
            = getCompositeDisposable().add(
            loginRepository.startPhoneVerification(phoneNumber, activity)
                    .doOnSubscribe { isLoading.set(true) }
                    .doAfterTerminate { isLoading.set(false) }
                    .subscribe({ handleOnSuccess(it) }, { handleOnError(it) }))

    fun resendOtp(activity: Activity, phoneNumber: String, token: PhoneAuthProvider.ForceResendingToken)
            = getCompositeDisposable().add(
            loginRepository.resendVerificationCode(phoneNumber, activity, token)
                    .doOnSubscribe { isLoading.set(true) }
                    .doAfterTerminate { isLoading.set(false) }
                    .subscribe({ handleOnSuccess(it) }, { handleOnError(it) }))

    private fun handleOnSuccess(result: Result<PhoneAuthDataModel>?)
            = if (result!!.isSuccess()) phoneAuthModelLiveData.value = result.value else handleOnError(result.error!!)


    private fun handleOnError(error: Throwable) = when (error) {
        is FirebaseNetworkException ->
            isNetConnected.value = false
        else ->
            this.apiError.value = true
    }

    fun autoVerification()
            = getCompositeDisposable().add(
            loginRepository.initAutoVerify(60)
                    .subscribe({
                        remainingTime.set(it)
                        if (remainingTime.get() == 0) {
                            state.set(State.STATE_OTP_VERIFICATION)
                        }
                    }))

    fun signInWith(credential: PhoneAuthCredential)
            = getCompositeDisposable().add(
            loginRepository.signInWith(credential)
                    .doOnSubscribe { isLoading.set(true) }
                    .doAfterTerminate { isLoading.set(false) }
                    .subscribe({ handleOnSuccess(it) }, { handleOnError(it) }))

    fun saveUser(user: User)
            = getCompositeDisposable().add(
            loginRepository.saveUser(user)
                    .doOnSubscribe { isLoading.set(true) }
                    .doAfterTerminate { isLoading.set(false) }
                    .subscribe({
                        if (it.isSuccess()) {
                            val phoneAuthDataModel = PhoneAuthDataModel.create {
                                state { State.STATE_USER_WRITE_SUCCESS }
                            }
                            handleOnSuccess(Result(phoneAuthDataModel, null))
                        } else {
                            handleOnError(it.error!!)
                        }
                    }, {
                        handleOnError(it)
                    }))

}