package swapp.items.com.swappify.firebase.listener

import android.app.Activity
import com.google.firebase.auth.PhoneAuthProvider
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.storage.UploadTask
import io.reactivex.Single
import io.reactivex.functions.Function
import swapp.items.com.swappify.controller.signup.model.PhoneAuthDataModel
import swapp.items.com.swappify.firebase.listener.authlistener.RxResendVerificationCodeSubscriber
import swapp.items.com.swappify.firebase.listener.authlistener.RxStartPhoneVerificationSubscriber
import swapp.items.com.swappify.firebase.listener.firebaselistener.GetValueOnSubscribe
import swapp.items.com.swappify.firebase.listener.firebaselistener.RunUserTransactionOnSubscribe
import swapp.items.com.swappify.firebase.listener.firebaselistener.SetValueOnSubscribe
import swapp.items.com.swappify.firebase.listener.firebaselistener.StorageUploadEventOnSubscribe
import swapp.items.com.swappify.injection.scopes.PerActivity
import swapp.items.com.swappify.repo.user.model.User
import javax.inject.Inject

@PerActivity
class FirebaseObservableListener @Inject constructor() {

    fun startPhoneVerificationListener(phoneNumber: String, activity: Activity): Single<PhoneAuthDataModel> =
            Single.create(RxStartPhoneVerificationSubscriber(phoneNumber, activity))

    fun resendVerificationCodeListener(phoneNumber: String, activity: Activity, token: PhoneAuthProvider.ForceResendingToken): Single<PhoneAuthDataModel> =
            Single.create(RxResendVerificationCodeSubscriber(phoneNumber, activity, token))

    fun <U> setValue(documentReference: DocumentReference, value: Any?, returnValue: U?): Single<U> =
            Single.create(SetValueOnSubscribe(documentReference, value, returnValue))

    fun <T> storageUploadEvent(uploadTask: UploadTask, marshaller: Function<UploadTask.TaskSnapshot, T>): Single<T> =
            Single.create(StorageUploadEventOnSubscribe(uploadTask, marshaller))

    fun <T> getValue(documentReference: DocumentReference, marshaller: Function<DocumentSnapshot, T>): Single<T> =
            Single.create(GetValueOnSubscribe(documentReference, marshaller))

    fun runUserTransaction(documentReference: DocumentReference, value: User) : Single<User> =
            Single.create(RunUserTransactionOnSubscribe(documentReference, value))
}

