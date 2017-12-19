package swapp.items.com.swappify.repo.game

import io.reactivex.Flowable
import retrofit2.http.GET
import retrofit2.http.QueryMap
import swapp.items.com.swappify.controllers.addgame.model.GameModel


interface IGameApi {

    @GET("/games/")
    fun searchGames(@QueryMap options: Map<String, String>): Flowable<List<GameModel>>
}