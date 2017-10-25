package swapp.items.com.swappify.controllers.base

import android.arch.lifecycle.ViewModel
import io.reactivex.disposables.CompositeDisposable
import swapp.items.com.swappify.data.AppDataManager

abstract class BaseViewModel<N> constructor(val dataManager: AppDataManager) : ViewModel() where N : Any {


    lateinit var baseCompositeDisposable: CompositeDisposable

    lateinit var baseNavigator: N

    fun onViewCreated() {
        baseCompositeDisposable = CompositeDisposable()
    }

    fun onDestroyView() {
        baseCompositeDisposable.dispose()
    }
}