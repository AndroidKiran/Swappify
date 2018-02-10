package swapp.items.com.swappify.mvvm

import android.app.Activity
import android.arch.lifecycle.Lifecycle
import android.arch.lifecycle.LifecycleObserver
import android.arch.lifecycle.OnLifecycleEvent
import com.google.android.gms.location.places.PlaceDetectionClient
import com.google.android.gms.location.places.Places



class PlacePickerLifeCyclerObserver constructor(private val lifecycle: Lifecycle, private  val activity: Activity): LifecycleObserver {

    private lateinit var placeDetectionClient: PlaceDetectionClient

    init {
        lifecycle.apply { addObserver(this@PlacePickerLifeCyclerObserver) }
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_CREATE)
    fun onCreate() {
        placeDetectionClient = Places.getPlaceDetectionClient(activity, null)
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_STOP)
    fun onStop() {
        lifecycle.apply { removeObserver(this@PlacePickerLifeCyclerObserver) }
    }
}
