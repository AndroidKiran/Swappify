package swapp.items.com.swappify.controllers

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import swapp.items.com.swappify.BuildConfig
import swapp.items.com.swappify.R

class MainActivity : AppCompatActivity() {

    val MAIN_ACTION = BuildConfig.APPLICATION_ID + ".action" + ".MAIN_ACTION"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main);
    }
}