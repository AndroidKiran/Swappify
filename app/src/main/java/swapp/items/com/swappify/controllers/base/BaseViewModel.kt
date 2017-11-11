package swapp.items.com.swappify.controllers.base

import android.arch.lifecycle.AndroidViewModel
import io.reactivex.disposables.CompositeDisposable
import swapp.items.com.swappify.controllers.SwapApplication

abstract class BaseViewModel<N> constructor(application: SwapApplication) : AndroidViewModel(application) where N : Any {

    private lateinit var baseCompositeDisposable: CompositeDisposable

    private lateinit var baseNavigator: N

    fun onViewCreated() {
        baseCompositeDisposable = CompositeDisposable()
    }

    fun onDestroyView() {
        baseCompositeDisposable.dispose()
    }

    fun setNavigator(navigator: N?) {
        baseNavigator = navigator!!
    }

    fun getNavigator(): N = baseNavigator

    fun getCompositeDisposable(): CompositeDisposable = baseCompositeDisposable
}