package swapp.items.com.swappify.controller.profile.viewmodel

import android.databinding.ObservableBoolean
import android.databinding.ObservableField
import android.net.Uri
import com.google.firebase.FirebaseNetworkException
import com.google.firebase.firestore.GeoPoint
import swapp.items.com.swappify.common.Constant
import swapp.items.com.swappify.common.Constant.Companion.IS_PROFILE_COMPLETE
import swapp.items.com.swappify.controller.SwapApplication
import swapp.items.com.swappify.controller.base.BaseViewModel
import swapp.items.com.swappify.controller.profile.model.Place
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

    var finishActivityLiveData = SingleLiveEvent<Boolean>()

    var isLoading = ObservableBoolean(false)

    var locationUri = ObservableField<String>()

    var place: ObservableField<Place>? = ObservableField()

    var errorLocation = ObservableBoolean()

    var errorName = ObservableBoolean()

    private val preferenceHelper = loginRepository.appUtilManager.preferencesHelper

    init {
        locationUri.set("https://maps.googleapis.com/maps/api/staticmap?center=Albany,+NY&zoom=17&scale=1&size=600x300&maptype=roadmap&format=jpg&visual_refresh=true&markers=size:mid%7Ccolor:0xff0000%7Clabel:%7CAlbany,+NY")
    }

    fun validate(): Boolean {
        var isValid = true

        if (name.get().isNullOrEmpty()) {
            isValid = false
            errorName.set(true)
        } else {
            errorName.set(false)
        }

        if (place == null) {
            isValid = false
            errorLocation.set(true)
        } else {
            errorLocation.set(false)
        }

        return isValid
    }

    fun setLocationUri(place: Place?) = place?.also {
        this.place?.set(it)
        this.locationUri.set("https://maps.googleapis.com/maps/api/staticmap?center=${it.latitude},${it.longitude}&zoom=12&size=280x280&markers=color:red|${it.latitude},${it.longitude}")
    }

    private fun getUser() = User("", preferenceHelper.getStringPreference(Constant.USER_PHONE_NUM, ""),
            name.get(), picUri.get(), place?.get()?.let { GeoPoint(it.latitude!!, it.longitude!!) })

    private fun getObservable() = getUser().let {
        if (it.userPic.isNullOrEmpty()) {
            loginRepository.saveOrUpdateUser(it)
        } else {
            loginRepository.saveOrUpdateUserWithImage(Uri.parse(it.userPic), it)
        }
    }

    fun verifyAndUpdate() =
            getObservable()
                    .doOnSubscribe { isLoading.apply { set(true) } }
                    .doAfterTerminate { isLoading.apply { set(false) } }
                    .subscribe({ handleOnSuccess(it) }, { handleOnError(it) })

    private fun handleOnSuccess(result: Result<User>?) = result?.run {
        if (this.isSuccess()) {
            finishActivityLiveData.apply { value = true }
        } else {
            handleOnError(this.error)
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

    public fun markProfileComplete() {
        preferenceHelper.set(IS_PROFILE_COMPLETE, true)
    }

}
