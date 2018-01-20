package swapp.items.com.swappify.controller.launcher.viewmodel

import swapp.items.com.swappify.controller.SwapApplication
import swapp.items.com.swappify.controller.base.BaseViewModel
import swapp.items.com.swappify.injection.scopes.PerActivity
import swapp.items.com.swappify.repo.AppUtilManager
import javax.inject.Inject

@PerActivity
class LauncherViewModel @Inject constructor(appUtilManager: AppUtilManager, application: SwapApplication): BaseViewModel(application) {

    val preferenceHelper = appUtilManager.preferencesHelper

}