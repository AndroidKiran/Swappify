package swapp.items.com.swappify.firebase.listener

import android.app.Activity
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.PhoneAuthProvider
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.storage.UploadTask
import io.reactivex.Observable
import io.reactivex.Single
import io.reactivex.functions.Function
import swapp.items.com.swappify.controllers.signup.model.PhoneAuthDataModel
import swapp.items.com.swappify.firebase.listener.authlistener.RxResendVerificationCodeSubscriber
import swapp.items.com.swappify.firebase.listener.authlistener.RxStartPhoneVerificationSubscriber
import swapp.items.com.swappify.firebase.listener.firebaselistener.AddValueOnSubscribe
import swapp.items.com.swappify.firebase.listener.firebaselistener.SetValueOnSubscribe
import swapp.items.com.swappify.firebase.listener.firebaselistener.StorageUploadEventOnSubscribe
import swapp.items.com.swappify.injection.scopes.PerActivity
import swapp.items.com.swappify.repo.user.model.User
import javax.inject.Inject

@PerActivity
class FirebaseObservableListener @Inject constructor() {

    fun startPhoneVerificationListener(phoneNumber: String, activity: Activity): Single<PhoneAuthDataModel> =
            Single.create(RxStartPhoneVerificationSubscriber(phoneNumber = phoneNumber, activity = activity))

    fun resendVerificationCodeListener(phoneNumber: String, activity: Activity, token: PhoneAuthProvider.ForceResendingToken): Single<PhoneAuthDataModel> =
            Single.create(RxResendVerificationCodeSubscriber(phoneNumber = phoneNumber, activity = activity, token = token))

    fun setValue(task: Task<Void>?, returnValue: User?): Observable<User> =
            Observable.create(SetValueOnSubscribe(task, returnValue))

    fun <T> storageUploadEvent(uploadTask: UploadTask, marshaller: Function<UploadTask.TaskSnapshot, T>): Single<T> =
            Single.create(StorageUploadEventOnSubscribe(uploadTask, marshaller))

    fun <T> addValueOnSubscribe(task: Task<DocumentReference>, marshaller: Function<DocumentReference, T>): Single<T> =
            Single.create(AddValueOnSubscribe(task, marshaller))
}

