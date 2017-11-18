package swapp.items.com.swappify.controllers.base

import android.arch.lifecycle.AndroidViewModel
import io.reactivex.disposables.CompositeDisposable
import swapp.items.com.swappify.controllers.SwapApplication

abstract class BaseViewModel constructor(application: SwapApplication) : AndroidViewModel(application) {

    private lateinit var baseCompositeDisposable: CompositeDisposable

    fun onViewCreated() {
        baseCompositeDisposable = CompositeDisposable()
    }

    fun onDestroyView() {
        baseCompositeDisposable.dispose()
    }

    fun getCompositeDisposable(): CompositeDisposable = baseCompositeDisposable
}