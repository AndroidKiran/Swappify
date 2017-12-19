package swapp.items.com.swappify.controllers.country.viewmodel

import swapp.items.com.swappify.repo.AppDataManager
import swapp.items.com.swappify.repo.AppUtilManager
import swapp.items.com.swappify.injection.scopes.PerActivity
import javax.inject.Inject

@PerActivity
class CountryPickerDataManager @Inject constructor(appUtilManager: AppUtilManager): AppDataManager(appUtilManager)
