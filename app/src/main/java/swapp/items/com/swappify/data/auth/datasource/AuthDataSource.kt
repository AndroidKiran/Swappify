package swapp.items.com.swappify.data.auth.datasource

import android.app.Activity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.PhoneAuthProvider
import io.reactivex.Single
import swapp.items.com.swappify.controllers.signup.model.PhoneAuthDataModel
import swapp.items.com.swappify.firebase.listeners.FirebaseObservableListeners
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AuthDataSource @Inject constructor(val firebaseAuth: FirebaseAuth, val firebaseObservableListeners: FirebaseObservableListeners) {

    fun startPhoneVerificationObservable(phoneNumber: String, activity: Activity): Single<PhoneAuthDataModel> =
            firebaseObservableListeners.startPhoneVerificationListener(phoneNumber = phoneNumber, activity = activity)

    fun resendVerificationCodeObservable(phoneNumber: String, activity: Activity, token: PhoneAuthProvider.ForceResendingToken): Single<PhoneAuthDataModel> =
            firebaseObservableListeners.resendVerificationCodeListener(phoneNumber = phoneNumber, activity = activity, token = token)
}