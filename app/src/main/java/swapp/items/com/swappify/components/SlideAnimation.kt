package swapp.items.com.swappify.components

import android.view.View
import android.view.animation.Animation
import android.view.animation.Transformation
import android.widget.FrameLayout


class SlideAnimation(private val view: View?, private val targetTopMargin: Int) : Animation() {

    var startTopMargin: Int

    init {
        val params = view?.layoutParams as FrameLayout.LayoutParams
        startTopMargin = params.topMargin
    }

    override fun applyTransformation(interpolatedTime: Float, t: Transformation) {
        val params = view?.layoutParams as FrameLayout.LayoutParams
        params.topMargin = (startTopMargin + (targetTopMargin - startTopMargin) * interpolatedTime).toInt()
        view.layoutParams = params
    }

    override fun willChangeBounds(): Boolean = true
}