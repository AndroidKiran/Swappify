package swapp.items.com.swappify.controllers.additem.viewmodel

import swapp.items.com.swappify.data.AppDataManager
import swapp.items.com.swappify.data.AppUtilManager
import swapp.items.com.swappify.data.game.GameRepository
import swapp.items.com.swappify.injection.scopes.PerActivity
import javax.inject.Inject

@PerActivity
class AddItemDataManager @Inject constructor(appUtilManager: AppUtilManager,
                                             val gameRepository: GameRepository) : AppDataManager(appUtilManager)