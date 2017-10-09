package swapp.items.com.swappify.controllers.base

import android.app.Application
import android.arch.lifecycle.AndroidViewModel
import android.content.Context
import io.reactivex.disposables.CompositeDisposable
import swapp.items.com.swappify.data.DataManagerHelper


abstract class BaseAndroidViewModel<N> constructor(private val dataManagerHelper: DataManagerHelper,
                                                   private val application: Context) :
        AndroidViewModel(application as Application?) where N : Any {

    val context: Context
        get() = application.applicationContext

    val dataManager: DataManagerHelper
        get() = dataManagerHelper

    lateinit var baseCompositeDisposable: CompositeDisposable

    lateinit var baseNavigator: N

    abstract fun onRetryClick()

    fun onViewCreated() {
        baseCompositeDisposable = CompositeDisposable()
    }

    fun onDestroyView() {
        baseCompositeDisposable.dispose()
    }
}