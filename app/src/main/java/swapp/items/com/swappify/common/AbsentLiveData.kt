package swapp.items.com.swappify.common

import android.arch.lifecycle.LiveData

class AbsentLiveData<T> private constructor() : LiveData<T>() {

    init {
        postValue(null)
    }

    companion object {
        fun <T> create(): LiveData<T> = AbsentLiveData()
    }
}
