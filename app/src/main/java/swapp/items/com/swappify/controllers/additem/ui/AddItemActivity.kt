package swapp.items.com.swappify.controllers.additem.ui

import android.app.Activity
import android.arch.lifecycle.ViewModelProvider
import android.arch.lifecycle.ViewModelProviders
import android.content.Intent
import android.os.Bundle
import android.support.design.widget.AppBarLayout
import android.support.v4.app.Fragment
import android.support.v4.view.ViewCompat
import android.support.v4.view.ViewPropertyAnimatorListener
import android.view.View
import android.view.animation.AccelerateInterpolator
import dagger.android.AndroidInjector
import dagger.android.DispatchingAndroidInjector
import dagger.android.support.HasSupportFragmentInjector
import swapp.items.com.swappify.BR
import swapp.items.com.swappify.R
import swapp.items.com.swappify.components.SlideAnimation
import swapp.items.com.swappify.controllers.additem.viewmodel.AddItemViewModel
import swapp.items.com.swappify.controllers.base.BaseActivity
import swapp.items.com.swappify.databinding.ActivityAddItemBinding
import javax.inject.Inject


class AddItemActivity : BaseActivity<ActivityAddItemBinding, AddItemViewModel>(),
        HasSupportFragmentInjector, AppBarLayout.OnOffsetChangedListener {

    companion object {
        fun startAddItemActivity(activity: Activity) {
            val intent: Intent = Intent(activity, AddItemActivity::class.java)
            activity.startActivity(intent)
            activity.finish()
        }
    }

    private val PERCENTAGE_TO_SHOW_ANCHOR = 60
    private var isAnchorHidden: Boolean = false
    private var isViewHidden: Boolean = false
    private var maxScrollSize = 0
    private val SCALE_ANIMATION_DURATION: Long = 400
    private val TRANSLATE_ANIMATION_DURATION: Long = 600
    private val MAX_SCALE_VALUE = 1f
    private val MIN_SCALE_VALUE = 0f


    @Inject
    lateinit var fragmentDispatchingAndroidInjector: DispatchingAndroidInjector<Fragment>

    @Inject
    lateinit var viewFactory: ViewModelProvider.Factory

    @Inject
    lateinit var addItemViewModel: AddItemViewModel

    private lateinit var activityAddItemBinding: ActivityAddItemBinding


    override fun getViewModel(): AddItemViewModel {
        addItemViewModel = ViewModelProviders.of(this@AddItemActivity, viewFactory)
                .get(AddItemViewModel::class.java)
        return addItemViewModel
    }

    override fun getLayoutId(): Int = R.layout.activity_add_item;

    override fun executePendingVariablesBinding() {
        activityAddItemBinding = getViewDataBinding()
        activityAddItemBinding.setVariable(BR.addItemViewModel, addItemViewModel)
        activityAddItemBinding.executePendingBindings()
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        activityAddItemBinding.appbar.addOnOffsetChangedListener(this@AddItemActivity)
    }


    override fun onOffsetChanged(appBarLayout: AppBarLayout?, verticalOffset: Int) {
        if (maxScrollSize == 0)
            maxScrollSize = activityAddItemBinding.appbar.getTotalScrollRange()

        val currentScrollPercentage = Math.abs(verticalOffset) * 100 / maxScrollSize

        scaleViewOnScroll(activityAddItemBinding.anchorLayout, currentScrollPercentage)
        handleVisibilityOnScroll(activityAddItemBinding.sliderView, currentScrollPercentage)
    }


    private fun scaleViewOnScroll(view: View?, scrollPercent: Int) {

        if (scrollPercent >= PERCENTAGE_TO_SHOW_ANCHOR) {
            if (!isAnchorHidden) {
                isAnchorHidden = true
                ViewCompat.animate(view)
                        .scaleY(MIN_SCALE_VALUE)
                        .alpha(MIN_SCALE_VALUE)
                        .setInterpolator(AccelerateInterpolator())
                        .setDuration(SCALE_ANIMATION_DURATION)
                        .setListener(object : ViewPropertyAnimatorListener {
                            override fun onAnimationEnd(view: View?) {
                                view?.visibility = View.GONE
                            }

                            override fun onAnimationCancel(view: View?) {
                                // DO nothing
                            }

                            override fun onAnimationStart(view: View?) {
                                // DO nothing
                            }

                        })
                        .start()

            }
        }

        if (scrollPercent < PERCENTAGE_TO_SHOW_ANCHOR) {
            if (isAnchorHidden) {
                isAnchorHidden = false
                ViewCompat.animate(view)
                        .scaleY(MAX_SCALE_VALUE)
                        .alpha(MAX_SCALE_VALUE)
                        .setInterpolator(AccelerateInterpolator())
                        .setDuration(SCALE_ANIMATION_DURATION)
                        .setListener(object : ViewPropertyAnimatorListener {
                            override fun onAnimationEnd(view: View?) {
                                // DO nothing
                            }

                            override fun onAnimationCancel(view: View?) {
                                // DO nothing
                            }

                            override fun onAnimationStart(view: View?) {
                                view?.visibility = View.VISIBLE
                            }

                        })
                        .start()


            }
        }
    }

    private fun handleVisibilityOnScroll(view: View?, scrollPercent: Int) {

        if (scrollPercent >= PERCENTAGE_TO_SHOW_ANCHOR) {
            if (!isViewHidden) {
                isViewHidden = true
                createTransformUpAnimation(view,
                        resources.getDimension(R.dimen.dimen_16).toInt())
            }
        }

        if (scrollPercent < PERCENTAGE_TO_SHOW_ANCHOR) {
            if (isViewHidden) {
                isViewHidden = false
                createTransformUpAnimation(view,
                        resources.getDimension(R.dimen.dimen_110).toInt())
            }
        }
    }

    private fun createTransformUpAnimation(view: View?, targetMargin: Int) {
        val animation = SlideAnimation(view, targetMargin)
        animation.duration = TRANSLATE_ANIMATION_DURATION
        view?.startAnimation(animation)
    }

    override fun supportFragmentInjector(): AndroidInjector<Fragment> = fragmentDispatchingAndroidInjector

}