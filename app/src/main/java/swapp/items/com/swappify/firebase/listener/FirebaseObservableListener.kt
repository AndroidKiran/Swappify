package swapp.items.com.swappify.firebase.listener

import android.app.Activity
import com.google.firebase.auth.PhoneAuthProvider
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.storage.UploadTask
import io.reactivex.Single
import io.reactivex.functions.Function
import swapp.items.com.swappify.controller.signup.model.PhoneAuthDataModel
import swapp.items.com.swappify.firebase.listener.authlistener.RxResendVerificationCodeSubscriber
import swapp.items.com.swappify.firebase.listener.authlistener.RxStartPhoneVerificationSubscriber
import swapp.items.com.swappify.firebase.listener.firebaselistener.SetValueOnSubscribe
import swapp.items.com.swappify.firebase.listener.firebaselistener.StorageUploadEventOnSubscribe
import swapp.items.com.swappify.injection.scopes.PerActivity
import javax.inject.Inject

@PerActivity
class FirebaseObservableListener @Inject constructor() {

    fun startPhoneVerificationListener(phoneNumber: String, activity: Activity): Single<PhoneAuthDataModel> =
            Single.create(RxStartPhoneVerificationSubscriber(phoneNumber = phoneNumber, activity = activity))

    fun resendVerificationCodeListener(phoneNumber: String, activity: Activity, token: PhoneAuthProvider.ForceResendingToken): Single<PhoneAuthDataModel> =
            Single.create(RxResendVerificationCodeSubscriber(phoneNumber = phoneNumber, activity = activity, token = token))

    fun <U> setValue(documentReference: DocumentReference, value: Any?, returnValue: U?): Single<U> =
            Single.create(SetValueOnSubscribe(documentReference, value, returnValue))

    fun <T> storageUploadEvent(uploadTask: UploadTask, marshaller: Function<UploadTask.TaskSnapshot, T>): Single<T> =
            Single.create(StorageUploadEventOnSubscribe(uploadTask, marshaller))
}

