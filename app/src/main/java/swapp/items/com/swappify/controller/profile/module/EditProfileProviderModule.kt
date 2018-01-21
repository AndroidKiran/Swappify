package swapp.items.com.swappify.controller.profile.module

import android.arch.lifecycle.ViewModel
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap
import swapp.items.com.swappify.controller.profile.viewmodel.EditProfileViewModel
import swapp.items.com.swappify.injection.qualifiers.ViewModelKey

@Module
abstract class EditProfileProviderModule {

    @Binds
    @IntoMap
    @ViewModelKey(EditProfileViewModel::class)
    abstract fun provideEditProfileViewModel(viewModel: EditProfileViewModel): ViewModel

}
