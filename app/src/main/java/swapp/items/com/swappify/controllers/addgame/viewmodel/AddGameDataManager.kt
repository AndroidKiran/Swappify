package swapp.items.com.swappify.controllers.addgame.viewmodel

import swapp.items.com.swappify.injection.scopes.PerActivity
import swapp.items.com.swappify.repo.AppDataManager
import swapp.items.com.swappify.repo.AppUtilManager
import swapp.items.com.swappify.repo.game.GameRepository
import javax.inject.Inject

@PerActivity
class AddGameDataManager @Inject constructor(appUtilManager: AppUtilManager,
                                             val gameRepository: GameRepository) : AppDataManager(appUtilManager)