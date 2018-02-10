package swapp.items.com.swappify.controller.home.game.viewmodel

import com.google.firebase.firestore.GeoPoint
import swapp.items.com.swappify.controller.SwapApplication
import swapp.items.com.swappify.controller.base.BaseViewModel
import swapp.items.com.swappify.firebase.utils.Result
import swapp.items.com.swappify.repo.game.GameRepository
import swapp.items.com.swappify.repo.user.model.User
import javax.inject.Inject

class GamesViewModel @Inject constructor(gameRepository: GameRepository, application: SwapApplication): BaseViewModel(application) {

    init {
        gameRepository.getNearByGames(GeoPoint(12.859925, 77.6022088))
                .subscribe({
                    handleOnSuccess(it)
                }, {
                    handleOnError(it)
                })
    }


    private fun handleOnSuccess(result: Result<List<User>>?) = result?.also {
        if (result.isSuccess()) {
            val userList = it.value
        } else {
            handleOnError(it.error)
        }
    }

    private fun handleOnError(error: Throwable?) {
        val error = error
    }
}
