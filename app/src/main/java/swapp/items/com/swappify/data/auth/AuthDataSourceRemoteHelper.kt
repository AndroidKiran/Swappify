package swapp.items.com.swappify.data.auth

import android.app.Activity
import com.google.firebase.auth.PhoneAuthProvider
import io.reactivex.Single
import swapp.items.com.swappify.controllers.signup.model.PhoneAuthDataModel


interface AuthDataSourceRemoteHelper {

    fun startPhoneVerification(phoneNumber: String, activity: Activity): Single<PhoneAuthDataModel>

    fun resendVerificationCode(phoneNumber: String, activity: Activity, token: PhoneAuthProvider.ForceResendingToken): Single<PhoneAuthDataModel>

}