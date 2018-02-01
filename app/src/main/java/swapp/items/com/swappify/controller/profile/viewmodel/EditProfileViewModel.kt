package swapp.items.com.swappify.controller.profile.viewmodel

import android.databinding.ObservableBoolean
import android.databinding.ObservableField
import android.net.Uri
import com.google.firebase.FirebaseNetworkException
import swapp.items.com.swappify.common.Constant
import swapp.items.com.swappify.common.Constant.Companion.IS_PROFILE_COMPLETE
import swapp.items.com.swappify.controller.SwapApplication
import swapp.items.com.swappify.controller.base.BaseViewModel
import swapp.items.com.swappify.firebase.utils.Result
import swapp.items.com.swappify.injection.scopes.PerActivity
import swapp.items.com.swappify.mvvm.SingleLiveEvent
import swapp.items.com.swappify.repo.user.LoginRepository
import swapp.items.com.swappify.repo.user.model.User
import java.io.IOException
import javax.inject.Inject

@PerActivity
class EditProfileViewModel @Inject constructor(private val loginRepository: LoginRepository, application: SwapApplication) : BaseViewModel(application) {

    var picUri = ObservableField<String>()

    var name = ObservableField<String>()

    var country = ObservableField<String>()

    var city = ObservableField<String>()

    var finishActivityLiveData = SingleLiveEvent<Boolean>()

    var isLoading = ObservableBoolean(false)

    fun getUser(): User {
        val preferenceUtils = loginRepository.appUtilManager.preferencesHelper
        val user = User(preferenceUtils.getStringPreference(Constant.USER_ID, ""),
                preferenceUtils.getStringPreference(Constant.USER_PHONE_NUM, "")
        )
        user.userName = name.get()
        user.userLocation = country.get()
       return user
    }

    fun updateUser(user: User) =
            loginRepository.runUserTransaction(user)
                    .doOnSubscribe { isLoading.set(true) }
                    .doAfterTerminate { isLoading.set(false) }
                    .subscribe({ handleOnSuccess(it) }, { handleOnError(it) })!!


    fun updateUser(user: User, uri: Uri) =
            loginRepository.updateUserWithImage(uri, user)
                    .doOnSubscribe { isLoading.set(true) }
                    .doAfterTerminate { isLoading.set(false) }
                    .subscribe({ handleOnSuccess(it) }, { handleOnError(it) })!!


    private fun handleOnSuccess(result: Result<User>?) {
        if (result!!.isSuccess()) {
            val preferenceHelper = loginRepository.appUtilManager.preferencesHelper
            preferenceHelper.set(IS_PROFILE_COMPLETE, true)
            finishActivityLiveData.value = true
        } else {
            handleOnError(result.error)
        }
    }

    private fun handleOnError(error: Throwable?) {
        when (error) {
            is FirebaseNetworkException ->
                isNetConnected.value = false

            is IOException ->
                isNetConnected.value = false

            else ->
                this.apiError.value = true

        }
    }
}
