package swapp.items.com.swappify.repo.game

import android.arch.lifecycle.LiveData
import android.net.Uri
import android.support.v4.util.ArrayMap
import io.reactivex.Flowable
import io.reactivex.Single
import io.reactivex.functions.BiFunction
import swapp.items.com.swappify.common.extension.getFlowableAsync
import swapp.items.com.swappify.common.extension.getSingleAsync
import swapp.items.com.swappify.common.extension.retrofitResponseToResult
import swapp.items.com.swappify.common.extension.toLiveData
import swapp.items.com.swappify.controller.addgame.model.GameModel
import swapp.items.com.swappify.controller.addgame.model.OptionsModel
import swapp.items.com.swappify.controller.addgame.model.SearchGameModel
import swapp.items.com.swappify.firebase.listener.FirebaseAppStorage
import swapp.items.com.swappify.firebase.utils.Result
import swapp.items.com.swappify.injection.scopes.PerActivity
import swapp.items.com.swappify.repo.AppUtilManager
import swapp.items.com.swappify.repo.game.datasource.GameDataBase
import swapp.items.com.swappify.repo.user.dataSource.UserDataBase
import java.util.*
import javax.inject.Inject

@PerActivity
class GameRepository @Inject constructor(private val gameApi: IGameApi,
                                         private val gameDataBase: GameDataBase,
                                         private val userDataBase: UserDataBase,
                                         private val storage: FirebaseAppStorage,
                                         val appUtilManager: AppUtilManager) {

    private val schedulerProvider = appUtilManager.schedulerProvider

    fun getGamesSearchFor(gameName: String?): LiveData<List<SearchGameModel>> {
        val data = ArrayMap<String, String>().apply {
            put(KEY_SEARCH, gameName)
            put(KEY_LIMIT, "20")
            put(KEY_FIELDS, SEARCH_FIELDS)
        }

        return gameApi.searchGames(options = data)
                .getFlowableAsync(schedulerProvider)
                .onErrorResumeNext(Flowable.empty())
                .toLiveData()
    }

    fun getGenresFor(gameId: Int?): Single<Result<List<OptionsModel>>> {
        val params = ArrayMap<String, String>().apply {
            put(KEY_FIELDS, OPTIONS_FIELDS)
        }
        return gameApi.getGenres(ids = "$gameId", options = params)
                .retrofitResponseToResult()
                .getSingleAsync(schedulerProvider)
    }

    fun getCompaniesFor(ids: String?): Single<Result<List<OptionsModel>>> {
        val params = ArrayMap<String, String>().apply {
            put(KEY_FIELDS, OPTIONS_FIELDS)
        }
        return gameApi.getCompanies(ids = "$ids", options = params)
                .retrofitResponseToResult()
                .getSingleAsync(schedulerProvider)

    }

    fun getOptionalData(gameId: Int? = 0, developer: Int? = 0, publisher: Int? = 0) = Single.zip(getGenresFor(gameId), getCompaniesFor(ids = "$developer,$publisher"), asPair())
            .getSingleAsync(schedulerProvider)

    private fun asPair(): BiFunction<Result<List<OptionsModel>>, Result<List<OptionsModel>>,
            Pair<Result<List<OptionsModel>>, Result<List<OptionsModel>>>> = BiFunction { genreList, companiesList -> Pair(genreList, companiesList) }

    fun addGame(gameModel: GameModel?): Single<Result<GameModel>> = gameDataBase.setGame(gameModel)
            .getSingleAsync(schedulerProvider)

    fun addGameWithImage(uri: Uri?, gameModel: GameModel?, userId: String?): Single<Result<GameModel>> = storage.uploadListener(uri, gameModel?.name!!, userId)
            .flatMap { url: String ->
                gameModel.url = url
                addGame(gameModel)
            }.getSingleAsync(schedulerProvider)


   /* fun getNearByGames(geoPoint: GeoPoint?): Single<Result<List<User>>> {

        val locationHelper = LocationHelper()

        val lesserGeoPoint = geoPoint?.apply {
            locationHelper.boundingBoxCoordinates(this, 1000, SOUTH_WEST)
        }

        val greaterGeoPoint = geoPoint?.apply {
            locationHelper.boundingBoxCoordinates(this, 1000, NORTH_EAST)
        }

        *//*  let lat = 0.0144927536231884
          let lon = 0.0181818181818182

          let lowerLat = latitude - (lat * distance)
          let lowerLon = longitude - (lon * distance)

          let greaterLat = latitude + (lat * distance)
          let greaterLon = longitude + (lon * distance)

          let lesserGeopoint = GeoPoint(latitude: lowerLat, longitude: lowerLon)
          let greaterGeopoint = GeoPoint(latitude: greaterLat, longitude: greaterLon)
  *//*
        *//*return if (lesserGeoPoint != null && greaterGeoPoint != null) {
            userDataBase.getNearByGamers(lesserGeoPoint, greaterGeoPoint)
        } else {
            Flowable.create { mutableListOf<User>() }
        }.getFlowableAsync(schedulerProvider)
                .retrofitResponseToResult()*//*
    }*/

    fun getGames(userNumber: String?, gameName: String?, genre: String?, createdAt: Date?) =
            gameDataBase.getGames(userNumber, gameName, genre, createdAt)
                    .getFlowableAsync(schedulerProvider)
                    .onErrorResumeNext(Flowable.empty())
                    .toLiveData()

    companion object {
        private const val KEY_LIMIT = "limit"
        private const val KEY_SEARCH = "search"
        private const val KEY_FIELDS = "fields"
        private const val SEARCH_FIELDS = "name,summary,rating,cover,genres,first_release_date,developers,publishers"
        private const val OPTIONS_FIELDS = "id,name"
    }
}
