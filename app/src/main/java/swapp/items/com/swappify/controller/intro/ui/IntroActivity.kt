package swapp.items.com.swappify.controller.intro.ui

import android.arch.lifecycle.ViewModelProvider
import android.arch.lifecycle.ViewModelProviders
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentPagerAdapter
import dagger.android.AndroidInjector
import dagger.android.DispatchingAndroidInjector
import dagger.android.support.HasSupportFragmentInjector
import kotlinx.android.synthetic.main.activity_intro.*
import swapp.items.com.swappify.BR
import swapp.items.com.swappify.R
import swapp.items.com.swappify.common.Constant.Companion.INTRO_COMPLETED
import swapp.items.com.swappify.controller.base.BaseActivity
import swapp.items.com.swappify.controller.intro.viewmodel.IntroViewModel
import swapp.items.com.swappify.controller.signup.ui.LoginActivity
import swapp.items.com.swappify.databinding.ActivityIntroBinding
import javax.inject.Inject

class IntroActivity : BaseActivity<ActivityIntroBinding, IntroViewModel>(), HasSupportFragmentInjector {


    @Inject
    lateinit var viewFactory: ViewModelProvider.Factory

    @Inject
    lateinit var fragmentDispatchingAndroidInjector: DispatchingAndroidInjector<Fragment>

    private lateinit var activityIntroViewBinding: ActivityIntroBinding

    @Inject
    lateinit var introViewModel: IntroViewModel

    override fun getViewModel(): IntroViewModel {
        introViewModel = ViewModelProviders.of(this@IntroActivity,
                viewFactory).get(IntroViewModel::class.java)
        return introViewModel    }

    override fun getLayoutId() = R.layout.activity_intro

    override fun executePendingVariablesBinding() {
        activityIntroViewBinding = getViewDataBinding()
        activityIntroViewBinding.setVariable(BR.viewModel, introViewModel)
    }

    private val NUM_OF_PAGE : Int = 3

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_intro)
        initViewPager()
        initPageIndicator()
        initSignUp()
    }

    private fun initSignUp() {
        signUpButton.setOnClickListener({
            introViewModel.preferenceHelper.set(INTRO_COMPLETED, true)
            startActivity(Intent(this@IntroActivity, LoginActivity::class.java))
        })
    }

    private fun initViewPager() {
        viewPager.offscreenPageLimit = NUM_OF_PAGE
        viewPager.adapter = IntroPager(supportFragmentManager)
        viewPager.setPageTransformer(false, IntroPageTransformer())
    }

    private fun initPageIndicator() {
        indicator.setViewPager(viewPager)
    }


    class IntroPager(fm: FragmentManager?) : FragmentPagerAdapter(fm) {

        override fun getCount(): Int = 3

        override fun getItem(position: Int): Fragment = run {
            IntroFragment newInstance(position)
        }
    }

    override fun supportFragmentInjector(): AndroidInjector<Fragment> = fragmentDispatchingAndroidInjector

    companion object {
        fun start(context: Context) = Intent(context, IntroActivity::class.java)
    }

}
