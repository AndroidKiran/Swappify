package swapp.items.com.swappify.repo.user

import android.app.Activity
import android.net.Uri
import com.google.firebase.auth.PhoneAuthCredential
import com.google.firebase.auth.PhoneAuthProvider
import io.reactivex.Observable
import swapp.items.com.swappify.common.extension.getObservableAsync
import swapp.items.com.swappify.common.extension.getSingleAsync
import swapp.items.com.swappify.firebase.listener.FirebaseAppStorage
import swapp.items.com.swappify.injection.scopes.PerActivity
import swapp.items.com.swappify.repo.AppUtilManager
import swapp.items.com.swappify.repo.auth.datasource.AuthDataSource
import swapp.items.com.swappify.repo.user.dataSource.UserDataBase
import swapp.items.com.swappify.repo.user.model.User
import java.util.concurrent.TimeUnit
import javax.inject.Inject

@PerActivity
class LoginRepository @Inject constructor(private val authDataSource: AuthDataSource,
                                          private val storage: FirebaseAppStorage,
                                          private val userDataBase: UserDataBase,
                                          val appUtilManager: AppUtilManager) {

    private val schedulerProvider = appUtilManager.schedulerProvider

    fun startPhoneVerification(phoneNumber: String, activity: Activity) =
            authDataSource.startPhoneVerificationObservable(phoneNumber, activity).getSingleAsync(schedulerProvider)

    fun resendVerificationCode(phoneNumber: String, activity: Activity, token: PhoneAuthProvider.ForceResendingToken) =
            authDataSource.resendVerificationCodeObservable(phoneNumber, activity, token).getSingleAsync(schedulerProvider)

    fun signInWith(credential: PhoneAuthCredential?) = authDataSource.signInWith(credential).getSingleAsync(schedulerProvider)

    fun saveOrUpdateUser(user: User?) = userDataBase.updateUser(user).getSingleAsync(schedulerProvider)

    fun saveOrUpdateUserWithImage(uri: Uri, user: User?) = storage.uploadListener(uri, user?.userNumber, user?.userNumber)
            .flatMap { url ->
                user?.apply { userPic = url }
                saveOrUpdateUser(user)
            }.getSingleAsync(schedulerProvider)

    fun initAutoVerify(time: Int) = Observable.interval(1, TimeUnit.SECONDS)
            .getObservableAsync(schedulerProvider)
            .map({ time - it.toInt() })
            .take(time + 1.toLong())

}

