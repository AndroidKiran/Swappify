package swapp.items.com.swappify.controllers.addgame.viewmodel

import android.arch.lifecycle.LiveData
import android.arch.lifecycle.MutableLiveData
import android.databinding.ObservableField
import android.net.Uri
import android.text.TextUtils
import io.reactivex.Flowable
import io.reactivex.Single.zip
import io.reactivex.functions.BiFunction
import swapp.items.com.swappify.R
import swapp.items.com.swappify.common.AbsentLiveData
import swapp.items.com.swappify.common.SingleLiveEvent
import swapp.items.com.swappify.common.extension.getObservableAsync
import swapp.items.com.swappify.common.extension.getSingleAsync
import swapp.items.com.swappify.common.extension.switchMap
import swapp.items.com.swappify.common.extension.toLiveData
import swapp.items.com.swappify.controllers.SwapApplication
import swapp.items.com.swappify.controllers.addgame.model.GameModel
import swapp.items.com.swappify.controllers.addgame.model.OptionsModel
import swapp.items.com.swappify.controllers.addgame.model.PostGameModel
import swapp.items.com.swappify.controllers.base.BaseViewModel
import swapp.items.com.swappify.injection.scopes.PerActivity
import swapp.items.com.swappify.repo.game.GameRepository
import swapp.items.com.swappify.rx.SchedulerProvider
import javax.inject.Inject

@PerActivity
class AddGameViewModel : BaseViewModel {

    var platFormsList: Array<String>? = null

    var searchInputText = ObservableField<String>()
    var searchQueryLiveData = MutableLiveData<String>()
    var gamesLiveData: LiveData<List<GameModel>>
    var gameModelLiveData = SingleLiveEvent<GameModel>()
    var gameModel = ObservableField<GameModel>(GameModel())
    var finishActivityLiveData = SingleLiveEvent<Boolean>()

    var errorGameName = ObservableField<Boolean>(false)
    var errorGamePlatform = ObservableField<Boolean>(false)
    var errorGameUrl = ObservableField<Boolean>(false)
    var enableLoading = ObservableField<Boolean>(false)

    private var schedulerProvider: SchedulerProvider
    private var addGameDataManager: AddGameDataManager
    private var gameRepository: GameRepository

    @Inject
    constructor(addGameDataManager: AddGameDataManager, application: SwapApplication) : super(application) {
        this.schedulerProvider = addGameDataManager.appUtilManager.schedulerProvider
        this.addGameDataManager = addGameDataManager
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

        return gameRepository.getGamesSearchFor(query!!)
                .getObservableAsync(schedulerProvider)
                .onErrorResumeNext(Flowable.empty())
                .toLiveData()
    }

    fun getOptionalData(gameId: Int? = 0, developer: Int? = 0, publisher: Int? = 0) =
            zip(gameRepository.getGenresFor(gameId),
                    gameRepository.getCompaniesFor(ids = "$developer,$publisher"),
                    asPair())
                    .getSingleAsync(schedulerProvider)
                    .subscribe({ handleOnSuccess(it) }, { handleOnError(it) })

    private fun asPair(): BiFunction<List<OptionsModel>, List<OptionsModel>,
            Pair<List<OptionsModel>, List<OptionsModel>>>
            = BiFunction { genreList, companiesList -> Pair(genreList, companiesList) }

    private fun handleOnSuccess(pair: Pair<List<OptionsModel>, List<OptionsModel>>) {
        if (pair.first.isNotEmpty()) {
            gameModel.get().setGameGenre(pair.first[0].name)
        }

        if (pair.second.isNotEmpty()) {
            gameModel.get().setGameDeveloper(pair.second[0].name)
            if (pair.second.size > 1) {
                gameModel.get().setGamePlublisher(pair.second[1].name)
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
            .getSingleAsync(schedulerProvider)
            .subscribe({ handleOnSuccess(it) }, { handleOnError(it) })



    fun addGameWithImage(userId: String) =
            gameRepository.addGameWithImage(Uri.parse(gameModel.get().url), postGameModel(gameModel.get()), userId)
                    .getSingleAsync(schedulerProvider)
                    .subscribe({ handleOnSuccess(it) }, { handleOnError(it) })

    private fun <T> handleOnSuccess(t: T) {
        if (t is PostGameModel) {
            enableLoading.set(false)
            finishActivityLiveData.value = true
        }
    }

    private fun handleOnError(throwable: Throwable) {
        enableLoading.set(false)
        throwable.message
    }

    private fun postGameModel(gameModel: GameModel): PostGameModel
            = PostGameModel(gameModel.id, gameModel.name, gameModel.url, gameModel.platform, gameModel.developer,
            gameModel.genre, gameModel.publisher, gameModel.releaseDate, gameModel.summary)

}

