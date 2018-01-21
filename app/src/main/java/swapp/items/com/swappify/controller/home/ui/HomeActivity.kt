package swapp.items.com.swappify.controller.home.ui

import android.arch.lifecycle.ViewModelProvider
import android.arch.lifecycle.ViewModelProviders
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.design.widget.TabLayout
import android.support.v4.app.Fragment
import dagger.android.AndroidInjector
import dagger.android.DispatchingAndroidInjector
import dagger.android.support.HasSupportFragmentInjector
import swapp.items.com.swappify.BR
import swapp.items.com.swappify.BuildConfig
import swapp.items.com.swappify.R
import swapp.items.com.swappify.controller.base.BaseActivity
import swapp.items.com.swappify.controller.home.viewmodel.HomeViewModel
import swapp.items.com.swappify.databinding.ActivityHomeBinding
import javax.inject.Inject

class HomeActivity : BaseActivity<ActivityHomeBinding, HomeViewModel>(), HasSupportFragmentInjector {

    @Inject
    lateinit var viewFactory: ViewModelProvider.Factory

    @Inject
    lateinit var fragmentDispatchingAndroidInjector: DispatchingAndroidInjector<Fragment>

    @Inject
    lateinit var homePagerAdapter: HomePagerAdapter

    @Inject
    lateinit var homeViewModel: HomeViewModel

    lateinit var activityHomeBinding: ActivityHomeBinding

    override fun getViewModel(): HomeViewModel {
        homeViewModel = ViewModelProviders.of(this@HomeActivity,
                viewFactory).get(HomeViewModel::class.java)
        return homeViewModel
    }

    override fun getLayoutId() = R.layout.activity_home

    override fun executePendingVariablesBinding() {
        activityHomeBinding = getViewDataBinding()
        activityHomeBinding.setVariable(BR.viewModel, homeViewModel)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        activityHomeBinding.viewpager.adapter = homePagerAdapter

        activityHomeBinding.tabs.addTab(activityHomeBinding.tabs.newTab().setIcon(R.drawable.vc_cross_white))
        activityHomeBinding.tabs.addTab(activityHomeBinding.tabs.newTab().setIcon(R.drawable.vc_cross_black))
        activityHomeBinding.tabs.addTab(activityHomeBinding.tabs.newTab().setIcon(R.drawable.vc_add_a_photo))

        activityHomeBinding.viewpager.offscreenPageLimit = activityHomeBinding.tabs.tabCount

        activityHomeBinding.tabs.setupWithViewPager(activityHomeBinding.viewpager)

        activityHomeBinding.tabs.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
            override fun onTabSelected(tab: TabLayout.Tab) {
                activityHomeBinding.viewpager.currentItem = tab.position
            }

            override fun onTabUnselected(tab: TabLayout.Tab) {

            }

            override fun onTabReselected(tab: TabLayout.Tab) {

            }
        })
    }

    companion object {

        const val MAIN_ACTION = BuildConfig.APPLICATION_ID + ".action" + ".MAIN_ACTION"

        fun start(context: Context)
                = Intent(context, HomeActivity::class.java)
    }


    override fun supportFragmentInjector(): AndroidInjector<Fragment> = fragmentDispatchingAndroidInjector

}