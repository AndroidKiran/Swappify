package swapp.items.com.swappify.controllers.addgame.viewmodel

import android.arch.lifecycle.LiveData
import android.arch.lifecycle.MutableLiveData
import android.databinding.ObservableBoolean
import android.databinding.ObservableField
import android.net.Uri
import android.text.TextUtils
import com.google.firebase.FirebaseNetworkException
import swapp.items.com.swappify.R
import swapp.items.com.swappify.common.AbsentLiveData
import swapp.items.com.swappify.common.SingleLiveEvent
import swapp.items.com.swappify.common.extension.switchMap
import swapp.items.com.swappify.controllers.SwapApplication
import swapp.items.com.swappify.controllers.addgame.model.GameModel
import swapp.items.com.swappify.controllers.addgame.model.OptionsModel
import swapp.items.com.swappify.controllers.addgame.model.PostGameModel
import swapp.items.com.swappify.controllers.base.BaseViewModel
import swapp.items.com.swappify.firebase.utils.Result
import swapp.items.com.swappify.repo.game.GameRepository
import java.io.IOException
import javax.inject.Inject

class AddGameViewModel : BaseViewModel {

    var platFormsList: Array<String>? = null

    var searchInputText = ObservableField<String>()
    var searchQueryLiveData = MutableLiveData<String>()
    var gamesLiveData: LiveData<List<GameModel>>
    var gameModelLiveData = SingleLiveEvent<GameModel>()
    var gameModel = ObservableField<GameModel>(GameModel())
    var finishActivityLiveData = SingleLiveEvent<Boolean>()

    var errorGameName = ObservableBoolean(false)
    var errorGamePlatform = ObservableBoolean(false)
    var errorGameUrl = ObservableBoolean(false)
    var isLoading = ObservableBoolean(false)

    private var gameRepository: GameRepository

    @Inject
    constructor(addGameDataManager: AddGameDataManager, application: SwapApplication) : super(application) {
        this.gameRepository = addGameDataManager.gameRepository

        gamesLiveData = searchQueryLiveData.switchMap {
            when (it) {
                null -> AbsentLiveData.create()
                else -> searchGamesFor(it)
            }
        }
        platFormsList = application.resources.getStringArray(R.array.platforms)
    }

    private fun searchGamesFor(query: String?): LiveData<List<GameModel>> {

        if (TextUtils.getTrimmedLength(query) <= 0) {
            return MutableLiveData<List<GameModel>>()
        }

        return gameRepository.getGamesSearchFor(query)
    }

    fun getOptionalData(gameId: Int? = 0, developer: Int? = 0, publisher: Int? = 0)
            = gameRepository.getOptionalData(gameId, developer, publisher)
            .subscribe({ handleOnSuccess(it) }, { handleOnError(it) })

    private fun handleOnSuccess(pair: Pair<Result<List<OptionsModel>>, Result<List<OptionsModel>>>) {

        if (pair.first.isSuccess() && pair.first.value.isNotEmpty()) {
            gameModel.get().setGameGenre(pair.first.value[0].name)
        }

        if (pair.second.isSuccess() && pair.second.value.isNotEmpty()) {
            gameModel.get().setGameDeveloper(pair.second.value[0].name)
            if (pair.second.value.size > 1) {
                gameModel.get().setGamePlublisher(pair.second.value[1].name)
            }
        }
    }


    fun validateGame(): Boolean {
        var isValid = true
        if (gameModel.get().url.isNullOrEmpty()) {
            isValid = false
            errorGameUrl.set(true)

        } else {
            errorGameUrl.set(false)
        }

        if (gameModel.get().name.isNullOrEmpty()) {
            isValid = false
            errorGameName.set(true)
        } else {
            errorGameName.set(false)
        }

        if (gameModel.get().platform.isNullOrEmpty()) {
            isValid = false
            errorGamePlatform.set(true)
        } else {
            errorGamePlatform.set(false)
        }
        return isValid
    }

    fun disableErrorField() {
        errorGameName.set(gameModel.get().name.isNullOrEmpty())
        errorGameUrl.set(gameModel.get().url.isNullOrEmpty())
    }

    fun addGame()
            = gameRepository.addGame(postGameModel(gameModel.get()))
            .doOnSubscribe { isLoading.set(true) }
            .doAfterTerminate { isLoading.set(false) }
            .subscribe({ handleOnSuccess(it) }, { handleOnError(it) })


    fun addGameWithImage(userId: String)
            = gameRepository.addGameWithImage(Uri.parse(gameModel.get().url), postGameModel(gameModel.get()), userId)
            .doOnSubscribe { isLoading.set(true) }
            .doAfterTerminate { isLoading.set(false) }
            .subscribe({ handleOnSuccess(it) }, { handleOnError(it) })

    private fun handleOnSuccess(result: Result<PostGameModel>) {
        if (result.isSuccess()) {
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

    private fun postGameModel(gameModel: GameModel): PostGameModel
            = PostGameModel(gameModel.id, gameModel.name, gameModel.url, gameModel.platform, gameModel.developer,
            gameModel.genre, gameModel.publisher, gameModel.releaseDate, gameModel.summary)

}

