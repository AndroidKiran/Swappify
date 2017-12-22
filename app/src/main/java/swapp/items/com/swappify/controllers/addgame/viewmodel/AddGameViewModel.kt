package swapp.items.com.swappify.controllers.addgame.viewmodel

import android.arch.lifecycle.LiveData
import android.arch.lifecycle.MutableLiveData
import android.databinding.ObservableField
import android.text.TextUtils
import io.reactivex.Flowable
import io.reactivex.Single.zip
import io.reactivex.functions.BiFunction
import swapp.items.com.swappify.common.AbsentLiveData
import swapp.items.com.swappify.common.SingleLiveEvent
import swapp.items.com.swappify.common.extension.getObservableAsync
import swapp.items.com.swappify.common.extension.getSingleAsync
import swapp.items.com.swappify.common.extension.switchMap
import swapp.items.com.swappify.common.extension.toLiveData
import swapp.items.com.swappify.controllers.SwapApplication
import swapp.items.com.swappify.controllers.addgame.model.GameModel
import swapp.items.com.swappify.controllers.addgame.model.OptionsModel
import swapp.items.com.swappify.controllers.base.BaseViewModel
import swapp.items.com.swappify.injection.scopes.PerActivity
import swapp.items.com.swappify.repo.game.GameRepository
import swapp.items.com.swappify.rx.SchedulerProvider
import javax.inject.Inject

@PerActivity
class AddGameViewModel : BaseViewModel {

    var searchInputText = ObservableField<String>()
    var searchQueryLiveData = MutableLiveData<String>()
    var gamesLiveData: LiveData<List<GameModel>>
    var gameModelLiveData = SingleLiveEvent<GameModel>()
    var gameModel = ObservableField<GameModel>()

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
    }

    fun searchGamesFor(query: String?): LiveData<List<GameModel>> {

        if (TextUtils.getTrimmedLength(query) <= 0) {
            return MutableLiveData<List<GameModel>>()
        }

        return gameRepository.getGamesSearchFor(query!!)
                .getObservableAsync(schedulerProvider)
                .onErrorResumeNext(Flowable.empty())
                .toLiveData()
    }

    fun getOptionalData(gameId: Int? = 0, developer: Int? = 0, publisher: Int? = 0)
            = zip(genresFor(gameId), companiesFor(developer, publisher), asPair())
            .getSingleAsync(schedulerProvider)
            .subscribe({ handleOnSuccess(it) }, { handleOnError(it) })

    private fun genresFor(gameId: Int?)
            = gameRepository.getGenresFor(gameId)


    private fun companiesFor(developer: Int?, publisher: Int?)
            = gameRepository.getCompaniesFor(ids = "$developer,$publisher")


    private fun asPair(): BiFunction<List<OptionsModel>, List<OptionsModel>,
            Pair<List<OptionsModel>, List<OptionsModel>>>
            = BiFunction { genreList, companiesList -> Pair(genreList, companiesList) }

    private fun handleOnSuccess(pair: Pair<List<OptionsModel>, List<OptionsModel>>) {

        if (pair.first.isNotEmpty()) {
            gameModel.get().genre = pair.first[0].name
        }

        if (pair.second.isNotEmpty()) {
            gameModel.get().developer = pair.second[0].name
            if (pair.second.size > 1) {
                gameModel.get().publisher = pair.second[1].name
            }
        }

    }

    private fun handleOnError(throwable: Throwable) {
        throwable.message
    }
}

