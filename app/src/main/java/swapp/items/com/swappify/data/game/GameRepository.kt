package swapp.items.com.swappify.data.game

import swapp.items.com.swappify.injection.scopes.PerActivity
import javax.inject.Inject

@PerActivity
class GameRepository @Inject constructor(gameApi: GameApi) {

}
