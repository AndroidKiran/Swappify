package swapp.items.com.swappify.controllers.addgame.ui

import android.arch.lifecycle.ViewModelProvider
import android.arch.lifecycle.ViewModelProviders
import android.os.Bundle
import android.support.design.widget.AppBarLayout
import android.support.v4.view.ViewCompat
import android.support.v4.view.ViewPropertyAnimatorListener
import android.view.View
import android.view.animation.AccelerateInterpolator
import swapp.items.com.swappify.BR
import swapp.items.com.swappify.R
import swapp.items.com.swappify.components.SlideAnimation
import swapp.items.com.swappify.controllers.addgame.viewmodel.AddGameViewModel
import swapp.items.com.swappify.controllers.base.BaseFragment
import swapp.items.com.swappify.databinding.FragmentAddGameBinding
import javax.inject.Inject

class AddGameFragment : BaseFragment<FragmentAddGameBinding, AddGameViewModel>(),
        AppBarLayout.OnOffsetChangedListener {

    companion object {
        const val PERCENTAGE_TO_SHOW_ANCHOR = 60
        const val SCALE_ANIMATION_DURATION: Long = 400
        const val TRANSLATE_ANIMATION_DURATION: Long = 600
        const val MAX_SCALE_VALUE = 1f
        const val MIN_SCALE_VALUE = 0f
        val FRAGMENT_TAG = AddGameFragment::class.java.simpleName!!
    }

    private var isAnchorHidden: Boolean = false
    private var isViewHidden: Boolean = false
    private var maxScrollSize = 0

    @Inject
    lateinit var viewFactory: ViewModelProvider.Factory

    @Inject
    lateinit var addGameViewModel: AddGameViewModel

    private lateinit var fragmentAddGameBinding: FragmentAddGameBinding

    override fun getLayoutId(): Int = R.layout.fragment_add_game

    override fun getViewModel(): AddGameViewModel {
        addGameViewModel = ViewModelProviders.of(this@AddGameFragment, viewFactory)
                .get(AddGameViewModel::class.java)
        return addGameViewModel
    }

    override fun executePendingVariablesBinding() {
        fragmentAddGameBinding = getViewDataBinding()
        fragmentAddGameBinding.setVariable(BR.addItemViewModel, addGameViewModel)
        fragmentAddGameBinding.executePendingBindings()
    }

    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        fragmentAddGameBinding.appbar.addOnOffsetChangedListener(this@AddGameFragment)
    }

    override fun onOffsetChanged(appBarLayout: AppBarLayout?, verticalOffset: Int) {
        if (maxScrollSize == 0)
            maxScrollSize = fragmentAddGameBinding.appbar.totalScrollRange

        val currentScrollPercentage = Math.abs(verticalOffset) * 100 / maxScrollSize

        scaleViewOnScroll(fragmentAddGameBinding.anchorLayout, currentScrollPercentage)
        handleVisibilityOnScroll(fragmentAddGameBinding.sliderView, currentScrollPercentage)
    }

    private fun scaleViewOnScroll(view: View?, scrollPercent: Int) {

        if (scrollPercent >= PERCENTAGE_TO_SHOW_ANCHOR) {
            if (!isAnchorHidden) {
                isAnchorHidden = true
                scaleAnimation(view = view, scaleFactor = MIN_SCALE_VALUE)
            }
        }

        if (scrollPercent < PERCENTAGE_TO_SHOW_ANCHOR) {
            if (isAnchorHidden) {
                isAnchorHidden = false
                scaleAnimation(view = view, scaleFactor = MAX_SCALE_VALUE)
            }
        }
    }

    private fun handleVisibilityOnScroll(view: View?, scrollPercent: Int) {

        if (scrollPercent >= PERCENTAGE_TO_SHOW_ANCHOR) {
            if (!isViewHidden) {
                isViewHidden = true
                translateAnimation(view, resources.getDimension(R.dimen.dimen_16).toInt())
            }
        }

        if (scrollPercent < PERCENTAGE_TO_SHOW_ANCHOR) {
            if (isViewHidden) {
                isViewHidden = false
                translateAnimation(view, resources.getDimension(R.dimen.dimen_110).toInt())
            }
        }
    }

    private fun translateAnimation(view: View?, targetMargin: Int) {
        val animation = SlideAnimation(view, targetMargin)
        animation.duration = TRANSLATE_ANIMATION_DURATION
        view?.startAnimation(animation)
    }

    private fun scaleAnimation(scaleFactor: Float, view: View?) {
        ViewCompat.animate(view)
                .scaleY(scaleFactor)
                .alpha(scaleFactor)
                .setInterpolator(AccelerateInterpolator())
                .setDuration(SCALE_ANIMATION_DURATION)
                .setListener(object : ViewPropertyAnimatorListener {
                    override fun onAnimationEnd(view: View?) {
                        if (scaleFactor == MIN_SCALE_VALUE) {
                            view?.visibility = View.GONE
                        }
                    }

                    override fun onAnimationCancel(view: View?) {
                        // DO nothing
                    }

                    override fun onAnimationStart(view: View?) {
                        if (scaleFactor == MAX_SCALE_VALUE) {
                            view?.visibility = View.VISIBLE
                        }
                    }

                })
                .start()
    }

}