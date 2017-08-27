package swapp.items.com.swappify.intro

import android.support.v4.view.ViewPager
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import swapp.items.com.swappify.R

/**
 * Created by ravi on 26/08/17.
 */
class IntroPageTransformer : ViewPager.PageTransformer {

    private var mPageWidth: Int = 0

    private var mPageWidthTimesPosition: Float = 0f

    private var mAbsPosition: Float = 0f

    private var mIcon: ImageView? = null

    private var mTitle: TextView? = null

    private var mDesc: TextView? = null

    override fun transformPage(page: View?, position: Float) {
        initDimens(page, position)
        initViews(page)
        animate()
    }


    private fun initDimens(page: View?, position: Float) {
        mPageWidth = page?.width?: 0
        mPageWidthTimesPosition = mPageWidth * position
        mAbsPosition = Math.abs(position)
    }

    private fun initViews(page: View?) {
        mIcon = page?.findViewById<ImageView>(R.id.illustratorIcon)
        mTitle = page?.findViewById<TextView>(R.id.title)
        mDesc = page?.findViewById<TextView>(R.id.desc)
    }

    private fun animate() {
        translateX(mIcon, -mPageWidthTimesPosition)
        alphaAnim(mIcon, mAbsPosition)

        translateY(mTitle, -mPageWidthTimesPosition)
        alphaAnim(mTitle, mAbsPosition)

        translateY(mDesc, mPageWidthTimesPosition)
        alphaAnim(mDesc, mAbsPosition)
    }

    private fun alphaAnim(view: View?, absPosition: Float) {
        view?.alpha = 1.0f - absPosition
    }

    private fun translateY(view: View?, pageWidthTimesPosition: Float) {
        view?.translationY = pageWidthTimesPosition / 2f
    }

    private fun translateX(view: View?, pageWidthTimesPosition: Float) {
        view?.translationX = pageWidthTimesPosition / 4f
    }

}