package swapp.items.com.swappify.base

import android.app.Application
import android.arch.lifecycle.AndroidViewModel
import android.databinding.ObservableBoolean
import io.reactivex.disposables.CompositeDisposable
import swapp.items.com.swappify.data.DataManagerHelper

/**
 * Created by ravi on 03/10/17.
 */
abstract class BaseAndroidViewModel constructor(private val dataManager: DataManagerHelper, application: Application?) : AndroidViewModel(application) {

    private val mIsLoading = ObservableBoolean(false)

    private lateinit var mCompositeDisposable: CompositeDisposable

    init {
        var context = application!!.applicationContext
    }

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