package swapp.items.com.swappify.controller.home.game.viewmodel

import swapp.items.com.swappify.controller.SwapApplication
import swapp.items.com.swappify.controller.base.BaseViewModel
import swapp.items.com.swappify.injection.scopes.PerActivity
import swapp.items.com.swappify.repo.game.GameRepository
import javax.inject.Inject

class GamesViewModel @Inject constructor(gameRepository: GameRepository, application: SwapApplication): BaseViewModel(application) {

}
