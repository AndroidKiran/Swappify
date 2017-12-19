package swapp.items.com.swappify.controllers.addgame.viewmodel

import android.arch.lifecycle.LiveData
import android.arch.lifecycle.MutableLiveData
import android.databinding.ObservableField
import android.text.TextUtils
import io.reactivex.Flowable
import swapp.items.com.swappify.common.AbsentLiveData
import swapp.items.com.swappify.common.extension.getObservableAsync
import swapp.items.com.swappify.common.extension.switchMap
import swapp.items.com.swappify.common.extension.toLiveData
import swapp.items.com.swappify.controllers.SwapApplication
import swapp.items.com.swappify.controllers.addgame.model.GameModel
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

        return gameRepository.getGamesOnSearch(query!!)
                .getObservableAsync(schedulerProvider)
                .onErrorResumeNext(Flowable.empty())
                .toLiveData()
    }

}

