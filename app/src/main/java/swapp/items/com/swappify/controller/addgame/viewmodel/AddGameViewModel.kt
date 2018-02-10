package swapp.items.com.swappify.controller.addgame.viewmodel

import android.arch.lifecycle.MutableLiveData
import android.databinding.ObservableBoolean
import android.databinding.ObservableField
import android.net.Uri
import com.google.firebase.FirebaseNetworkException
import swapp.items.com.swappify.R
import swapp.items.com.swappify.common.extension.switchMap
import swapp.items.com.swappify.controller.SwapApplication
import swapp.items.com.swappify.controller.addgame.model.GameModel
import swapp.items.com.swappify.controller.addgame.model.OptionsModel
import swapp.items.com.swappify.controller.addgame.model.SearchGameModel
import swapp.items.com.swappify.controller.base.BaseViewModel
import swapp.items.com.swappify.firebase.utils.Result
import swapp.items.com.swappify.injection.scopes.PerActivity
import swapp.items.com.swappify.mvvm.SingleLiveEvent
import java.io.IOException
import javax.inject.Inject

@PerActivity
class AddGameViewModel @Inject constructor(addGameDataManager: AddGameDataManager, application: SwapApplication) : BaseViewModel(application) {

    private val gameRepository = addGameDataManager.gameRepository
    private val preferenceHelper = addGameDataManager.appUtilManager.preferencesHelper

    var platForms = application.resources.getStringArray(R.array.platforms)

    var searchInputText = ObservableField<String>()
    var searchQueryLiveData = MutableLiveData<String>()

    var gameModelLiveData = SingleLiveEvent<SearchGameModel>()
    var searchGameModel = ObservableField<SearchGameModel>()
    var finishActivityLiveData = SingleLiveEvent<Boolean>()

    var errorGameName = ObservableBoolean()
    var errorGamePlatform = ObservableBoolean()
    var errorGameUrl = ObservableBoolean()
    var isLoading = ObservableBoolean()

    var gamesLiveData = searchQueryLiveData.switchMap {
        when {
            it.isNullOrEmpty() -> MutableLiveData<List<SearchGameModel>>()
            else -> gameRepository.getGamesSearchFor(it)
        }
    }

    fun getOptionalData(gameId: Int? = 0, developer: Int? = 0, publisher: Int? = 0) = gameRepository.getOptionalData(gameId, developer, publisher)
            .subscribe({ handleOnSuccess(it) }, { handleOnError(it) })

    private fun handleOnSuccess(pair: Pair<Result<List<OptionsModel>>, Result<List<OptionsModel>>>) {

        if (pair.first.isSuccess() && pair.first.value.isNotEmpty()) {
            searchGameModel.get().setGameGenre(pair.first.value[0].name)
        }

        if (pair.second.isSuccess() && pair.second.value.isNotEmpty()) {
            searchGameModel.get().setGameDeveloper(pair.second.value[0].name)
            if (pair.second.value.size > 1) {
                searchGameModel.get().setGamePlublisher(pair.second.value[1].name)
            }
        }
    }


    fun validateGame(): Boolean {
        var isValid = true
        if (searchGameModel.get().url.isNullOrEmpty()) {
            isValid = false
            errorGameUrl.set(true)

        } else {
            errorGameUrl.set(false)
        }

        if (searchGameModel.get().name.isNullOrEmpty()) {
            isValid = false
            errorGameName.set(true)
        } else {
            errorGameName.set(false)
        }

        if (searchGameModel.get().platform.isNullOrEmpty()) {
            isValid = false
            errorGamePlatform.set(true)
        } else {
            errorGamePlatform.set(false)
        }
        return isValid
    }

    fun disableErrorField() {
        errorGameName.set(searchGameModel.get().name.isNullOrEmpty())
        errorGameUrl.set(searchGameModel.get().url.isNullOrEmpty())
    }

    fun addGame() =
            gameRepository.addGame(searchGameModel.get().toGameModel(preferenceHelper))
                    .doOnSubscribe { isLoading.apply { set(true) } }
                    .doAfterTerminate { isLoading.apply { set(false) } }
                    .subscribe({ handleOnSuccess(it) }, { handleOnError(it) })


    fun addGameWithImage(userNumber: String?) = userNumber?.run {
        val searchGameModel = searchGameModel.get()
        gameRepository.addGameWithImage(Uri.parse(searchGameModel.url), searchGameModel.toGameModel(preferenceHelper), this)
                .doOnSubscribe { isLoading.apply { set(true) } }
                .doAfterTerminate { isLoading.apply { set(false) } }
                .subscribe({ handleOnSuccess(it) }, { handleOnError(it) })
    }


    private fun handleOnSuccess(result: Result<GameModel>?) = result?.also {
        if (it.isSuccess()) {
            finishActivityLiveData.value = true
        } else {
            handleOnError(it.error)
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

