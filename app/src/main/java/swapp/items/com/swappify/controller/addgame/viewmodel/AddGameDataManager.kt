package swapp.items.com.swappify.controller.addgame.viewmodel

import swapp.items.com.swappify.injection.scopes.PerActivity
import swapp.items.com.swappify.repo.AppDataManager
import swapp.items.com.swappify.repo.game.GameRepository
import javax.inject.Inject

@PerActivity
class AddGameDataManager @Inject constructor(val gameRepository: GameRepository) : AppDataManager(gameRepository.appUtilManager)