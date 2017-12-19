package swapp.items.com.swappify.repo.user

import android.app.Activity
import com.google.firebase.auth.PhoneAuthCredential
import com.google.firebase.auth.PhoneAuthProvider
import io.reactivex.Observable
import io.reactivex.ObservableSource
import io.reactivex.Single
import swapp.items.com.swappify.controllers.signup.model.PhoneAuthDataModel
import swapp.items.com.swappify.controllers.signup.viewmodel.SignUpLogInViewModel.Companion.STATE_USER_WRITE_FAILED
import swapp.items.com.swappify.controllers.signup.viewmodel.SignUpLogInViewModel.Companion.STATE_USER_WRITE_SUCCESS
import swapp.items.com.swappify.firebase.utils.DatabaseResult
import swapp.items.com.swappify.injection.scopes.PerActivity
import swapp.items.com.swappify.repo.auth.datasource.AuthDataSource
import swapp.items.com.swappify.repo.user.dataSource.UserDataBase
import swapp.items.com.swappify.repo.user.model.User
import javax.inject.Inject

@PerActivity
class LoginRemoteService @Inject constructor(val authDataSource: AuthDataSource,
                                             val userDataBase: UserDataBase) {

    fun startPhoneVerification(phoneNumber: String, activity: Activity): Single<PhoneAuthDataModel> =
            authDataSource.startPhoneVerificationObservable(phoneNumber = phoneNumber, activity = activity)

    fun resendVerificationCode(phoneNumber: String, activity: Activity, token: PhoneAuthProvider.ForceResendingToken): Single<PhoneAuthDataModel> =
            authDataSource.resendVerificationCodeObservable(phoneNumber = phoneNumber, activity = activity, token = token)

    fun loginWithPhoneNumber(credential: PhoneAuthCredential?): Single<out PhoneAuthDataModel> =
            authDataSource.loginWithPhoneNumber(credential)


    fun saveUserToDB(phoneAuthDataModel: PhoneAuthDataModel?): Observable<out PhoneAuthDataModel> {
        return userDataBase.writeUser(phoneAuthDataModel?.user)
                .flatMap { databaseResult: DatabaseResult<User> ->
                    toPhoneAuthDataModel(databaseResult, phoneAuthDataModel)
                }
    }


    private fun toPhoneAuthDataModel(databaseResult: DatabaseResult<User>?, phoneAuthDataModel: PhoneAuthDataModel?): ObservableSource<out PhoneAuthDataModel> {
        phoneAuthDataModel?.state = if (databaseResult!!.isSuccess()) STATE_USER_WRITE_SUCCESS else STATE_USER_WRITE_FAILED
        val phoneAuthDataModelObservable: Observable<PhoneAuthDataModel> = Observable.just(phoneAuthDataModel)
        return phoneAuthDataModelObservable
    }
}

