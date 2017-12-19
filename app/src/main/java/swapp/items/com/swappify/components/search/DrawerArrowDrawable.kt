package swapp.items.com.swappify.components.search

import android.animation.ObjectAnimator
import android.content.Context
import android.support.v7.graphics.drawable.DrawerArrowDrawable
import android.util.Property
import android.view.animation.AccelerateDecelerateInterpolator

internal class DrawerArrowDrawable(context: Context) : DrawerArrowDrawable(context) {

    fun animate(state: Float, duration: Int) {
        val anim: ObjectAnimator = if (state == STATE_ARROW) {
            ObjectAnimator.ofFloat(this, PROGRESS, state, STATE_HAMBURGER)
        } else {
            ObjectAnimator.ofFloat(this, PROGRESS, state, STATE_ARROW)
        }
        anim.interpolator = AccelerateDecelerateInterpolator()
        anim.duration = duration.toLong()
        anim.start()
    }

    companion object {

        val STATE_ARROW = 0.0f
        val STATE_HAMBURGER = 1.0f
        private val PROGRESS = object : Property<swapp.items.com.swappify.components.search.DrawerArrowDrawable, Float>(Float::class.java, "progress") {
            override fun set(drawerArrowDrawable: swapp.items.com.swappify.components.search.DrawerArrowDrawable, value: Float?) {
                drawerArrowDrawable.progress = value!!
            }

            override fun get(drawerArrowDrawable: swapp.items.com.swappify.components.search.DrawerArrowDrawable): Float = drawerArrowDrawable.progress
        }
    }

}
