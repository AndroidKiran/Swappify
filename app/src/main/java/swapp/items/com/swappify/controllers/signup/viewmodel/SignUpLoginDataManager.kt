package swapp.items.com.swappify.controllers.signup.viewmodel

import swapp.items.com.swappify.injection.scopes.PerActivity
import swapp.items.com.swappify.repo.AppDataManager
import swapp.items.com.swappify.repo.AppUtilManager
import swapp.items.com.swappify.repo.user.LoginRemoteService
import javax.inject.Inject

@PerActivity
class SignUpLoginDataManager @Inject constructor(appUtilManager: AppUtilManager,
                                         val loginRemoteService: LoginRemoteService) : AppDataManager(appUtilManager)