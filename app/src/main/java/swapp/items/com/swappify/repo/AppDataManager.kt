package swapp.items.com.swappify.repo

import swapp.items.com.swappify.injection.scopes.PerActivity
import javax.inject.Inject

@PerActivity
open class AppDataManager @Inject constructor(val appUtilManager: AppUtilManager)