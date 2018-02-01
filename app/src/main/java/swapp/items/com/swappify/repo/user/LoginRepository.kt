package swapp.items.com.swappify.repo.user

import android.app.Activity
import android.net.Uri
import com.google.firebase.auth.PhoneAuthCredential
import com.google.firebase.auth.PhoneAuthProvider
import io.reactivex.Observable
import io.reactivex.Single
import swapp.items.com.swappify.common.extension.firebaseResponseToResult
import swapp.items.com.swappify.common.extension.getObservableAsync
import swapp.items.com.swappify.common.extension.getSingleAsync
import swapp.items.com.swappify.controller.signup.model.PhoneAuthDataModel
import swapp.items.com.swappify.firebase.listener.FirebaseAppStorage
import swapp.items.com.swappify.firebase.utils.Result
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

    fun startPhoneVerification(phoneNumber: String, activity: Activity) = authDataSource.startPhoneVerificationObservable(phoneNumber, activity)
            .firebaseResponseToResult()
            .getSingleAsync(schedulerProvider)

    fun resendVerificationCode(phoneNumber: String, activity: Activity, token: PhoneAuthProvider.ForceResendingToken) = authDataSource.resendVerificationCodeObservable(phoneNumber, activity, token)
            .firebaseResponseToResult()
            .getSingleAsync(schedulerProvider)

    fun signInWith(credential: PhoneAuthCredential?): Single<Result<PhoneAuthDataModel>> = authDataSource.signInWith(credential)
            .firebaseResponseToResult()
            .getSingleAsync(schedulerProvider)

    fun getUser(mobile: String) = userDataBase.fetch(mobile).getSingleAsync(schedulerProvider)

    fun saveUser(user: User) = userDataBase.write(user).getSingleAsync(schedulerProvider)

    fun runUserTransaction(user: User) = userDataBase.runUserTransaction(user).getSingleAsync(schedulerProvider)

    fun updateUserWithImage(uri: Uri, user: User): Single<Result<User>> = storage.uploadListener(uri, user.userName, user.userNumber)
            .flatMap { url ->
                user.userPic = url
                runUserTransaction(user)
            }


    fun initAutoVerify(time: Int) = Observable.interval(1, TimeUnit.SECONDS)
            .getObservableAsync(schedulerProvider)
            .map({ time - it.toInt() })
            .take(time + 1.toLong())

}

