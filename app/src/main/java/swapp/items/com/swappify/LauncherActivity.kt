package swapp.items.com.swappify

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import swapp.items.com.swappify.intro.IntroActivity

class LauncherActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_launcher)

        startActivity(startIntroActivity(this))
    }


    fun startMainActivity(context : Context ) : Intent {
        return Intent(context , MainActivity::class.java)
    }


    fun startIntroActivity(context : Context ) : Intent {
        return Intent(context , IntroActivity::class.java)
    }
}
