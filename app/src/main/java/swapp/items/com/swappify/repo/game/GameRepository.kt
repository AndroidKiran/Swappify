package swapp.items.com.swappify.repo.game

import io.reactivex.Flowable
import swapp.items.com.swappify.controllers.addgame.model.GameModel
import swapp.items.com.swappify.injection.scopes.PerActivity
import javax.inject.Inject

@PerActivity
class GameRepository @Inject constructor(private val gameApi: IGameApi) {

    private val KEY_LIMIT = "limit"
    private val KEY_SEARCH = "search"
    private val KEY_FIELDS = "fields"
    private val VALUE_FIELDS = "name,summary,rating,cover,genres,first_release_date"

    fun getGamesOnSearch(gameName: String): Flowable<List<GameModel>> {
        val data = mutableMapOf<String, String>()
        data.put(KEY_SEARCH, gameName)
        data.put(KEY_LIMIT, "30")
        data.put(KEY_FIELDS, VALUE_FIELDS)

        return gameApi.searchGames(data)
    }

}
