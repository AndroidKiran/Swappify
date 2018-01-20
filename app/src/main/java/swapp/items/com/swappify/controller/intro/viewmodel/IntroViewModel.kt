package swapp.items.com.swappify.controller.intro.viewmodel

import swapp.items.com.swappify.controller.SwapApplication
import swapp.items.com.swappify.controller.base.BaseViewModel
import swapp.items.com.swappify.injection.scopes.PerActivity
import swapp.items.com.swappify.repo.AppUtilManager
import javax.inject.Inject

@PerActivity
class IntroViewModel @Inject constructor(appUtilManager: AppUtilManager, application: SwapApplication): BaseViewModel(application) {

    val preferenceHelper = appUtilManager.preferencesHelper
}