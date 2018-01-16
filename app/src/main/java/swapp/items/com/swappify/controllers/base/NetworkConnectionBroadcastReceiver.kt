package swapp.items.com.swappify.controllers.base

import android.content.Context
import android.content.Intent
import android.net.ConnectivityManager
import android.net.ConnectivityManager.CONNECTIVITY_ACTION
import swapp.items.com.swappify.injection.scopes.PerActivity
import javax.inject.Inject

@PerActivity
class NetworkConnectionBroadcastReceiver @Inject constructor(): BaseBroadcastReceiver() {

    private var internetConnectionChangeListener: InternetConnectionChangeListener? = null

    private fun isInternetAvailable(context: Context?): Boolean {
        return try {
            val cm = context?.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
            val activeNetwork = cm.activeNetworkInfo
            activeNetwork != null && activeNetwork.isConnectedOrConnecting
        } catch (exception: Exception) {
            false
        }
    }

    override fun onReceive(context: Context?, intent: Intent?) {

        if (intent?.action.equals(CONNECTIVITY_ACTION) && internetConnectionChangeListener != null) {
            internetConnectionChangeListener?.onConnectionChanged(isInternetAvailable(context))
        }
    }

    fun setConnectionChangeListener(internetConnectionChangeListener: InternetConnectionChangeListener?) {
        this.internetConnectionChangeListener = internetConnectionChangeListener
    }

    interface InternetConnectionChangeListener {
        fun onConnectionChanged(netAvailable: Boolean)
    }
}
