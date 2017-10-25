package swapp.items.com.swappify.data.auth

import android.app.Activity
import com.google.firebase.auth.PhoneAuthProvider
import io.reactivex.Single
import swapp.items.com.swappify.controllers.signup.model.PhoneAuthDataModel
import swapp.items.com.swappify.data.auth.datasource.AuthDataSource
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AuthDataSourceRemote @Inject constructor(val authDataSource: AuthDataSource) {

    fun startPhoneVerification(phoneNumber: String, activity: Activity): Single<PhoneAuthDataModel> =
            authDataSource.startPhoneVerificationObservable(phoneNumber = phoneNumber, activity = activity)

    fun resendVerificationCode(phoneNumber: String, activity: Activity, token: PhoneAuthProvider.ForceResendingToken): Single<PhoneAuthDataModel> =
            authDataSource.resendVerificationCodeObservable(phoneNumber = phoneNumber, activity = activity, token = token)

}