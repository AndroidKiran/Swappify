package swapp.items.com.swappify.data.auth.datasource

import android.app.Activity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.PhoneAuthCredential
import com.google.firebase.auth.PhoneAuthProvider
import io.reactivex.Single
import swapp.items.com.swappify.controllers.signup.model.PhoneAuthDataModel
import swapp.items.com.swappify.controllers.signup.viewModel.SignUpLogInViewModel
import swapp.items.com.swappify.data.user.model.User
import swapp.items.com.swappify.firebase.listeners.FirebaseObservableListeners
import swapp.items.com.swappify.injection.scopes.PerActivity
import javax.inject.Inject

@PerActivity
class AuthDataSource @Inject constructor(val firebaseAuth: FirebaseAuth, val firebaseObservableListeners: FirebaseObservableListeners) {

    fun startPhoneVerificationObservable(phoneNumber: String, activity: Activity): Single<PhoneAuthDataModel> =
            firebaseObservableListeners.startPhoneVerificationListener(phoneNumber = phoneNumber, activity = activity)

    fun resendVerificationCodeObservable(phoneNumber: String, activity: Activity, token: PhoneAuthProvider.ForceResendingToken): Single<PhoneAuthDataModel> =
            firebaseObservableListeners.resendVerificationCodeListener(phoneNumber = phoneNumber, activity = activity, token = token)

    fun loginWithPhoneNumber(credential: PhoneAuthCredential?): Single<PhoneAuthDataModel> =
            Single.create<PhoneAuthDataModel>({ emitter ->
                firebaseAuth.signInWithCredential(credential!!)
                        .addOnCompleteListener({ task ->
                            val phoneAuthDataModel: PhoneAuthDataModel?
                            if (task.isSuccessful) {
                                val currentUser: FirebaseUser? = task.result.user
                                phoneAuthDataModel = PhoneAuthDataModel.create {
                                    state { SignUpLogInViewModel.STATE_SIGNIN_SUCCESS }
                                    currentUser { User(currentUser?.uid, currentUser?.phoneNumber) }
                                }
                            } else {
                                phoneAuthDataModel = PhoneAuthDataModel.create {
                                    state { SignUpLogInViewModel.STATE_SIGNIN_FAILED }
                                }
                            }
                            emitter.onSuccess(phoneAuthDataModel)
                        })
            })


}