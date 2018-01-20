package swapp.items.com.swappify.repo.game

import android.support.v4.util.ArrayMap
import io.reactivex.Flowable
import io.reactivex.Single
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.QueryMap
import swapp.items.com.swappify.controller.addgame.model.GameModel
import swapp.items.com.swappify.controller.addgame.model.OptionsModel


interface IGameApi {

    @GET(value = "/games/")
    fun searchGames(@QueryMap options: ArrayMap<String, String>): Flowable<List<GameModel>>

    @GET(value = "/genres/{ids}")
    fun getGenres(@Path(value = "ids") ids: String,
                  @QueryMap options: ArrayMap<String, String>): Single<List<OptionsModel>>

    @GET(value = "/companies/{ids}")
    fun getCompanies(@Path(value = "ids") ids: String,
                     @QueryMap options: ArrayMap<String, String>): Single<List<OptionsModel>>
}