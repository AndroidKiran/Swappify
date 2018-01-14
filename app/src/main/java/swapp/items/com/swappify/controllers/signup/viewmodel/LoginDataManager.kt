package swapp.items.com.swappify.controllers.signup.viewmodel

import swapp.items.com.swappify.repo.AppDataManager
import swapp.items.com.swappify.repo.user.LoginRepository
import javax.inject.Inject


class LoginDataManager @Inject constructor(val loginRepository: LoginRepository) : AppDataManager(loginRepository.appUtilManager)