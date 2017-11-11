package swapp.items.com.swappify.controllers.signup.viewModel

import swapp.items.com.swappify.data.AppDataManager
import swapp.items.com.swappify.data.AppUtilManager
import swapp.items.com.swappify.data.user.LoginRemoteService
import swapp.items.com.swappify.injection.scopes.PerActivity
import javax.inject.Inject

@PerActivity
class SignUpLoginDataManager @Inject constructor(appUtilManager: AppUtilManager?,
                                         val loginRemoteService: LoginRemoteService?) : AppDataManager(appUtilManager)