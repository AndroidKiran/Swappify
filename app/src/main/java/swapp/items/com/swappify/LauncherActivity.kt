package swapp.items.com.swappify

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity

class LauncherActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_launcher)

        startActivity(startMainActivity(this))
    }


    fun startMainActivity(context : Context ) : Intent {
        return Intent(context , MainActivity::class.java)
    }
}
