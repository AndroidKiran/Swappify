package swapp.items.com.swappify.components.search

import android.animation.Animator
import android.annotation.TargetApi
import android.content.Context
import android.os.Build
import android.view.View
import android.view.ViewAnimationUtils
import android.view.animation.AccelerateDecelerateInterpolator
import android.view.animation.AlphaAnimation
import android.view.animation.Animation
import swapp.items.com.swappify.R


internal object SearchAnimator {

    fun fadeIn(view: View, duration: Int) {
        val anim = AlphaAnimation(0.0f, 1.0f)
        anim.interpolator = AccelerateDecelerateInterpolator()
        anim.duration = duration.toLong()

        view.animation = anim
        view.visibility = View.VISIBLE
    }

    fun fadeOut(view: View, duration: Int) {
        val anim = AlphaAnimation(1.0f, 0.0f)
        anim.interpolator = AccelerateDecelerateInterpolator()
        anim.duration = duration.toLong()

        view.animation = anim
        view.visibility = View.GONE
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    fun revealOpen(view: View, cX: Int, duration: Int, context: Context, editText: SearchEditText, shouldClearOnOpen: Boolean, listener: ISearchOnClickListener?) {
        var cx = cX

        if (cx <= 0) {
            val padding = context.resources.getDimensionPixelSize(R.dimen.dimen_24)
            if (SearchUtils.isRtlLayout(context))
                cx = padding
            else
                cx = view.width - padding
        }

        val cy = context.resources.getDimensionPixelSize(R.dimen.dimen_46) / 2

        if (cx != 0 && cy != 0) {
            val finalRadius = Math.hypot(cx.toDouble(), cy.toDouble()).toFloat()

            val anim = ViewAnimationUtils.createCircularReveal(view, cx, cy, 0.0f, finalRadius)
            anim.interpolator = AccelerateDecelerateInterpolator()
            anim.duration = duration.toLong()
            anim.addListener(object : Animator.AnimatorListener { // new AnimatorListenerAdapter()
                override fun onAnimationStart(animation: Animator) {
                    if (listener != null) {
                        listener.onOpen()
                    }
                }

                override fun onAnimationEnd(animation: Animator) {
                    if (shouldClearOnOpen && editText.length() > 0) {
                        editText.text.clear()
                    }
                    editText.requestFocus()
                }

                override fun onAnimationCancel(animation: Animator) {

                }

                override fun onAnimationRepeat(animation: Animator) {

                }
            })

            view.visibility = View.VISIBLE
            anim.start()
        }
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    fun revealClose(view: View, cX: Int, duration: Int, context: Context, editText: SearchEditText, shouldClearOnClose: Boolean, searchView: SearchView, listener: ISearchOnClickListener?) {
        var cx = cX

        if (cx <= 0) {
            val padding = context.resources.getDimensionPixelSize(R.dimen.dimen_24)
            if (SearchUtils.isRtlLayout(context))
                cx = padding
            else
                cx = view.width - padding
        }

        val cy = context.resources.getDimensionPixelSize(R.dimen.dimen_46) / 2

        if (cx != 0 && cy != 0) {
            val initialRadius = Math.hypot(cx.toDouble(), cy.toDouble()).toFloat()

            val anim = ViewAnimationUtils.createCircularReveal(view, cx, cy, initialRadius, 0.0f)
            anim.interpolator = AccelerateDecelerateInterpolator()
            anim.duration = duration.toLong()
            anim.addListener(object : Animator.AnimatorListener {
                override fun onAnimationStart(animation: Animator) {
                    if (shouldClearOnClose && editText.length() > 0) {
                        editText.text.clear()
                    }
                    editText.clearFocus()
                }

                override fun onAnimationEnd(animation: Animator) {
                    view.visibility = View.GONE
                    searchView.visibility = View.GONE
                    if (listener != null) {
                        listener.onClose()
                    }
                }

                override fun onAnimationCancel(animation: Animator) {

                }

                override fun onAnimationRepeat(animation: Animator) {

                }
            })
            anim.start()
        }
    }

    fun fadeOpen(view: View, duration: Int, editText: SearchEditText, shouldClearOnOpen: Boolean, listener: ISearchOnClickListener?) {
        val anim = AlphaAnimation(0.0f, 1.0f)
        anim.interpolator = AccelerateDecelerateInterpolator()
        anim.duration = duration.toLong()
        anim.setAnimationListener(object : Animation.AnimationListener {
            override fun onAnimationStart(animation: Animation) {
                if (listener != null) {
                    listener.onOpen()
                }
            }

            override fun onAnimationEnd(animation: Animation) {
                if (shouldClearOnOpen && editText.length() > 0) {
                    editText.text.clear()
                }
                editText.requestFocus()
            }

            override fun onAnimationRepeat(animation: Animation) {}
        })

        view.animation = anim
        view.visibility = View.VISIBLE
    }

    fun fadeClose(view: View, duration: Int, editText: SearchEditText, shouldClearOnClose: Boolean, searchView: SearchView, listener: ISearchOnClickListener?) {
        val anim = AlphaAnimation(1.0f, 0.0f)
        anim.interpolator = AccelerateDecelerateInterpolator()
        anim.duration = duration.toLong()
        anim.setAnimationListener(object : Animation.AnimationListener {
            override fun onAnimationStart(animation: Animation) {
                if (shouldClearOnClose && editText.length() > 0) {
                    editText.text.clear()
                }
                editText.clearFocus()
            }

            override fun onAnimationEnd(animation: Animation) {
                view.visibility = View.GONE
                searchView.visibility = View.GONE
                if (listener != null) {
                    listener.onClose()
                }
            }

            override fun onAnimationRepeat(animation: Animation) {}
        })

        view.animation = anim
        view.visibility = View.GONE
    }

}