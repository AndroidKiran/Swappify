package swapp.items.com.swappify.repo.game

import android.net.Uri
import android.support.v4.util.ArrayMap
import io.reactivex.Flowable
import io.reactivex.Single
import swapp.items.com.swappify.controllers.addgame.model.GameModel
import swapp.items.com.swappify.controllers.addgame.model.OptionsModel
import swapp.items.com.swappify.controllers.addgame.model.PostGameModel
import swapp.items.com.swappify.firebase.listener.FirebaseAppStorage
import swapp.items.com.swappify.injection.scopes.PerActivity
import javax.inject.Inject

@PerActivity
class GameRepository @Inject constructor(private val gameApi: IGameApi,
                                         private val gameDataSource: GameDataSource,
                                         private val storage: FirebaseAppStorage) {

    companion object {
        private const val KEY_LIMIT = "limit"
        private const val KEY_SEARCH = "search"
        private const val KEY_FIELDS = "fields"
        private const val SEARCH_FIELDS = "name,summary,rating,cover,genres,first_release_date,developers,publishers"
        private const val OPTIONS_FIELDS = "id,name"
    }

    fun getGamesSearchFor(gameName: String): Flowable<List<GameModel>> {
        val data = ArrayMap<String, String>()
        data.put(KEY_SEARCH, gameName)
        data.put(KEY_LIMIT, "20")
        data.put(KEY_FIELDS, SEARCH_FIELDS)

        return gameApi.searchGames(options = data)
    }

    fun getGenresFor(gameId: Int?): Single<List<OptionsModel>> {
        val params = ArrayMap<String, String>()
        params.put(KEY_FIELDS, OPTIONS_FIELDS)
        return gameApi.getGenres(ids = "$gameId", options = params)
    }

    fun getCompaniesFor(ids: String?): Single<List<OptionsModel>> {
        val params = ArrayMap<String, String>()
        params.put(KEY_FIELDS, OPTIONS_FIELDS)
        return gameApi.getCompanies(ids = "$ids", options = params)
    }

    fun addGame(gameModel: PostGameModel?): Single<PostGameModel> =
            gameDataSource.addGame(gameModel)

    fun addGameWithImage(uri: Uri?, gameModel: PostGameModel?, userId: String?): Single<PostGameModel> =
            storage.uploadListener(uri, gameModel?.name!!, userId)
                    .flatMap { url: String ->
                        gameModel.url = url
                        addGame(gameModel)
                    }
}
