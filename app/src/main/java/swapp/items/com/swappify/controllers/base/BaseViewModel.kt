package swapp.items.com.swappify.controllers.base

import android.arch.lifecycle.ViewModel
import io.reactivex.disposables.CompositeDisposable
import swapp.items.com.swappify.data.DataManagerHelper

abstract class BaseViewModel<N> constructor(private val dataManagerHelper: DataManagerHelper) : ViewModel() where N : Any {

    val dataManager: DataManagerHelper
        get() = dataManagerHelper

    lateinit var baseCompositeDisposable: CompositeDisposable

    lateinit var baseNavigator: N

    fun onViewCreated() {
        baseCompositeDisposable = CompositeDisposable()
    }

    fun onDestroyView() {
        baseCompositeDisposable.dispose()
    }
}