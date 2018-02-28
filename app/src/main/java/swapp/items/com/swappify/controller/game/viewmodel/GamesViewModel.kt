package swapp.items.com.swappify.controller.game.viewmodel

import android.arch.lifecycle.MutableLiveData
import swapp.items.com.swappify.common.extension.switchMap
import swapp.items.com.swappify.controller.SwapApplication
import swapp.items.com.swappify.controller.addgame.model.GameModel
import swapp.items.com.swappify.controller.base.BaseViewModel
import swapp.items.com.swappify.controller.game.ui.model.GameFilter
import swapp.items.com.swappify.repo.game.GameRepository
import java.util.*
import javax.inject.Inject

class GamesViewModel @Inject constructor(private val gameRepository: GameRepository, application: SwapApplication) : BaseViewModel(application) {


    var gameFilterLiveData = MutableLiveData<GameFilter>()
    var gamesLiveData = gameFilterLiveData.switchMap {
        if (it == null) {
            MutableLiveData<List<GameModel>>()
        } else {
            getGames(it.userNumber, it.gameName, it.genre, it.createdAt)
        }
    }

    var searchGamesFilterLiveData = MutableLiveData<GameFilter>()
    var searchGamesLiveData = searchGamesFilterLiveData.switchMap {
        if (it == null) {
            MutableLiveData<List<GameModel>>()
        } else {
            getGames(it.userNumber, it.gameName, it.genre, it.createdAt)
        }
    }

    fun getGames(userNumber: String? = null, gameName: String? = null, genre: String? = null, createdAt: Date? = null) =
            gameRepository.getGames(userNumber, gameName, genre, createdAt)

    fun updateGameFilter(userNumber: String? = null, gameName: String? = null, genre: String? = null, createdAt: Date? = null) {

       /* val updateGameFilterLiveData = gameFilterLiveData.value?.also { gameFilter ->
            userNumber?.let { gameFilter.userNumber = it }
            gameName?.let { gameFilter.gameName = it }
            genre?.let { gameFilter.genre = it }
            createdAt?.let { gameFilter.createdAt = it }
        }*/

        val gameFilter = GameFilter(userNumber, gameName, genre, createdAt)
        gameFilterLiveData.value = gameFilter
    }

}
