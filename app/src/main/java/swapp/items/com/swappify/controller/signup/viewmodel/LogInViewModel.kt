package swapp.items.com.swappify.controller.signup.viewmodel

import android.app.Activity
import android.databinding.ObservableBoolean
import android.databinding.ObservableField
import android.databinding.ObservableInt
import com.google.firebase.FirebaseNetworkException
import com.google.firebase.auth.PhoneAuthCredential
import com.google.firebase.auth.PhoneAuthProvider
import com.google.i18n.phonenumbers.PhoneNumberUtil
import io.reactivex.Single
import swapp.items.com.swappify.common.AppUtils.getLocale
import swapp.items.com.swappify.common.AppUtils.isValidPhone
import swapp.items.com.swappify.common.Constant.Companion.USER_ID
import swapp.items.com.swappify.common.Constant.Companion.USER_PHONE_NUM
import swapp.items.com.swappify.common.extension.firebaseResponseToResult
import swapp.items.com.swappify.controller.SwapApplication
import swapp.items.com.swappify.controller.base.BaseViewModel
import swapp.items.com.swappify.controller.signup.model.PhoneAuthDataModel
import swapp.items.com.swappify.firebase.utils.Result
import swapp.items.com.swappify.injection.scopes.PerActivity
import swapp.items.com.swappify.mvvm.SingleLiveEvent
import swapp.items.com.swappify.repo.user.model.User
import javax.inject.Inject

@PerActivity
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

    fun validateMobileNum() = phoneError.apply { set(isValidPhone(mobileNumber.get(), countryCode.get())) }


    fun startPhoneNumberVerification(activity: Activity, phoneNumber: String) = getCompositeDisposable().add(
            loginRepository.startPhoneVerification(phoneNumber, activity)
                    .doOnSubscribe { isLoading.set(true) }
                    .doAfterTerminate { isLoading.set(false) }
                    .subscribe({ handleOnSuccess(it) }, { handleOnError(it) })
    )

    fun resendOtp(activity: Activity, phoneNumber: String, token: PhoneAuthProvider.ForceResendingToken) = getCompositeDisposable().add(
            loginRepository.resendVerificationCode(phoneNumber, activity, token)
                    .doOnSubscribe { isLoading.set(true) }
                    .doAfterTerminate { isLoading.set(false) }
                    .subscribe({ handleOnSuccess(it) }, { handleOnError(it) })
    )

    private fun handleOnSuccess(result: Result<PhoneAuthDataModel>?) =
            if (result!!.isSuccess()) phoneAuthModelLiveData.value = result.value else handleOnError(result.error!!)


    private fun handleOnError(error: Throwable) = when (error) {
        is FirebaseNetworkException ->
            isNetConnected.value = false
        else ->
            this.apiError.value = true
    }

    fun autoVerification() = getCompositeDisposable().add(
            loginRepository.initAutoVerify(60)
                    .subscribe({
                        remainingTime.set(it)
                        if (remainingTime.get() == 0) {
                            state.set(State.STATE_OTP_VERIFICATION)
                        }
                    })
    )

    fun signInWith(credential: PhoneAuthCredential) = getCompositeDisposable().add(
            loginRepository.signInWith(credential)
                    .doOnSubscribe { isLoading.set(true) }
                    .doAfterTerminate { isLoading.set(false) }
                    .subscribe({ handleOnSuccess(it) }, { handleOnError(it) })
    )

    fun verifyAndSave(user: User) =
            loginRepository.getUser(user.userNumber!!)
                    .doOnSubscribe { isLoading.set(true) }
                    .doAfterTerminate { isLoading.set(false) }
                    .toFlatMap(user)
                    .toSubscribe()


    private fun Single<Result<User>>.toFlatMap(user: User) =
            this.flatMap {
                when {
                    it.isSuccess() -> {
                        if (it.value.userNumber.isNullOrEmpty()) {
                            loginRepository.saveUser(user)
                        } else {
                            Single.just<User>(it.value)
                                    .firebaseResponseToResult()
                        }
                    }
                    else -> Single.error(it.error)
                }
            }

    private fun Single<Result<User>>.toSubscribe() =
            this.subscribe({
                if (it.isSuccess()) {
                    val userUpdated = it.value
                    val preferenceUtils = loginRepository.appUtilManager.preferencesHelper
                    preferenceUtils.set(USER_PHONE_NUM, userUpdated.userNumber)
                    preferenceUtils.set(USER_ID, userUpdated.userID)

                    val phoneAuthDataModel = PhoneAuthDataModel.create {
                        currentUser { userUpdated }
                        state { State.STATE_USER_WRITE_SUCCESS }
                    }

                    handleOnSuccess(Result(phoneAuthDataModel, null))
                } else {
                    handleOnError(it.error!!)
                }
            }, {
                handleOnError(it)
            })

}