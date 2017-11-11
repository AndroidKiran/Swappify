package swapp.items.com.swappify.controllers.country.viewmodel

import swapp.items.com.swappify.data.AppDataManager
import swapp.items.com.swappify.data.AppUtilManager
import swapp.items.com.swappify.injection.scopes.PerActivity
import javax.inject.Inject

@PerActivity
class CountryPickerDataManager @Inject constructor(appUtilManager: AppUtilManager?): AppDataManager(appUtilManager)
