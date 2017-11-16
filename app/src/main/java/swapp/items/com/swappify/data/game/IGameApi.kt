package swapp.items.com.swappify.data.game

import io.reactivex.Observable
import retrofit2.http.GET
import retrofit2.http.QueryMap
import swapp.items.com.swappify.controllers.additem.model.GameModel


interface IGameApi {

    @GET("/games/")
    fun searchGames(@QueryMap options: Map<String, String>): Observable<List<GameModel>>
}