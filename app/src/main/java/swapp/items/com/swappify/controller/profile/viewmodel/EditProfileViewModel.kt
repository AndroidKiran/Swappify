package swapp.items.com.swappify.controller.profile.viewmodel

import android.databinding.ObservableField
import swapp.items.com.swappify.controller.SwapApplication
import swapp.items.com.swappify.controller.base.BaseViewModel
import swapp.items.com.swappify.injection.scopes.PerActivity
import swapp.items.com.swappify.repo.user.LoginRepository
import javax.inject.Inject

@PerActivity
class EditProfileViewModel @Inject constructor(loginRepository: LoginRepository, application: SwapApplication): BaseViewModel(application) {

    var picUri = ObservableField<String>()

    var name = ObservableField<String>()

    var country = ObservableField<String>()

    var city = ObservableField<String>()


}
