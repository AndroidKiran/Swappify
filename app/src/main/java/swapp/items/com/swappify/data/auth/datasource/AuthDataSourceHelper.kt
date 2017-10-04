package swapp.items.com.swappify.data.auth.datasource

import android.app.Activity
import com.google.firebase.auth.PhoneAuthProvider
import io.reactivex.Single
import swapp.items.com.swappify.controllers.signup.model.PhoneAuthDataModel

interface AuthDataSourceHelper {

    fun startPhoneVerificationObservable(phoneNumber: String, activity: Activity): Single<PhoneAuthDataModel>

    fun resendVerificationCodeObservable(phoneNumber: String, activity: Activity, token: PhoneAuthProvider.ForceResendingToken): Single<PhoneAuthDataModel>
}