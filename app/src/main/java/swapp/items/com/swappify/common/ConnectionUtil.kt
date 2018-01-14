package swapp.items.com.swappify.common

import android.content.Context
import android.net.ConnectivityManager
import javax.inject.Inject


class ConnectionUtil @Inject constructor(private val mContext: Context?) {

    val isNetNotConnected: Boolean
        get() = !isNetConnected

    private val isNetConnected: Boolean
        get() {
            if (mContext != null) {
                val cm = mContext.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
                val networkInfo = cm.activeNetworkInfo
                return !(networkInfo == null || !networkInfo.isConnectedOrConnecting)
            } else {
                return false
            }
        }

    companion object {

        fun isNetConnected(context: Context): Boolean {
            val cm = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
            val networkInfo = cm.activeNetworkInfo
            return !(networkInfo == null || !networkInfo.isConnectedOrConnecting)
        }
    }
}