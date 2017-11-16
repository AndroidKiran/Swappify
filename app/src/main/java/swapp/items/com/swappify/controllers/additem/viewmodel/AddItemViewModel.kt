package swapp.items.com.swappify.controllers.additem.viewmodel

import android.arch.lifecycle.MutableLiveData
import swapp.items.com.swappify.controllers.SwapApplication
import swapp.items.com.swappify.controllers.additem.model.GameModel
import swapp.items.com.swappify.controllers.additem.ui.IAddItemNavigator
import swapp.items.com.swappify.controllers.base.BaseViewModel
import swapp.items.com.swappify.injection.scopes.PerActivity
import swapp.items.com.swappify.rx.SchedulerProvider
import swapp.items.com.swappify.rx.utils.getObservableAsync
import javax.inject.Inject

@PerActivity
class AddItemViewModel @Inject constructor(addItemDataManager: AddItemDataManager, application: SwapApplication) : BaseViewModel<IAddItemNavigator>(application) {

    private var gameListLiveData: MutableLiveData<List<GameModel>> = MutableLiveData()

    private val schedulerProvider: SchedulerProvider = addItemDataManager.appUtilManager.schedulerProvider

    init {
        addItemDataManager.gameRepository.getGamesOnSearch("halo2")
                .getObservableAsync(schedulerProvider)
                .subscribe({
                    handleSearchOnSucces(it)
                }, {
                    handleSearchOnError(it)
                })
    }

    fun handleSearchOnSucces(gamesList: List<GameModel>) {
        gameListLiveData.value = gamesList
    }

    fun handleSearchOnError(throwable: Throwable) {

    }

}