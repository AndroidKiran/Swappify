package swapp.items.com.swappify.components.search

import android.animation.ObjectAnimator
import android.content.Context
import android.support.v7.graphics.drawable.DrawerArrowDrawable
import android.util.Property
import android.view.animation.AccelerateDecelerateInterpolator

internal class SearchArrowDrawable(context: Context)// mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
    : DrawerArrowDrawable(context) {

    fun animate(state: Float, duration: Int) {
        val anim: ObjectAnimator
        if (state == STATE_ARROW) {
            anim = ObjectAnimator.ofFloat(this, PROGRESS, state, STATE_HAMBURGER)
        } else {
            anim = ObjectAnimator.ofFloat(this, PROGRESS, state, STATE_ARROW)
        }
        anim.interpolator = AccelerateDecelerateInterpolator()
        anim.duration = duration.toLong()
        anim.start()
    }

    companion object {

        val STATE_ARROW = 0.0f
        val STATE_HAMBURGER = 1.0f
        private val PROGRESS = object : Property<SearchArrowDrawable, Float>(Float::class.java, "progress") {
            override fun set(searchArrowDrawable: SearchArrowDrawable, value: Float?) {
                searchArrowDrawable.progress = value!!
            }

            override fun get(searchArrowDrawable: SearchArrowDrawable): Float = searchArrowDrawable.progress
        }
    }

}
