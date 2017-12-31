package swapp.items.com.swappify.repo.auth.datasource

import android.app.Activity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.PhoneAuthCredential
import com.google.firebase.auth.PhoneAuthProvider
import io.reactivex.Single
import swapp.items.com.swappify.controllers.signup.model.PhoneAuthDataModel
import swapp.items.com.swappify.controllers.signup.viewmodel.SignUpLogInViewModel
import swapp.items.com.swappify.firebase.listener.FirebaseObservableListener
import swapp.items.com.swappify.injection.scopes.PerActivity
import swapp.items.com.swappify.repo.user.model.User
import javax.inject.Inject

@PerActivity
class AuthDataSource @Inject constructor(private val firebaseAuth: FirebaseAuth, private val firebaseObservableListener: FirebaseObservableListener) {

    fun startPhoneVerificationObservable(phoneNumber: String, activity: Activity): Single<PhoneAuthDataModel> =
            firebaseObservableListener.startPhoneVerificationListener(phoneNumber = phoneNumber, activity = activity)

    fun resendVerificationCodeObservable(phoneNumber: String, activity: Activity, token: PhoneAuthProvider.ForceResendingToken): Single<PhoneAuthDataModel> =
            firebaseObservableListener.resendVerificationCodeListener(phoneNumber = phoneNumber, activity = activity, token = token)

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