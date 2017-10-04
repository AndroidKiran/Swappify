package swapp.items.com.swappify.base

import android.arch.lifecycle.ViewModel
import android.databinding.ObservableBoolean
import io.reactivex.disposables.CompositeDisposable
import swapp.items.com.swappify.data.DataManagerHelper

abstract class BaseViewModel<N> constructor(private val dataManager: DataManagerHelper): ViewModel() where N: Any {

    private val mIsLoading = ObservableBoolean(false)

    private lateinit var mCompositeDisposable: CompositeDisposable

    private lateinit var mNavigator: N

    fun setNavigator(navigator: N){
        this.mNavigator = navigator
    }

    fun getNavigator(): N = mNavigator


    fun onViewCreated() {
        this.mCompositeDisposable = CompositeDisposable()
    }

    fun onDestroyView() {
        mCompositeDisposable.dispose()
    }

    fun getDataManager(): DataManagerHelper = dataManager


    fun getCompositeDisposable(): CompositeDisposable = mCompositeDisposable

    fun getIsLoading(): ObservableBoolean = mIsLoading

    fun setIsLoading(isLoading: Boolean) {
        mIsLoading.set(isLoading)
    }
}