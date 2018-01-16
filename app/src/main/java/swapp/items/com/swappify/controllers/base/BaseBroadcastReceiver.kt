package swapp.items.com.swappify.controllers.base

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import dagger.android.AndroidInjection


open class BaseBroadcastReceiver: BroadcastReceiver() {

    override fun onReceive(context: Context?, intent: Intent?) {
        AndroidInjection.inject(this@BaseBroadcastReceiver, context)
    }

}