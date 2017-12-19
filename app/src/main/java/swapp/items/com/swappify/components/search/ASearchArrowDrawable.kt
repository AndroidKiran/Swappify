package swapp.items.com.swappify.components.search

import android.animation.ObjectAnimator
import android.content.Context
import android.util.Property
import android.view.animation.AccelerateDecelerateInterpolator
import swapp.items.com.swappify.components.actiondrawable.SearchArrowDrawable

internal class ASearchArrowDrawable(context: Context) : SearchArrowDrawable(context) {

    fun animate(state: Float, duration: Int) {
        val anim: ObjectAnimator = ObjectAnimator.ofFloat(this, PROGRESS, state,
                if (state == STATE_ARROW) STATE_SEARCH else STATE_ARROW)
        anim.interpolator = AccelerateDecelerateInterpolator()
        anim.duration = duration.toLong()
        anim.start()
    }

    companion object {

        val STATE_ARROW = 0.0f
        val STATE_SEARCH = 1.0f
        private val PROGRESS = object : Property<swapp.items.com.swappify.components.search.ASearchArrowDrawable, Float>(Float::class.java, "progress") {
            override fun set(ASearchArrowDrawable: swapp.items.com.swappify.components.search.ASearchArrowDrawable, value: Float?) {
                ASearchArrowDrawable.progress = value!!
            }

            override fun get(ASearchArrowDrawable: swapp.items.com.swappify.components.search.ASearchArrowDrawable): Float = ASearchArrowDrawable.progress
        }
    }

}
