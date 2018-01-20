package swapp.items.com.swappify.controller.home

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import swapp.items.com.swappify.BuildConfig
import swapp.items.com.swappify.R

class HomeActivity : AppCompatActivity() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main);
    }

    companion object {
        const val MAIN_ACTION = BuildConfig.APPLICATION_ID + ".action" + ".MAIN_ACTION"

        fun start(context: Context)
                = Intent(context, HomeActivity::class.java)
    }
}