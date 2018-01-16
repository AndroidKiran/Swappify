package swapp.items.com.swappify.components.networkconnection

import android.arch.lifecycle.Lifecycle
import android.arch.lifecycle.LifecycleObserver
import android.arch.lifecycle.OnLifecycleEvent
import android.content.IntentFilter
import android.net.ConnectivityManager
import android.support.v7.app.AppCompatActivity
import swapp.items.com.swappify.common.SingleLiveEvent
import swapp.items.com.swappify.controllers.base.NetworkConnectionBroadcastReceiver

class NetworkConnectionLifeCycleObserver constructor(val lifecycle: Lifecycle, private val netConnected: SingleLiveEvent<Boolean>, private val appCompatActivity: AppCompatActivity): LifecycleObserver, NetworkConnectionBroadcastReceiver.InternetConnectionChangeListener {

    private lateinit var networkConnectionBroadCaseReceiver: NetworkConnectionBroadcastReceiver

    init {
        lifecycle.addObserver(this@NetworkConnectionLifeCycleObserver)
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_CREATE)
    fun onCreate(){
        networkConnectionBroadCaseReceiver = NetworkConnectionBroadcastReceiver()
        networkConnectionBroadCaseReceiver.setConnectionChangeListener(this)
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_START)
    fun onStart() {
        appCompatActivity.registerReceiver(networkConnectionBroadCaseReceiver, IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION))
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_STOP)
    fun onStop() {
        appCompatActivity.unregisterReceiver(networkConnectionBroadCaseReceiver)
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_DESTROY)
    fun onDestroy() {
        lifecycle.removeObserver(this@NetworkConnectionLifeCycleObserver)
    }

    override fun onConnectionChanged(netAvailable: Boolean) {
        netConnected.value = netAvailable
    }
}