package swapp.items.com.swappify.data.auth

import android.app.Activity
import com.google.firebase.auth.PhoneAuthProvider
import io.reactivex.Single
import swapp.items.com.swappify.controllers.signup.model.PhoneAuthDataModel
import swapp.items.com.swappify.data.auth.datasource.AuthDataSourceHelper
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AuthDataSourceRemote @Inject constructor(val authDataSourceHelper: AuthDataSourceHelper): AuthDataSourceRemoteHelper {

    override fun startPhoneVerification(phoneNumber: String, activity: Activity): Single<PhoneAuthDataModel> =
            authDataSourceHelper.startPhoneVerificationObservable(phoneNumber = phoneNumber, activity = activity)

    override fun resendVerificationCode(phoneNumber: String, activity: Activity, token: PhoneAuthProvider.ForceResendingToken): Single<PhoneAuthDataModel> =
            authDataSourceHelper.resendVerificationCodeObservable(phoneNumber = phoneNumber, activity = activity, token = token)

}