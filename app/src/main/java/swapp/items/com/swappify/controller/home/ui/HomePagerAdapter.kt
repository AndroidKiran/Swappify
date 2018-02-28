package swapp.items.com.swappify.controller.home.ui

import android.graphics.drawable.Drawable
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentPagerAdapter
import javax.inject.Inject


class HomePagerAdapter @Inject constructor(fm: FragmentManager) : FragmentPagerAdapter(fm) {

    private val fragments = mutableListOf<Fragment>()
    private val icons = mutableListOf<Drawable>()

    override fun getCount(): Int = fragments.size

    override fun getItem(position: Int): Fragment = fragments[position]

    override fun getPageTitle(position: Int): CharSequence = ""

    fun addFragment(fragment: Fragment?, iconDrawable: Drawable?) {
        fragment?.apply {
            fragments.add(this)
        }

        iconDrawable?.apply {
            icons.add(this)
        }
    }

    fun getIcons(): List<Drawable> = icons
}