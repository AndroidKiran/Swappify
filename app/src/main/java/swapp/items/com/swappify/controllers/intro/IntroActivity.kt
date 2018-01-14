package swapp.items.com.swappify.controllers.intro

import android.content.Intent
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentPagerAdapter
import android.support.v7.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_intro.*
import swapp.items.com.swappify.R
import swapp.items.com.swappify.controllers.signup.ui.LoginActivity

class IntroActivity : AppCompatActivity() {

    private val NUM_OF_PAGE : Int = 3

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_intro)
        initViewPager()
        initCircleIndicator()
        initSignUp()
    }

    private fun initSignUp() {
        signUpButton.setOnClickListener({
            startActivity(Intent(this@IntroActivity, LoginActivity::class.java))
        })
    }

    private fun initViewPager() {
        viewPager.offscreenPageLimit = NUM_OF_PAGE
        viewPager.adapter = IntroPager(supportFragmentManager)
        viewPager.setPageTransformer(false, IntroPageTransformer())
    }

    private fun initCircleIndicator() {
        circleIndicator.setViewPager(viewPager)
    }


    class IntroPager(fm: FragmentManager?) : FragmentPagerAdapter(fm) {

        override fun getCount(): Int = 3

        override fun getItem(position: Int): Fragment = run {
            IntroFragment newInstance(position)
        }
    }

}
