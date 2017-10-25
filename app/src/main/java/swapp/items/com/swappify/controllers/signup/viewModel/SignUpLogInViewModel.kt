package swapp.items.com.swappify.controllers.signup.viewModel

import android.app.Activity
import android.databinding.ObservableField
import com.google.firebase.auth.PhoneAuthProvider
import swapp.items.com.swappify.controllers.base.BaseViewModel
import swapp.items.com.swappify.controllers.country.model.Countries
import swapp.items.com.swappify.controllers.signup.SignUpLogInNavigator
import swapp.items.com.swappify.data.AppDataManager


class SignUpLogInViewModel constructor(dataManager: AppDataManager) : BaseViewModel<SignUpLogInNavigator>(dataManager) {

    companion object {
        val STATE_INITIALIZED = 1
        val STATE_CODE_SENT = 2
        val STATE_VERIFY_FAILED = 3
        val STATE_VERIFY_SUCCESS = 4
        val STATE_SIGNIN_FAILED = 5
        val STATE_SIGNIN_SUCCESS = 6
    }

    var countryCode: ObservableField<String> = ObservableField<String>()

    var mobileNumber: ObservableField<String> = ObservableField<String>()

    var selectedCountry: ObservableField<Countries.Country> = ObservableField<Countries.Country>()

    var enableNxtBtn: ObservableField<Boolean> = ObservableField<Boolean>(false)

    var enableErrorMsg: ObservableField<Boolean> = ObservableField<Boolean>(false)

    init {
        dataManager.preferencesUtils?.set("TEST", "test===value")
        println("preference========== ${dataManager.preferencesUtils?.getStringPreference("TEST")}")
    }

    fun onCodeClick() {
        baseNavigator.openCountryCodeDialog()
    }

    fun onPhoneVerificationClick() {
        baseNavigator.verifyPhoneNumber()
    }

    fun onNextClick(countryCode: String, phoneNumber: String) {
        baseNavigator.onNextClick(countryCode.plus(phoneNumber))
    }

    private fun startPhoneNumberVerification(phoneNumber: String, activity: Activity) {
        baseCompositeDisposable.add(
                dataManager.authDataSourceRemote?.startPhoneVerification(phoneNumber = phoneNumber,
                        activity = activity)
                !!.subscribe(
                        { baseNavigator.handleOnSucces(phoneAuthDataModel = it) },
                        { baseNavigator.handleOnError(error = it) }
                )
        )

    }

    private fun resendVerificationCode(phoneNumber: String, token: PhoneAuthProvider.ForceResendingToken, activity: Activity) {
        baseCompositeDisposable.add(
                dataManager.authDataSourceRemote?.resendVerificationCode(
                        phoneNumber = phoneNumber,
                        activity = activity,
                        token = token)
                !!.subscribe(
                        { baseNavigator.handleOnSucces(phoneAuthDataModel = it) },
                        { baseNavigator.handleOnError(error = it) }
                )
        )
    }
}