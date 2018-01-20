package swapp.items.com.swappify.common.extension

import android.content.Intent
import android.support.annotation.AnimatorRes
import android.support.annotation.IdRes
import android.support.v4.app.Fragment
import android.support.v7.app.AppCompatActivity
import android.support.v7.app.AppCompatDialogFragment
import swapp.items.com.swappify.controller.addgame.ui.AddGameActivity

fun AppCompatActivity.addFragmentSafely(fragment: Fragment,
                                        @IdRes containerViewId: Int,
                                        tag: String?,
                                        addToBackStack: Boolean = false,
                                        allowStateLoss: Boolean = false,
                                        @AnimatorRes enterAnimation: Int = 0,
                                        @AnimatorRes exitAnimation: Int = 0,
                                        @AnimatorRes popEnterAnimation: Int = 0,
                                        @AnimatorRes popExitAnimation: Int = 0): Fragment {

    var fragmentHistory = supportFragmentManager.findFragmentByTag(tag)
    if (fragmentHistory == null) {
        fragmentHistory = fragment
        val ft = supportFragmentManager
                .beginTransaction()
                .setCustomAnimations(enterAnimation, exitAnimation, popEnterAnimation, popExitAnimation)
                .add(containerViewId, fragment, tag)

        if (addToBackStack) {
            ft.addToBackStack(tag)
        }

        if (!supportFragmentManager.isStateSaved) {
            ft.commit()
        } else if (allowStateLoss) {
            ft.commitAllowingStateLoss()
        }
    }

    return fragmentHistory
}

fun AppCompatActivity.isFragmentVisible(tag: String): Boolean {
    val fragment = supportFragmentManager.findFragmentByTag(tag)
    return fragment != null && fragment.userVisibleHint
}


fun AppCompatActivity.start(appCompatDialogFragment: AppCompatDialogFragment, tag: String) {
    appCompatDialogFragment.show(supportFragmentManager,tag)
}

fun AppCompatActivity.startAddGameActivity() {
    startActivity(Intent(this, AddGameActivity::class.java))
}




