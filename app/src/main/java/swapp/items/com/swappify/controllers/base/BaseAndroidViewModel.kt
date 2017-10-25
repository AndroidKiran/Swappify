package swapp.items.com.swappify.controllers.base

import android.app.Application
import android.arch.lifecycle.AndroidViewModel
import android.content.Context
import io.reactivex.disposables.CompositeDisposable
import swapp.items.com.swappify.data.AppDataManager


abstract class BaseAndroidViewModel<N> constructor(val dataManager: AppDataManager,
                                                   val context: Context) :
        AndroidViewModel(context as Application?) where N : Any {


    private var baseCompositeDisposable: CompositeDisposable? = null

    private var baseNavigator: N? = null

    fun onViewCreated() {
        baseCompositeDisposable = CompositeDisposable()
    }

    fun onDestroyView() {
        baseCompositeDisposable?.dispose()
    }

    fun setBaseNavigator(navigator: N) {
        baseNavigator = navigator
    }

    fun getNavigator(): N? = baseNavigator

    fun getCompositeDisposable(): CompositeDisposable? = baseCompositeDisposable
}