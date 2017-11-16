package swapp.items.com.swappify.components

import android.content.Context
import android.support.design.widget.CoordinatorLayout
import android.support.design.widget.FloatingActionButton
import android.support.design.widget.Snackbar
import android.support.v4.view.ViewCompat
import android.util.AttributeSet
import android.view.View


/**
 * Created by ravi on 14/11/17.
 */
class ShrinkBehavior : CoordinatorLayout.Behavior<FloatingActionButton> {

    constructor() {}

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs) {}

    override fun layoutDependsOn(parent: CoordinatorLayout, child: FloatingActionButton, dependency: View): Boolean {
        return dependency is Snackbar.SnackbarLayout
    }

    override fun onDependentViewChanged(parent: CoordinatorLayout, child: FloatingActionButton, dependency: View): Boolean {
        val translationY = getFabTranslationYForSnackbar(parent, child)
        val percentComplete = -translationY / dependency.getHeight()
        val scaleFactor = 1 - percentComplete

        child.scaleX = scaleFactor
        child.scaleY = scaleFactor
        return false
    }

    private fun getFabTranslationYForSnackbar(parent: CoordinatorLayout,
                                              fab: FloatingActionButton): Float {
        var minOffset = 0f
        val dependencies = parent.getDependencies(fab)
        var i = 0
        val z = dependencies.size
        while (i < z) {
            val view = dependencies[i]
            if (view is Snackbar.SnackbarLayout && parent.doViewsOverlap(fab, view)) {
                minOffset = Math.min(minOffset,
                        ViewCompat.getTranslationY(view) - view.getHeight())
            }
            i++
        }

        return minOffset
    }
}