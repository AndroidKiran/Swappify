package swapp.items.com.swappify.firebase.listeners

import android.app.Activity
import com.google.firebase.auth.PhoneAuthProvider
import io.reactivex.Single
import swapp.items.com.swappify.controllers.signup.model.PhoneAuthDataModel

interface FirebaseObsevableListenerHelper {

    fun startPhoneVerificationListener(phoneNumber: String, activity: Activity): Single<PhoneAuthDataModel>

    fun resendVerificationCodeListener(phoneNumber: String, activity: Activity, token: PhoneAuthProvider.ForceResendingToken): Single<PhoneAuthDataModel>
}