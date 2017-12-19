package swapp.items.com.swappify.common.extension

import android.support.annotation.AnimatorRes
import android.support.annotation.IdRes
import android.support.v4.app.Fragment
import android.support.v7.app.AppCompatActivity


fun AppCompatActivity.replaceFragmentSafely(fragment: Fragment,
                                            @IdRes containerViewId: Int,
                                            tag: String?,
                                            addToBackStack: Boolean = false,
                                            allowStateLoss: Boolean = false,
                                            @AnimatorRes enterAnimation: Int = 0,
                                            @AnimatorRes exitAnimation: Int = 0,
                                            @AnimatorRes popEnterAnimation: Int = 0,
                                            @AnimatorRes popExitAnimation: Int = 0) {

    if (addToBackStack) {
        val fragmentHistory = supportFragmentManager.findFragmentByTag(tag)
        if (fragmentHistory != null) {
            supportFragmentManager.popBackStack();
        }
    }

    val ft = supportFragmentManager
            .beginTransaction()
            .setCustomAnimations(enterAnimation, exitAnimation, popEnterAnimation, popExitAnimation)
            .replace(containerViewId, fragment, tag)

    if (addToBackStack) {
        ft.addToBackStack(tag)
    }

    if (!supportFragmentManager.isStateSaved) {
        ft.commit()
    } else if (allowStateLoss) {
        ft.commitAllowingStateLoss()
    }
}

fun AppCompatActivity.isFragmentVisible(tag: String): Boolean {
    val fragment = supportFragmentManager.findFragmentByTag(tag)
    return fragment != null && fragment.userVisibleHint
}
