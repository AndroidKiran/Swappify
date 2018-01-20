package swapp.items.com.swappify.controller.intro.ui

import android.support.v4.view.ViewPager
import android.view.View
import swapp.items.com.swappify.R

class IntroPageTransformer : ViewPager.PageTransformer {

    private var mPageWidth: Int = 0

    private var mPageWidthTimesPosition: Float = 0f

    private var mAbsPosition: Float = 0f

    private var mIcon: View? = null

    private var mTitle: View? = null

    private var mDesc: View? = null

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
        mIcon = page?.findViewById(R.id.illustratorIcon)
        mTitle = page?.findViewById(R.id.title)
        mDesc = page?.findViewById(R.id.desc)
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