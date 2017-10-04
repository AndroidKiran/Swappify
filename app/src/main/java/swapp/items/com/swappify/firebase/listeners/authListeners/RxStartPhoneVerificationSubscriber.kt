package swapp.items.com.swappify.firebase.listeners.authListeners

import android.app.Activity
import com.google.firebase.FirebaseException
import com.google.firebase.auth.PhoneAuthCredential
import com.google.firebase.auth.PhoneAuthProvider
import io.reactivex.SingleEmitter
import io.reactivex.SingleOnSubscribe
import swapp.items.com.swappify.controllers.signup.model.PhoneAuthDataModel
import swapp.items.com.swappify.controllers.signup.model.SignUpLogInViewModel.Companion.STATE_CODE_SENT
import swapp.items.com.swappify.controllers.signup.model.SignUpLogInViewModel.Companion.STATE_VERIFY_FAILED
import swapp.items.com.swappify.controllers.signup.model.SignUpLogInViewModel.Companion.STATE_VERIFY_SUCCESS


import java.util.concurrent.TimeUnit

class RxStartPhoneVerificationSubscriber(val phoneNumber: String, val activity: Activity) : SingleOnSubscribe<PhoneAuthDataModel> {

    override fun subscribe(emitter: SingleEmitter<PhoneAuthDataModel>) {
        PhoneAuthProvider.getInstance().verifyPhoneNumber(
                phoneNumber, // Phone number to verify
                60, // Timeout duration
                TimeUnit.SECONDS, // Unit of timeout
                activity, // Activity (for callback binding)
                PhoneAuthVerificationStateListener(emitter = emitter))
    }

    class PhoneAuthVerificationStateListener(val emitter: SingleEmitter<PhoneAuthDataModel>) : PhoneAuthProvider.OnVerificationStateChangedCallbacks() {

        override fun onVerificationFailed(firebaseException: FirebaseException) {
            if (!emitter.isDisposed) {
                emitter.onSuccess(
                        PhoneAuthDataModel.create {
                            firebaseException { firebaseException }
                            state { STATE_VERIFY_FAILED }
                        })
            }
        }

        override fun onVerificationCompleted(phoneAuthCredential: PhoneAuthCredential) {
            if (!emitter.isDisposed) {
                emitter.onSuccess(
                        PhoneAuthDataModel.create {
                            phoneAuthCredential { phoneAuthCredential }
                            state { STATE_VERIFY_SUCCESS }
                        })
            }
        }

        override fun onCodeSent(verificationId: String, token: PhoneAuthProvider.ForceResendingToken) {
            if (!emitter.isDisposed) {
                emitter.onSuccess(
                        PhoneAuthDataModel.create {
                            verificationId { verificationId }
                            token { token }
                            state { STATE_CODE_SENT }
                        })
            }
        }
    }
}