package swapp.items.com.swappify.controller.profile.viewmodel

import swapp.items.com.swappify.controller.SwapApplication
import swapp.items.com.swappify.controller.base.BaseViewModel
import swapp.items.com.swappify.repo.user.LoginRepository
import javax.inject.Inject

class ProfileViewModel @Inject constructor(loginRepository: LoginRepository, swapApplication: SwapApplication): BaseViewModel(swapApplication) {

}
