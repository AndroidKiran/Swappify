package swapp.items.com.swappify.repo.auth.datasource

import android.app.Activity
import com.google.firebase.FirebaseException
import com.google.firebase.FirebaseNetworkException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.PhoneAuthCredential
import com.google.firebase.auth.PhoneAuthProvider
import io.reactivex.Single
import swapp.items.com.swappify.controller.signup.model.PhoneAuthDataModel
import swapp.items.com.swappify.controller.signup.viewmodel.LogInViewModel
import swapp.items.com.swappify.firebase.listener.FirebaseObservableListener
import swapp.items.com.swappify.injection.scopes.PerActivity
import swapp.items.com.swappify.repo.user.model.User
import javax.inject.Inject

@PerActivity
class AuthDataSource @Inject constructor(private val firebaseAuth: FirebaseAuth, private val firebaseObservableListener: FirebaseObservableListener) {

    fun startPhoneVerificationObservable(phoneNumber: String, activity: Activity) =
            firebaseObservableListener.startPhoneVerificationListener(phoneNumber = phoneNumber, activity = activity)

    fun resendVerificationCodeObservable(phoneNumber: String, activity: Activity, token: PhoneAuthProvider.ForceResendingToken) =
            firebaseObservableListener.resendVerificationCodeListener(phoneNumber = phoneNumber, activity = activity, token = token)

    fun signInWith(credential: PhoneAuthCredential?) =
            Single.create<PhoneAuthDataModel>({ emitter ->
                firebaseAuth.signInWithCredential(credential!!)
                        .addOnCompleteListener({ task ->
                            val phoneAuthDataModel = if (task.isSuccessful) {
                                val currentUser: FirebaseUser? = task.result.user
                                PhoneAuthDataModel.create {
                                    state { LogInViewModel.State.STATE_SIGNIN_SUCCESS }
                                    currentUser { User(currentUser?.uid, currentUser?.phoneNumber) }
                                }
                            } else {
                                PhoneAuthDataModel.create {
                                    firebaseException { FirebaseException(task.exception.toString()) }
                                    state { LogInViewModel.State.STATE_SIGNIN_FAILED }
                                }
                            }

                            if (!task.isSuccessful && task.exception is FirebaseNetworkException) {
                                emitter.onError(task.exception!!)
                            } else {
                                emitter.onSuccess(phoneAuthDataModel)
                            }
                        })
            })


}