package swapp.items.com.swappify.repo.game

import android.arch.lifecycle.LiveData
import android.net.Uri
import android.support.v4.util.ArrayMap
import io.reactivex.Flowable
import io.reactivex.Single
import io.reactivex.functions.BiFunction
import swapp.items.com.swappify.common.extension.getObservableAsync
import swapp.items.com.swappify.common.extension.getSingleAsync
import swapp.items.com.swappify.common.extension.retrofitResponseToResult
import swapp.items.com.swappify.common.extension.toLiveData
import swapp.items.com.swappify.controller.addgame.model.GameModel
import swapp.items.com.swappify.controller.addgame.model.OptionsModel
import swapp.items.com.swappify.controller.addgame.model.PostGameModel
import swapp.items.com.swappify.firebase.listener.FirebaseAppStorage
import swapp.items.com.swappify.firebase.utils.Result
import swapp.items.com.swappify.injection.scopes.PerActivity
import swapp.items.com.swappify.repo.AppUtilManager
import javax.inject.Inject

@PerActivity
class GameRepository @Inject constructor(private val gameApi: IGameApi,
                                         private val gameDataSource: GameDataSource,
                                         private val storage: FirebaseAppStorage,
                                         val appUtilManager: AppUtilManager) {

    private val schedulerProvider = appUtilManager.schedulerProvider

    fun getGamesSearchFor(gameName: String?): LiveData<List<GameModel>> {
        val data = ArrayMap<String, String>()
        data.put(KEY_SEARCH, gameName)
        data.put(KEY_LIMIT, "20")
        data.put(KEY_FIELDS, SEARCH_FIELDS)

        return gameApi.searchGames(options = data)
                .getObservableAsync(schedulerProvider)
                .onErrorResumeNext(Flowable.empty())
                .toLiveData()
    }

    fun getGenresFor(gameId: Int?): Single<Result<List<OptionsModel>>> {
        val params = ArrayMap<String, String>()
        params.put(KEY_FIELDS, OPTIONS_FIELDS)
        return gameApi.getGenres(ids = "$gameId", options = params)
                .retrofitResponseToResult()
                .getSingleAsync(schedulerProvider)
    }

    fun getCompaniesFor(ids: String?): Single<Result<List<OptionsModel>>> {
        val params = ArrayMap<String, String>()
        params.put(KEY_FIELDS, OPTIONS_FIELDS)
        return gameApi.getCompanies(ids = "$ids", options = params)
                .retrofitResponseToResult()
                .getSingleAsync(schedulerProvider)

    }

    fun getOptionalData(gameId: Int? = 0, developer: Int? = 0, publisher: Int? = 0)
            = Single.zip(getGenresFor(gameId), getCompaniesFor(ids = "$developer,$publisher"), asPair())
            .getSingleAsync(schedulerProvider)

    private fun asPair(): BiFunction<Result<List<OptionsModel>>, Result<List<OptionsModel>>,
            Pair<Result<List<OptionsModel>>, Result<List<OptionsModel>>>>
            = BiFunction { genreList, companiesList -> Pair(genreList, companiesList) }

    fun addGame(gameModel: PostGameModel?): Single<Result<PostGameModel>>
            = gameDataSource.addGame(gameModel)
            .getSingleAsync(schedulerProvider)

    fun addGameWithImage(uri: Uri?, gameModel: PostGameModel?, userId: String?): Single<Result<PostGameModel>>
            = storage.uploadListener(uri, gameModel?.name!!, userId)
            .flatMap { url: String ->
                gameModel.url = url
                addGame(gameModel)
            }

    companion object {
        private const val KEY_LIMIT = "limit"
        private const val KEY_SEARCH = "search"
        private const val KEY_FIELDS = "fields"
        private const val SEARCH_FIELDS = "name,summary,rating,cover,genres,first_release_date,developers,publishers"
        private const val OPTIONS_FIELDS = "id,name"
    }
}
