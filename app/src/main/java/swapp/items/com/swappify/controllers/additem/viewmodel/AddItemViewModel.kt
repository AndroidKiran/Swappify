package swapp.items.com.swappify.controllers.additem.viewmodel

import swapp.items.com.swappify.controllers.SwapApplication
import swapp.items.com.swappify.controllers.additem.ui.AddItemNavigator
import swapp.items.com.swappify.controllers.base.BaseViewModel
import swapp.items.com.swappify.injection.scopes.PerActivity
import javax.inject.Inject

@PerActivity
class AddItemViewModel @Inject constructor(addItemDataManager: AddItemDataManager?, application: SwapApplication): BaseViewModel<AddItemNavigator>(application) {

}