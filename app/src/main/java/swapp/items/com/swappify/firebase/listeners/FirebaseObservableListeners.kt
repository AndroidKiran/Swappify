package swapp.items.com.swappify.firebase.listeners

import android.app.Activity
import com.google.firebase.auth.PhoneAuthProvider
import io.reactivex.Single
import swapp.items.com.swappify.controllers.signup.model.PhoneAuthDataModel
import swapp.items.com.swappify.firebase.listeners.authListeners.RxResendVerificationCodeSubscriber
import swapp.items.com.swappify.firebase.listeners.authListeners.RxStartPhoneVerificationSubscriber
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class FirebaseObservableListeners @Inject constructor() {

    fun startPhoneVerificationListener(phoneNumber: String, activity: Activity): Single<PhoneAuthDataModel> =
            Single.create(RxStartPhoneVerificationSubscriber(phoneNumber = phoneNumber, activity = activity))

    fun resendVerificationCodeListener(phoneNumber: String, activity: Activity, token: PhoneAuthProvider.ForceResendingToken): Single<PhoneAuthDataModel> =
            Single.create(RxResendVerificationCodeSubscriber(phoneNumber = phoneNumber, activity = activity, token = token))
}

