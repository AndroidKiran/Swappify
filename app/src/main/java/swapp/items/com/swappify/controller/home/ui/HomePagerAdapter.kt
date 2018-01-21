package swapp.items.com.swappify.controller.home.ui

import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentPagerAdapter
import swapp.items.com.swappify.controller.home.game.ui.GamesFragment
import javax.inject.Inject


class HomePagerAdapter @Inject constructor(fm: FragmentManager) : FragmentPagerAdapter(fm) {

    override fun getCount(): Int = TAB_COUNT

    override fun getItem(i: Int): Fragment = GamesFragment()

    override fun getPageTitle(position: Int): CharSequence = "Tab " + position.toString()

    companion object {
        const val TAB_COUNT = 3
    }
}