package swapp.items.com.swappify.controllers.additem.module

import android.arch.lifecycle.ViewModel
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap
import swapp.items.com.swappify.controllers.additem.viewmodel.AddItemViewModel
import swapp.items.com.swappify.injection.qualifiers.ViewModelKey

@Module
abstract class AddItemProviderModule {

    @Binds
    @IntoMap
    @ViewModelKey(AddItemViewModel::class)
    abstract fun bindAddItemViewModel(viewModel: AddItemViewModel): ViewModel

//    @ContributesAndroidInjector(modules = arrayOf(AddItemViewModel::class))
//    abstract fun provideAddItemByApiFragment(): AddItemByApiFragment
//
//    @ContributesAndroidInjector(modules = arrayOf(AddItemViewModel::class))
//    abstract fun provideAddItemManuallyFragment(): AddItemManuallyFragment
}