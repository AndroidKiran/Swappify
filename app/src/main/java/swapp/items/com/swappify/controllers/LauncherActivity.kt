package swapp.items.com.swappify.controllers

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import swapp.items.com.swappify.R
import swapp.items.com.swappify.controllers.signup.ui.LoginActivity

class LauncherActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_launcher)

        startActivity(startIntroActivity(this))
    }

    private fun startMainActivity(context : Context ) : Intent =
            Intent(context , MainActivity::class.java)


    private fun startIntroActivity(context : Context ) : Intent =
            Intent(context , LoginActivity::class.java)
}
