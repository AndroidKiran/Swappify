package swapp.items.com.swappify.controllers.signup.viewmodel

import android.app.Activity
import com.google.firebase.auth.PhoneAuthProvider
import swapp.items.com.swappify.controllers.base.BaseViewModel
import swapp.items.com.swappify.controllers.signup.SignUpLogInNavigator
import swapp.items.com.swappify.data.DataManagerHelper

class SignUpLogInViewModel(dataManagerHelper: DataManagerHelper) : BaseViewModel<SignUpLogInNavigator>(dataManagerHelper) {

    companion object {
        val STATE_INITIALIZED = 1
        val STATE_CODE_SENT = 2
        val STATE_VERIFY_FAILED = 3
        val STATE_VERIFY_SUCCESS = 4
        val STATE_SIGNIN_FAILED = 5
        val STATE_SIGNIN_SUCCESS = 6
    }


    fun onCodeClick() {
        baseNavigator.openCountryCodeDialog()
    }

    fun onPhoneVerificationClick() {
        baseNavigator.verifyPhoneNumber()
    }


    private fun startPhoneNumberVerification(phoneNumber: String, activity: Activity) {
        baseCompositeDisposable.add(
                dataManager.getAuthRespository().startPhoneVerification(phoneNumber = phoneNumber,
                        activity = activity)
                        .subscribe(
                                { baseNavigator.handleOnSucces(phoneAuthDataModel = it) },
                                { baseNavigator.handleOnError(error = it) }
                        )
        )

    }

    private fun resendVerificationCode(phoneNumber: String, token: PhoneAuthProvider.ForceResendingToken, activity: Activity) {
        baseCompositeDisposable.add(
                dataManager.getAuthRespository().resendVerificationCode(phoneNumber = phoneNumber,
                        activity = activity,
                        token = token)
                        .subscribe(
                                { baseNavigator.handleOnSucces(phoneAuthDataModel = it) },
                                { baseNavigator.handleOnError(error = it) }
                        )
        )
    }
}