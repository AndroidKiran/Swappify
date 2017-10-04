package swapp.items.com.swappify.controllers.signup.model

import android.app.Activity
import com.google.firebase.auth.PhoneAuthProvider
import swapp.items.com.swappify.base.BaseViewModel
import swapp.items.com.swappify.controllers.signup.SignUpLogInNavigator
import swapp.items.com.swappify.data.DataManagerHelper

/**
 * Created by ravi on 02/10/17.
 */
class SignUpLogInViewModel (dataManagerHelper: DataManagerHelper) : BaseViewModel<SignUpLogInNavigator> (dataManagerHelper) {

    companion object {
        val STATE_INITIALIZED = 1
        val STATE_CODE_SENT = 2
        val STATE_VERIFY_FAILED = 3
        val STATE_VERIFY_SUCCESS = 4
        val STATE_SIGNIN_FAILED = 5
        val STATE_SIGNIN_SUCCESS = 6
    }


    fun onCodeClick() {
        getNavigator().openCountryCodeDialog()
    }

    fun onPhoneVerificationClick() {
        getNavigator().verifyPhoneNumber()
    }


    private fun startPhoneNumberVerification(phoneNumber: String, activity: Activity) {
        getCompositeDisposable().add(
                getDataManager().getAuthRespository().startPhoneVerification(phoneNumber = phoneNumber,
                        activity = activity)
                        .subscribe(
                                { getNavigator().handleOnSucces(phoneAuthDataModel = it) },
                                { getNavigator().handleOnError(error = it) }
                        )
        )

    }

    private fun resendVerificationCode(phoneNumber: String, token: PhoneAuthProvider.ForceResendingToken, activity: Activity) {
        getCompositeDisposable().add(
                getDataManager().getAuthRespository().resendVerificationCode(phoneNumber = phoneNumber,
                        activity = activity,
                        token = token)
                        .subscribe(
                                { getNavigator().handleOnSucces(phoneAuthDataModel = it) },
                                { getNavigator().handleOnError(error = it) }
                        )
        )
    }
}