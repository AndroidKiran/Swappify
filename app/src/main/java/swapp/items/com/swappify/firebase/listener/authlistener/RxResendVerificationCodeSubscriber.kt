package swapp.items.com.swappify.firebase.listener.authlistener


import android.app.Activity
import com.google.firebase.FirebaseException
import com.google.firebase.FirebaseNetworkException
import com.google.firebase.auth.PhoneAuthCredential
import com.google.firebase.auth.PhoneAuthProvider
import io.reactivex.SingleEmitter
import io.reactivex.SingleOnSubscribe
import swapp.items.com.swappify.controller.signup.model.PhoneAuthDataModel
import swapp.items.com.swappify.controller.signup.viewmodel.LogInViewModel
import java.util.concurrent.TimeUnit

class RxResendVerificationCodeSubscriber(private val phoneNumber: String,
                                         private val activity: Activity,
                                         private val token: PhoneAuthProvider.ForceResendingToken) : SingleOnSubscribe<PhoneAuthDataModel> {

    override fun subscribe(emitter: SingleEmitter<PhoneAuthDataModel>) {
        PhoneAuthProvider.getInstance().verifyPhoneNumber(
                phoneNumber, // Phone number to verify
                60, // Timeout duration
                TimeUnit.SECONDS, // Unit of timeout
                activity, // Activity (for callback binding)
                PhoneAuthVerificationStateListener(emitter),
                token)
    }


    class PhoneAuthVerificationStateListener(private val emitter: SingleEmitter<PhoneAuthDataModel>) : PhoneAuthProvider.OnVerificationStateChangedCallbacks() {

        override fun onVerificationFailed(firebaseException: FirebaseException) {
            if (!emitter.isDisposed) {
                if (firebaseException is FirebaseNetworkException) {
                    emitter.onError(firebaseException)
                } else {
                    emitter.onSuccess(PhoneAuthDataModel.create {
                        firebaseException { firebaseException }
                        state { LogInViewModel.State.STATE_VERIFY_FAILED }
                    })
                }
            }
        }

        override fun onVerificationCompleted(phoneAuthCredential: PhoneAuthCredential) {
            if (!emitter.isDisposed) {
                emitter.onSuccess(PhoneAuthDataModel.create {
                    phoneAuthCredential { phoneAuthCredential }
                    state { LogInViewModel.State.STATE_VERIFY_SUCCESS }
                })
            }
        }

        override fun onCodeSent(verificationId: String, token: PhoneAuthProvider.ForceResendingToken) {
            if (!emitter.isDisposed) {
                emitter.onSuccess(PhoneAuthDataModel.create {
                    verificationId { verificationId }
                    token { token }
                    state { LogInViewModel.State.STATE_CODE_SENT }
                })
            }
        }
    }
}